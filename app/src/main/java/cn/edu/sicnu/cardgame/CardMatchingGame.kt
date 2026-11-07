package cn.edu.sicnu.cardgame

/**
 * 游戏模型。实验五在三张牌规则下工作：翻开三张牌后由 [Card.match] 判定，
 * 三张牌点数相同或花色相同即配对成功，否则三张牌一起翻回背面并扣分。
 *
 * 为了让玩家看清第三张牌，[chooseCardAtIndex] 只负责翻牌，
 * 判定由控制器在短暂延时后调用 [judgeChosen] 完成。
 */
class CardMatchingGame(private val count: Int) {
    var score = 0
        private set

    private val cards: MutableList<Card> = mutableListOf()

    companion object {
        /** 一次最多同时翻开三张牌（实验五的三张牌规则）。 */
        const val MAX_CHOSEN = 3
    }

    init {
        deal()
    }

    val cardCount: Int
        get() = cards.size

    fun cardAtIndex(index: Int): Card {
        return cards.get(index)
    }

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    /** 三张牌都已翻开，等待控制器判定。 */
    val isAwaitingMatch: Boolean
        get() = openedCards().size == MAX_CHOSEN

    /** 重新发牌并清零得分，供“重新开始”使用。 */
    fun reset() {
        deal()
        score = 0
    }

    /**
     * 翻开或合上一张牌。三张牌都翻开后不再立即判定，
     * 由控制器把三张牌展示一会儿后再调用 [judgeChosen]。
     */
    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (card.isMatched) {
            return
        }
        if (card.isChosen) {
            card.isChosen = false
            return
        }
        // 已经有三张牌翻开、等待判定时，忽略新的点击。
        if (isAwaitingMatch) {
            return
        }
        score -= COST_TO_CHOOSE
        card.isChosen = true
    }

    /**
     * 判定当前翻开的三张牌。配对成功则三张牌保持翻开并加分，
     * 失败则三张牌一起翻回背面并扣分。
     * @return 是否配对成功
     */
    fun judgeChosen(): Boolean {
        val opened = openedCards()
        if (opened.size != MAX_CHOSEN) {
            return false
        }
        val matchScore = opened[0].match(opened.drop(1).toTypedArray())
        return if (matchScore > 0) {
            score += matchScore * MATCH_BONUS
            opened.forEach { it.isMatched = true }
            true
        } else {
            score -= MISMATCH_PENALTY
            opened.forEach { it.isChosen = false }
            false
        }
    }

    private fun openedCards(): List<Card> {
        return cards.filter { it.isChosen && !it.isMatched }
    }

    private fun deal() {
        val deck = Deck()
        cards.clear()
        for (i in 1..count) {
            val card: Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }
}