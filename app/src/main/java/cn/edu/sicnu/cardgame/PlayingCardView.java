package cn.edu.sicnu.cardgame;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

/** 57×87 扑克牌；角标与点数直接取自模型，避免图片点数与游戏规则不一致。 */
public final class PlayingCardView extends View {
    /** 牌面素材只需解码一次，供 RecyclerView 中所有牌共用。 */
    private static Bitmap artwork;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Rect source = new Rect();
    private final RectF target = new RectF();
    private final Path outline = new Path();
    private Card card;

    public PlayingCardView(Context context) {
        this(context, null);
    }

    public PlayingCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ensureArtwork();
        setClickable(true);
        setFocusable(true);
    }

    private void ensureArtwork() {
        if (artwork == null) {
            artwork = BitmapFactory.decodeResource(getResources(), R.drawable.yaoji_cards);
        }
    }

    /** 绑定要显示的牌，之后由 onDraw 绘制正面或背面。 */
    void show(Card card) {
        this.card = card;
        invalidate();
    }

    @Override protected void onMeasure(int widthSpec, int heightSpec) {
        int width = resolveSize((int)(80 * getResources().getDisplayMetrics().density), widthSpec);
        setMeasuredDimension(width, Math.round(width * 87f / 57f));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (card == null || artwork == null || getWidth() == 0) return;
        canvas.save();
        canvas.scale(getWidth() / 570f, getHeight() / 870f);
        outline.reset();
        outline.addRoundRect(2, 2, 568, 868, 24, 24, Path.Direction.CW);
        canvas.clipPath(outline);
        paint.setColor(Color.WHITE);
        canvas.drawColor(Color.WHITE);
        boolean faceUp = card.isChosen() || card.isMatched();
        String value = card.toString();
        String suit = value.substring(0, 1);
        String rank = value.substring(1);
        if (!faceUp) {
            drawPanel(canvas, 0);
        } else {
            // J/Q/K 的人物画使用同一套素材，角标和花色由模型绘制。
            int court = rank.equals(Card.Companion.getRankStrings()[10]) ? 1
                : rank.equals(Card.Companion.getRankStrings()[11]) ? 2
                : rank.equals(Card.Companion.getRankStrings()[12]) ? 3 : 0;
            if (court > 0) drawPanel(canvas, court);
            paint.setColor(suit.equals(Card.Companion.getValidSuits()[0])
                || suit.equals(Card.Companion.getValidSuits()[1]) ? Color.rgb(185, 24, 37) : Color.rgb(25, 25, 28));
            drawIndex(canvas, rank, suit);
            canvas.save();
            canvas.rotate(180, 285, 435);
            drawIndex(canvas, rank, suit);
            canvas.restore();
            if (court == 0) drawPips(canvas, rank, suit);
            else {
                pip(canvas, suit, 140, 210, 65, false);
                pip(canvas, suit, 430, 660, 65, true);
            }
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.rgb(160, 160, 160));
        canvas.drawRoundRect(2, 2, 568, 868, 24, 24, paint);
        paint.setStyle(Paint.Style.FILL);
        if (card.isMatched()) {
            paint.setColor(Color.argb(80, 255, 255, 255));
            canvas.drawRoundRect(2, 2, 568, 868, 24, 24, paint);
        }
        canvas.restore();
    }

    private void drawPanel(Canvas canvas, int panel) {
        int left = artwork.getWidth() * panel / 4;
        int right = artwork.getWidth() * (panel + 1) / 4;
        source.set(left, 0, right, artwork.getHeight());
        if (panel == 0) target.set(0, 0, 570, 870);
        else target.set(85, 150, 485, 720);
        canvas.drawBitmap(artwork, source, target, paint);
    }

    private void drawIndex(Canvas canvas, String rank, String suit) {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
        paint.setTextSize(90);
        canvas.drawText(rank, 61, 100, paint);
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
        paint.setTextSize(82);
        canvas.drawText(suit, 61, 177, paint);
    }

    private void pip(Canvas canvas, String suit, float x, float y, int size, boolean upsideDown) {
        canvas.save();
        if (upsideDown) canvas.rotate(180, x, y);
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(size);
        canvas.drawText(suit, x, y - (paint.ascent() + paint.descent()) / 2, paint);
        canvas.restore();
    }

    private void drawPips(Canvas canvas, String rank, String suit) {
        int count = 1;
        String[] ranks = Card.Companion.getRankStrings();
        for (int i = 0; i < 10; i++) if (ranks[i].equals(rank)) count = i + 1;
        if (count == 1) { pip(canvas, suit, 285, 435, 210, false); return; }
        if (count <= 3) {
            pip(canvas, suit, 285, 230, 105, false);
            pip(canvas, suit, 285, 640, 105, true);
            if (count == 3) pip(canvas, suit, 285, 435, 105, false);
            return;
        }
        int rows = count >= 9 ? 4 : count >= 6 ? 3 : 2;
        for (int row = 0; row < rows; row++) {
            float y = 220 + row * 430f / (rows - 1);
            pip(canvas, suit, 180, y, 100, y > 435);
            pip(canvas, suit, 390, y, 100, y > 435);
        }
        if (count == 5 || count == 7 || count == 9) pip(canvas, suit, 285, 435, 100, false);
        if (count == 8 || count == 10) {
            pip(canvas, suit, 285, 325, 100, false);
            pip(canvas, suit, 285, 545, 100, true);
        }
    }

    @Override public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(android.widget.Button.class.getName());
    }
}