package cn.edu.sicnu.cardgame

class Card(private var suit: String, private var rank: String, var isChosen:Boolean = false, var isMatched:Boolean = false)  {
    companion object {
        val rankStrings= arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    /**
     * 计算本牌与 otherCards 组成的一组牌的分值。
     * 实验五改为“三张牌”的游戏：otherCards 有两个元素时按三张牌规则判定；
     * 只有一个元素时保留实验四的两张牌规则，方便单独比较两张牌。
     */
    fun match(otherCards: Array<Card>): Int {
        val group = ArrayList<Card>(otherCards.size + 1)
        group.add(this)
        group.addAll(otherCards)
        return when (group.size) {
            2 -> {
                val otherCard = group[1]
                when {
                    otherCard.rank == rank -> 4
                    otherCard.suit == suit -> 1
                    else -> 0
                }
            }
            3 -> when {
                // 三张牌点数全部相同：最高奖励
                group.all { it.rank == rank } -> 8
                // 三张牌花色全部相同：次高奖励
                group.all { it.suit == suit } -> 6
                else -> 0
            }
            else -> 0
        }
    }
}