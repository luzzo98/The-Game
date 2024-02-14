package sd.Akka.Messages;

import sd.Akka.Actor.PlayerActor;
import sd.Model.Difficulty;
import sd.Model.GameState;

/**
 * Message to send to {@link PlayerActor}; it says that the game is started and the player can create the
 * {@link sd.View.GameGUI}.
 */
public class CreateGameGUIMsg extends StandardMsg {

    private final GameState gameState;
    private final String nextPlayerName;
    private final Difficulty difficulty;

    /**
     * Create a message to send when the player who created the waiting room started the game and all the player have
     * to be informed to create the {@link sd.View.GameGUI} and obtain the initial state of the game.
     *
     * @param receiver the name of the actor to whom send the message.
     * @param hostName the name of the actor who created the game.
     * @param nextPlayerName the name of the next player who will play after the end of the turn.
     * @param gameState the {@link GameState} for all the players.
     * @param difficulty the {@link Difficulty} of the game.
     */
    public CreateGameGUIMsg(final String receiver, final String hostName, final String nextPlayerName,
                            final GameState gameState, final Difficulty difficulty) {
        super(receiver, hostName);
        this.nextPlayerName = nextPlayerName;
        this.gameState = gameState;
        this.difficulty = difficulty;
    }

    /**
     * Get the name of the next player who will play after the end of the turn.
     *
     * @return a {@link String} with the name of the next player.
     */
    public String getNextPlayerName() {
        return nextPlayerName;
    }

    /**
     * Get the unique {@link GameState} with the initial information about the game.
     *
     * @return a {@link String} with the name of the player.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Get the difficulty of the game chose by the host.
     *
     * @return the {@link Difficulty} of the game.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }
}
