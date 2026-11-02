package cn.edu.sicnu.cardgame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GameActivity extends Activity {
    private CardMatchingGame game;
    private final PlayingCardView[] buttons = new PlayingCardView[16];
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Player.init(this);
        setContentView(R.layout.activity_game);
        game = getLastNonConfigurationInstance() instanceof CardMatchingGame
            ? (CardMatchingGame)getLastNonConfigurationInstance() : new CardMatchingGame(buttons.length);
        GridLayout grid = findViewById(R.id.cards);
        Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.drawable.yaoji_cards);
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            PlayingCardView button = new PlayingCardView(this, artwork);
            GridLayout.LayoutParams p = new GridLayout.LayoutParams(GridLayout.spec(i / 4), GridLayout.spec(i % 4, 1f));
            p.width = 0; p.height = GridLayout.LayoutParams.WRAP_CONTENT;
            int gap = (int)(3 * getResources().getDisplayMetrics().density);
            p.setMargins(gap, gap, gap, gap);
            grid.addView(button, p);
            buttons[i] = button;
            button.setOnClickListener(v -> { game.chooseCardAtIndex(index); update(); });
        }
        findViewById(R.id.restart).setOnClickListener(v -> { game = new CardMatchingGame(buttons.length); update(); });
        findViewById(R.id.exit).setOnClickListener(v -> finish());
        update();
    }
    private void update() {
        Player.best = Math.max(Player.best, game.getScore());
        ((TextView)findViewById(R.id.score)).setText(getString(R.string.score, game.getScore()));
        for (int i = 0; i < buttons.length; i++) {
            Card c = game.cardAtIndex(i);
            String face = c.isChosen() || c.isMatched() ? c.toString() : getString(R.string.back);
            buttons[i].show(c);
            buttons[i].setEnabled(!c.isMatched());
            buttons[i].setContentDescription(getString(R.string.card_description, i + 1, face));
        }
    }
    @Override public Object onRetainNonConfigurationInstance() { return game; }
}
