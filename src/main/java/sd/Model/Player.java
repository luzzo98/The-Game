package sd.Model;

import java.util.List;

/**
 * Interface that describe a player and his information.
 */
public interface Player {

    /**
     * The nickname of the player.
     *
     * @return player's name.
     */
    String getName();

    /**
     * Gives the cards that will be the player's hand.
     *
     * @param cards the cards of the player.
     */
    void setHand(List<Integer> cards);

    /**
     * Method used to obtain the cards in the player's hand.
     *
     * @return a {@link List} with the value of player's cards.
     */
    List<Integer> getHand();

    /**
     * Specify a card that the player has played.
     *
     * @param card the value of the card played.
     */
    void playedCard(Integer card);
}
