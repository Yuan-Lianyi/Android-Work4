package cn.edu.sicnu.cardgame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 游戏控制器：负责把模型（CardMatchingGame）、视图（RecyclerView + PlayingCardView）
 * 和适配器（CardAdapter）连接起来，并处理旋转屏幕等配置变化。
 */
public class GameActivity extends Activity implements CardAdapter.OnCardClickListener {
    /** 第三张牌翻开后停留的时间，让玩家看清牌面再判定。 */
    private static final long REVEAL_DELAY_MS = 800L;

    private CardMatchingGame game;
    private CardAdapter adapter;
    private TextView scoreView;
    private RecyclerView cards;

    /** 延时判定：三张牌都展示过后再结算，避免第三张牌一闪而过。 */
    private final Runnable judgeTask = () -> {
        if (!game.isAwaitingMatch()) return;
        game.judgeChosen();
        adapter.notifyCardsChanged();
        update();
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Player.init(this);
        setContentView(R.layout.activity_game);

        // 旋转屏幕后沿用同一局游戏，避免重新发牌。
        Object retained = getLastNonConfigurationInstance();
        game = retained instanceof CardMatchingGame
            ? (CardMatchingGame) retained
            : new CardMatchingGame(getResources().getInteger(R.integer.card_count));

        scoreView = findViewById(R.id.score);
        cards = findViewById(R.id.cards);
        cards.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.card_columns)));
        cards.setHasFixedSize(true);
        adapter = new CardAdapter(game, this);
        cards.setAdapter(adapter);

        findViewById(R.id.restart).setOnClickListener(v -> {
            cards.removeCallbacks(judgeTask);
            game.reset();
            adapter.notifyCardsChanged();
            update();
        });
        findViewById(R.id.exit).setOnClickListener(v -> finish());

        // 旋转屏幕时如果正好有三张牌等待判定，继续展示计时。
        if (game.isAwaitingMatch()) {
            cards.postDelayed(judgeTask, REVEAL_DELAY_MS);
        }
        update();
    }

    @Override public void onCardClick(int position) {
        if (position == RecyclerView.NO_POSITION || game.isAwaitingMatch()) {
            return;
        }
        game.chooseCardAtIndex(position);
        adapter.notifyCardsChanged();
        update();
        if (game.isAwaitingMatch()) {
            // 先把第三张牌展示出来，再判定。
            cards.postDelayed(judgeTask, REVEAL_DELAY_MS);
        }
    }

    private void update() {
        Player.best = Math.max(Player.best, game.getScore());
        scoreView.setText(getString(R.string.score, game.getScore()));
    }

    @Override protected void onDestroy() {
        cards.removeCallbacks(judgeTask);
        super.onDestroy();
    }

    @Override public Object onRetainNonConfigurationInstance() {
        return game;
    }
}