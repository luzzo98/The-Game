package sd.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestPlayer {

    /**
     * Check the basic operation with the cards in player's hand.
     */
    @Test
    public void testPlayerHand() {
        String name = "Mario";
        Player player = new PlayerImpl(name);
        Assertions.assertEquals(name, player.getName());
        List<Integer> initialCards = List.of(1, 2, 3);
        player.setHand(initialCards);
        Assertions.assertEquals(player.getHand(), initialCards);
        player.playedCard(1);
        Assertions.assertEquals(player.getHand(), List.of(2, 3));
        player.playedCard(2);
        player.playedCard(3);
        Assertions.assertEquals(player.getHand(), List.of());
    }
}
