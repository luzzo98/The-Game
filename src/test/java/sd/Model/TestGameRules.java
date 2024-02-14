package sd.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestGameRules {

    private static final int LOWER = 30;
    private static final int HIGHER = 55;

    /**
     * Check if you can play a card in a deck.
     */
    @Test
    public void testValidPlay() {
        Assertions.assertFalse(GameRules.isAscValid(HIGHER, LOWER));
        Assertions.assertFalse(GameRules.isDescValid(LOWER, HIGHER));

        Assertions.assertTrue(GameRules.isAscValid(LOWER, HIGHER));
        Assertions.assertTrue(GameRules.isAscValid(HIGHER, HIGHER - GameRules.getTrickValue()));
        Assertions.assertTrue(GameRules.isDescValid(HIGHER, LOWER));
        Assertions.assertTrue(GameRules.isAscValid(LOWER, LOWER - GameRules.getTrickValue()));
    }

    /**
     * Check what cards are in the main deck, the number of cards to play each turn and the number of cards you must
     * have in hand based on the difficulty chosen.
     */
    @Test
    public void testCardsRules() {
        Assertions.assertTrue(GameRules.mainDeckCards().containsAll(List.of(
                GameRules.getLowerCard(), GameRules.getHigherCard()
        )));
        Assertions.assertFalse(GameRules.mainDeckCards().containsAll(List.of(
                GameRules.getLowerCard() - 1, GameRules.getHigherCard() + 1
        )));

        Assertions.assertEquals(GameRules.getNormalAmount(), GameRules.cardsPerTurn(Difficulty.NORMAL));
        Assertions.assertEquals(GameRules.getHardAmount(), GameRules.cardsPerTurn(Difficulty.DIFFICULT));
        Assertions.assertEquals(GameRules.getHardAmount(), GameRules.cardsPerTurn(Difficulty.IMPOSSIBLE));

        Assertions.assertEquals(GameRules.getCardsForOne(), GameRules.cardsInHand(1, Difficulty.NORMAL));
        Assertions.assertEquals(GameRules.getCardsForTwo(), GameRules.cardsInHand(2, Difficulty.NORMAL));
        Assertions.assertEquals(GameRules.getCardsForMore(), GameRules.cardsInHand(3, Difficulty.NORMAL));
        Assertions.assertEquals(GameRules.getCardsForMore(), GameRules.cardsInHand(5, Difficulty.NORMAL));

        Assertions.assertEquals(GameRules.getCardsForOne() - 1, GameRules.cardsInHand(1, Difficulty.IMPOSSIBLE));
        Assertions.assertEquals(GameRules.getCardsForTwo() - 1, GameRules.cardsInHand(2, Difficulty.IMPOSSIBLE));
        Assertions.assertEquals(GameRules.getCardsForMore() - 1, GameRules.cardsInHand(3, Difficulty.IMPOSSIBLE));
        Assertions.assertEquals(GameRules.getCardsForMore() - 1, GameRules.cardsInHand(5, Difficulty.IMPOSSIBLE));
    }
}
