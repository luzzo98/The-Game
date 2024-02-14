package sd.View;

import java.util.List;

/**
 * Defines the executable operations in the GUI where the game is actually played.
 */
public interface GameGUI {

    /**
     * Create and display the GUI where the user can play the game.
     *
     * @param remainingCards the number of the remaining cards in the main deck.
     */
    void create(int remainingCards);

    /**
     * Enable or disable the component of the GUI; if it's not your turn you can't press any button, when is your turn
     * they will be enabled.
     *
     * @param enabled true to enable, false to disable.
     */
    void setCardsEnabled(boolean enabled);

    /**
     * Remove all the cards from the player's hand and create new cards with the passed values.
     *
     * @param newHand a {@link List} with the values of the cards which will be the new hand of the player.
     */
    void clearAndSetHand(List<Integer> newHand);


    /**
     * Update the number of cards of each player.
     */
    void updatePlayersInfo();

    /**
     * Method to update the number of the remaining cards of the main deck after another player ends his turn and draw.
     */
    void updateRemainingCards();

    /**
     * Update the deck after the move of another player.
     *
     * @param playerName a {@link String} with the name of the player who played the card.
     * @param cardValue the value of the card.
     * @param deckNumber the number of the deck where the card was played.
     */
    void updateAfterPlayerMove(String playerName, int cardValue, int deckNumber);

    /**
     * Show a dialog with the Game Over.
     */
    void comunicateGameOver();
}
