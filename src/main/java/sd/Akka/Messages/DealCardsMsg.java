package sd.Akka.Messages;

import akka.actor.ActorRef;
import sd.Akka.Actor.GameStateActor;
import sd.Utils.ClusterHelper;

import java.util.Map;

/**
 * Message to send to {@link GameStateActor}; it says that the game can start he can create the
 * {@link sd.Model.GameState} and deal the cards to the players.
 */
public class DealCardsMsg extends StandardMsg {

    private final Map<String, ActorRef> players; // name of a player and his reference
    private final String difficulty;

    /**
     * Create a message to send when the player who created the waiting room want to start the game because all of his
     * friends have joined.
     *
     * @param hostName the name of the actor who created the game.
     * @param players a {@link Map} with the names of the players and the references of their actors.
     * @param difficulty a {@link String} with the difficulty of the game.
     */
    public DealCardsMsg(final String hostName, final String difficulty, final Map<String, ActorRef> players) {
        super(hostName + ClusterHelper.getGameStateExtension(), hostName);
        this.difficulty = difficulty;
        this.players = players;
    }

    /**
     * Get the difficulty of the game.
     *
     * @return a {@link String} with the difficulty value.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Get the {@link Map} with the names of the players and the references of their actors.
     *
     * @return the {@link Map} with these values.
     */
    public Map<String, ActorRef> getPlayers() {
        return players;
    }
}
