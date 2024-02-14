package sd.Controller;

import sd.Akka.Actor.GameStateActor;
import sd.Model.GameState;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Interface to link a {@link sd.View.GameGUI} where the game is played with a {@link sd.Model.GameState} that maintains
 * match information and the {@link sd.Model.GameRules} that explain what is allowed to do.
 */
public interface GameController {

    /**
     * Take the information from the {@link GameState} and create the components in the {@link sd.View.GameGUI} to play
     * the game.
     *
     * @param gameState the {@link GameState} with the information of the game.
     * @param isFirst true if it's the turn of this player and can plays as first, false otherwise and has to wait.
     */
    void startGame(GameState gameState, boolean isFirst);

    /**
     * Enable or disable the component of the GUI; if it's not your turn you can't press any button, when is your turn
     * they will be enabled.
     *
     * @param enabled true to enable, false to disable.
     */
    void setEnabled(boolean enabled);

    /**
     * Update the {@link sd.View.GameGUI} after a player play a card in a specific deck.
     *
     * @param playerName a {@link String} with the name of the player who played the card.
     * @param cardValue the value of the card.
     * @param deckNumber the number of the deck where the card was played.
     */
    void updateAfterPlayerMove(String playerName, int cardValue, int deckNumber);

    /**
     * Update the number of cards of each player in the {@link sd.View.GameGUI}.
     */
    void updatePlayerInfo();

    /**
     * Check if a card il playable in an ascending deck (the one which starts from 1).
     *
     * @param deckValue the value of the last card played in the deck.
     * @param cardSelected the card the player want to play.
     * @return true if the play is possible, false otherwise.
     */
    boolean isAscValid(int deckValue, int cardSelected);

    /**
     * Check if a card il playable in a descending deck (the one which starts from 99).
     *
     * @param deckValue the value of the last card played in the deck.
     * @param cardSelected the card the player want to play.
     * @return true if the play is possible, false otherwise.
     */
    boolean isDescValid(int deckValue, int cardSelected);

    /**
     * Method to count the number of cards in hand of each player.
     *
     * @return a Map with the name of the players and the number of their cards in hand.
     */
    LinkedHashMap<String, Integer> countPlayersCards();

    /**
     * Method that specify a card played by the player in a specific deck.
     *
     * @param cardValue the value of the played card.
     * @param deckNumber the number of the deck where the card is played.
     */
    void playedCard(int cardValue, int deckNumber);

    /**
     * Method to notify the end of the turn to the controller so it can send the message to the next player because his
     * turn can start.
     */
    void endOfTurn();

    /**
     * Method used to give the correct amount of cards from the main deck to the player with the specified name when
     * he has finished his turn.
     *
     * @param playerName a {@link String} with the name of the player who has to draw
     * @return the new hand of the player that contain also the old cards not played this turn.
     */
    List<Integer> draw(String playerName);

    /**
     * Count the remaining cards in the main deck.
     *
     * @return the number of the remaining cards.
     */
    int countMainDeckCards();

    /**
     * Method to update the number of the remaining cards of the main deck in the {@link sd.View.GameGUI} after another
     * player ends his turn and draw.
     */
    void updateRemainingCards();

    /**
     * Check if the player can finish his turn controlling the number of cards to play based on the difficulty and if
     * the main deck is empty.
     *
     * @param cardsPlayedThisTurn the number of cards already played this turn.
     * @return the minimum number of cards to play.
     */
    boolean canFinishTurn(int cardsPlayedThisTurn);

    /**
     * Starts a new game.
     */
    void rematch();

    /**
     * Method used to count the cards not played from the main deck and from the hands of the players.
     *
     * @return the number of card not played.
     */
    int cardsNotPlayed();

    /**
     * Check if the game is finished with a win.
     *
     * @return true if it's a win, false if the game it's not finished
     */
    boolean isWin();

    /**
     * Check if there is a playable card in the hand of the player.
     */
    void checkIfValidMoveExist();

    /**
     * Check if there is a playable card in the hand of the player.
     *
     * @param playerName a {@link String} with the name of the player to be checked if he has a valid move.
     * @return true if exists a playable cards, false otherwise.
     */
    boolean checkIfValidMoveExist(String playerName);

    /**
     * Show a dialog with the Game Over in the {@link sd.View.GameGUI}.
     */
    void comunicateGameOver();

    /**
     * Send a message to the {@link GameStateActor} to notify the other players that there is a Game Over.
     */
    void notifyGameOverToOthers();

    /**
     * Close the program after remove this node from the akka cluster.
     */
    void exitGame();
}






