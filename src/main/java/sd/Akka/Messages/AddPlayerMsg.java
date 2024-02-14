package sd.Akka.Messages;

import sd.Akka.Actor.WaitingRoomActor;
import sd.Utils.ClusterHelper;

/**
 * Message to send to {@link WaitingRoomActor}; it says that a new player joined the game.
 */
public class AddPlayerMsg extends StandardMsg {

    /**
     * Create a message to send when a new player joined the game end has to be added in the waiting room.
     *
     * @param hostName the name of the player who created the game.
     * @param playerToAdd the name of the player who want to join the game.
     */
    public AddPlayerMsg(final String hostName, final String playerToAdd) {
        super(hostName + ClusterHelper.getWaitingRoomExtension(), playerToAdd);
    }
}
