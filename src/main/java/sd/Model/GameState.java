package sd.Model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * Interface that describe the operation you can do in a match related to the state of the game like information about
 * the cards and players.
 */
public interface GameState {

    /**
     * Deal the initial cards to all players.
     */
    void initialHand();

    /**
     * Method used to give the correct amount of cards from the main deck to a player who has finished his turn or at
     * the beginning of the game.
     *
     * @param player the player who has to draw.
     * @return the new hand of the player that contain also the old cards not played this turn.
     * @throws RuntimeException if the player is not present in the game.
     */
    List<Integer> draw(Player player);

    /**
     * Method used to obtain a {@link Player} with his information.
     *
     * @param playerName the name of the player.
     * @return an {@link Optional} with the {@link Player}, if found.
     */
    Optional<Player> getPlayer(String playerName);

    /**
     * Method to count the number of cards in hand of each player.
     *
     * @return a Map with the name of the players and the number of their cards in hand.
     */
    LinkedHashMap<String, Integer> countPlayersCards();

    /**
     * Method that specify a card played by a player in a specific deck.
     *
     * @param player the name of the player.
     * @param card the number of the card played.
     * @param deckNumber the number of the deck where the card was played.
     */
    void playedCard(Player player, int card, int deckNumber);

    /**
     * Count the remaining cards in the main deck.
     *
     * @return the number of the remaining cards.
     */
    int countMainDeckCards();

    /**
     * Method used to count the cards not played from the main deck and from the hands of the players.
     *
     * @return the number of card not played.
     */
    int cardNotPlayed();

    /**
     * Check if there is a playable card in the hand of the player.
     *
     * @param player the {@link Player} to be checked if he has a valid move.
     * @return true if exists a playable cards, false otherwise.
     */
    boolean validMoveExist(Player player);

    /**
     * Check if the game is finished with a win.
     *
     * @return true if it's a win, false if the game it's not finished
     */
    boolean isWin();
}
