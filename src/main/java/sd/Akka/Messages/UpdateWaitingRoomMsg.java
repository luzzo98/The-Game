package sd.Akka.Messages;

import sd.Akka.Actor.PlayerActor;

import java.util.List;

/**
 * Message to send to {@link PlayerActor}; it says that a new player joined the game so he can update the list
 * of the players' name in the {@link sd.View.WaitingGUI}.
 */
public class UpdateWaitingRoomMsg extends StandardMsg {

    private final List<String> playerNames;

    /**
     * Create a message to make a {@link PlayerActor} update his {@link sd.View.WaitingGUI}.
     *
     * @param receiver the name of the actor to whom send the message.
     * @param playerNames a {@link List} with the names of the players who will be in the game.
     */
    public UpdateWaitingRoomMsg(final String receiver, final List<String> playerNames) {
        super(receiver, null);
        this.playerNames = playerNames;
    }

    /**
     * Get the names of the players that will play the game.
     *
     * @return A {@link List} with the names of the players.
     */
    public List<String> getPlayerNames() {
        return playerNames;
    }
}
