package sd.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestGameState {

    private final String name = "Bob";
    private final List<String> players = List.of(name, "Bill", "Alice");
    private static final int TOTAL_CARDS = 97;
    private static final Difficulty NORMAL = Difficulty.NORMAL;
    private GameState gameState;

    /**
     * Check some aspects about the initial distribution of the cards like the numbers of cards, the ones not played
     * and the ones in the players' hand.
     */
    @Test
    public void testInitialPhase() {
        gameState = new GameStateImpl(players, NORMAL);

        Assertions.assertEquals(TOTAL_CARDS, gameState.cardNotPlayed());
        Assertions.assertEquals(TOTAL_CARDS, gameState.countMainDeckCards());

        gameState.initialHand();
        Assertions.assertEquals(TOTAL_CARDS, gameState.cardNotPlayed());
        int remainingCards = TOTAL_CARDS - (players.size() * GameRules.cardsInHand(players.size(), NORMAL));
        Assertions.assertEquals(remainingCards, gameState.countMainDeckCards());

        Map<String, Integer> map = gameState.countPlayersCards();
        players.forEach(player ->
                Assertions.assertEquals(GameRules.cardsInHand(players.size(), NORMAL), map.get(player))
        );
    }

    /**
     * Check some aspects about the player's draw and the numbers of cards in hand.
     */
    @Test
    public void testPlayersDraw() {
        gameState = new GameStateImpl(players, NORMAL);

        Assertions.assertTrue(gameState.getPlayer(name).isPresent());
        Player bob = gameState.getPlayer(name).get();
        Assertions.assertTrue(bob.getHand().isEmpty());

        gameState.initialHand();
        List<Integer> hand = new LinkedList<>(bob.getHand());
        gameState.draw(bob);
        Assertions.assertEquals(hand, bob.getHand());

        bob.getHand().clear();
        Assertions.assertTrue(bob.getHand().isEmpty());
        gameState.draw(bob);
        Assertions.assertEquals(GameRules.cardsInHand(players.size(), NORMAL), bob.getHand().size());
    }

    /**
     * Check some aspects after the cards have been played, like the numbers of cards not played and in hand after play
     * and draw.
     */
    @Test
    public void testPlayGame() {
        gameState = new GameStateImpl(players, NORMAL);
        gameState.initialHand();
        Assertions.assertTrue(gameState.getPlayer(name).isPresent());
        Player bob = gameState.getPlayer(name).get();

        int cardsCounter = bob.getHand().size();
        gameState.playedCard(bob, bob.getHand().get(0), 0);
        Assertions.assertEquals(cardsCounter - 1, bob.getHand().size());
        Assertions.assertEquals(TOTAL_CARDS - 1, gameState.cardNotPlayed());

        List<Integer> handCopy = new LinkedList<>(bob.getHand());
        handCopy.forEach(card -> gameState.playedCard(bob, card, 0));
        Assertions.assertTrue(bob.getHand().isEmpty());
        gameState.draw(bob);
        Assertions.assertEquals(GameRules.cardsInHand(players.size(), NORMAL), bob.getHand().size());
    }
}
