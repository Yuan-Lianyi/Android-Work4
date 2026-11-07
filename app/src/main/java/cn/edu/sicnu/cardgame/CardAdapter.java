package cn.edu.sicnu.cardgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 适配器。把游戏模型中的每张牌绑定到一张 PlayingCardView 上，
 * 并通过 {@link OnCardClickListener} 把点击位置回调给控制器 GameActivity。
 */
class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    /** 牌被点击时的回调，由控制器实现。 */
    interface OnCardClickListener {
        void onCardClick(int position);
    }

    private final OnCardClickListener listener;
    private CardMatchingGame game;

    CardAdapter(CardMatchingGame game, OnCardClickListener listener) {
        this.game = game;
        this.listener = listener;
    }

    /** 一次选择后刷新所有牌的显示，匹配的牌会淡出。 */
    void notifyCardsChanged() {
        notifyItemRangeChanged(0, getItemCount());
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(game.cardAtIndex(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return game == null ? 0 : game.getCardCount();
    }

    /** 一张牌的 ViewHolder，负责绘制牌面并处理点击回调。 */
    static class CardViewHolder extends RecyclerView.ViewHolder {
        private final PlayingCardView cardView;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
        }

        void bind(Card card, int position, OnCardClickListener listener) {
            cardView.show(card);
            cardView.setEnabled(!card.isMatched());
            cardView.setAlpha(card.isMatched() ? 0.55f : 1f);

            boolean faceUp = card.isChosen() || card.isMatched();
            String back = itemView.getContext().getString(R.string.back);
            String face = faceUp ? card.toString() : back;
            itemView.setContentDescription(itemView.getContext()
                .getString(R.string.card_description, position + 1, face));

            itemView.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onCardClick(adapterPosition);
                }
            });
        }
    }
}