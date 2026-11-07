package cn.edu.sicnu.cardgame;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;

public class GameTest {
    /** 三张牌规则：点数全同加8、花色全同加6、其余为0；两张牌时保留实验四规则。 */
    @Test public void cardMatchingRulesForThreeCards() {
        Card card = new Card("♥", "A", false, false);
        assertEquals(8, card.match(new Card[]{new Card("♠", "A", false, false), new Card("♦", "A", false, false)}));
        assertEquals(6, card.match(new Card[]{new Card("♥", "2", false, false), new Card("♥", "3", false, false)}));
        assertEquals(0, card.match(new Card[]{new Card("♠", "2", false, false), new Card("♦", "3", false, false)}));
        assertEquals(4, card.match(new Card[]{new Card("♠", "A", false, false)}));
    }

    @Test public void deckHas52UniqueCards() {
        Deck deck = new Deck();
        HashSet<String> cards = new HashSet<>();
        for (int i = 0; i < 52; i++) assertTrue(cards.add(deck.drawRandomCard().toString()));
        assertNull(deck.drawRandomCard());
    }

    /** 翻开两张牌不会配对；第三张牌翻开后先展示，再由 judgeChosen 判定。 */
    @Test public void threeCardScoring() {
        CardMatchingGame game = new CardMatchingGame(24);
        int match = game.cardAtIndex(0).match(new Card[]{game.cardAtIndex(1), game.cardAtIndex(2)});
        game.chooseCardAtIndex(0);
        game.chooseCardAtIndex(1);
        assertEquals(-2, game.getScore());
        assertTrue(game.cardAtIndex(0).isChosen());
        assertFalse(game.cardAtIndex(0).isMatched());
        assertFalse(game.isAwaitingMatch());

        game.chooseCardAtIndex(2);
        // 第三张牌翻开后只展示、不立刻结算。
        assertEquals(-3, game.getScore());
        assertTrue(game.isAwaitingMatch());
        assertTrue(game.cardAtIndex(2).isChosen());
        assertFalse(game.cardAtIndex(2).isMatched());

        boolean matched = game.judgeChosen();
        assertEquals(match > 0, matched);
        if (match > 0) {
            assertEquals(match * 4 - 3, game.getScore());
            assertTrue(game.cardAtIndex(0).isMatched());
            assertTrue(game.cardAtIndex(1).isMatched());
            assertTrue(game.cardAtIndex(2).isMatched());
            assertTrue(game.cardAtIndex(0).isChosen());
            assertFalse(game.isAwaitingMatch());
        } else {
            assertEquals(-5, game.getScore());
            assertFalse(game.cardAtIndex(0).isChosen());
            assertFalse(game.cardAtIndex(1).isChosen());
            assertFalse(game.cardAtIndex(2).isChosen());
            assertFalse(game.cardAtIndex(0).isMatched());
            assertFalse(game.isAwaitingMatch());
        }
    }

    /** 三张牌展示期间，点击第四张牌应被忽略。 */
    @Test public void fourthCardIgnoredWhileAwaiting() {
        CardMatchingGame game = new CardMatchingGame(24);
        game.chooseCardAtIndex(0);
        game.chooseCardAtIndex(1);
        game.chooseCardAtIndex(2);
        int score = game.getScore();
        game.chooseCardAtIndex(3);
        assertEquals(score, game.getScore());
        assertFalse(game.cardAtIndex(3).isChosen());
        assertTrue(game.isAwaitingMatch());
    }

    @Test public void deselectAndReset() {
        CardMatchingGame game = new CardMatchingGame(24);
        game.chooseCardAtIndex(0);
        game.chooseCardAtIndex(0);
        assertFalse(game.cardAtIndex(0).isChosen());
        assertEquals(-1, game.getScore());

        game.chooseCardAtIndex(1);
        game.reset();
        assertEquals(0, game.getScore());
        assertEquals(24, game.getCardCount());
        for (int i = 0; i < game.getCardCount(); i++) {
            assertFalse(game.cardAtIndex(i).isChosen());
            assertFalse(game.cardAtIndex(i).isMatched());
        }
    }

    @Test public void matchedCardCannotBeChosenAgain() {
        CardMatchingGame game = new CardMatchingGame(24);
        Card first = game.cardAtIndex(0);
        Card second = game.cardAtIndex(1);
        first.setMatched(true);
        second.setMatched(true);
        game.chooseCardAtIndex(0);
        assertEquals(0, game.getScore());
    }
}