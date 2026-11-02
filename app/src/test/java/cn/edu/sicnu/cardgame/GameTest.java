package cn.edu.sicnu.cardgame;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;

public class GameTest {
    @Test public void cardMatchingRules() {
        Card card = new Card("♥", "A", false, false);
        assertEquals(4, card.match(new Card[]{new Card("♠", "A", false, false)}));
        assertEquals(1, card.match(new Card[]{new Card("♥", "2", false, false)}));
        assertEquals(0, card.match(new Card[]{new Card("♠", "2", false, false)}));
    }
    @Test public void deckHas52UniqueCards() {
        Deck deck = new Deck();
        HashSet<String> cards = new HashSet<>();
        for (int i = 0; i < 52; i++) assertTrue(cards.add(deck.drawRandomCard().toString()));
        assertNull(deck.drawRandomCard());
    }
    @Test public void scoringAndMatchedCards() {
        CardMatchingGame game = new CardMatchingGame(16);
        int first = 0, second = 1;
        int match = game.cardAtIndex(first).match(new Card[]{game.cardAtIndex(second)});
        game.chooseCardAtIndex(first);
        assertEquals(-1, game.getScore());
        game.chooseCardAtIndex(second);
        assertEquals(match > 0 ? match * 4 - 2 : -4, game.getScore());
        assertEquals(match > 0, game.cardAtIndex(first).isMatched());
        if (match > 0) {
            int score = game.getScore();
            game.chooseCardAtIndex(first);
            assertEquals(score, game.getScore());
        } else assertFalse(game.cardAtIndex(first).isChosen());
    }
    @Test public void deselectAndNewGame() {
        CardMatchingGame game = new CardMatchingGame(16);
        game.chooseCardAtIndex(0);
        game.chooseCardAtIndex(0);
        assertFalse(game.cardAtIndex(0).isChosen());
        assertEquals(-1, game.getScore());
        game = new CardMatchingGame(16);
        assertEquals(0, game.getScore());
        for (int i = 0; i < 16; i++) {
            assertFalse(game.cardAtIndex(i).isChosen());
            assertFalse(game.cardAtIndex(i).isMatched());
        }
    }
}
