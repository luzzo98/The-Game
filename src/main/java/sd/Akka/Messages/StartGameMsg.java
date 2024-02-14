package sd.Akka.Messages;

import sd.Akka.Actor.WaitingRoomActor;
import sd.Utils.ClusterHelper;

/**
 * Message to send to {@link WaitingRoomActor}; it says that the game can start and every player must be
 * informed.
 */
public class StartGameMsg extends StandardMsg {

    /**
     * Create a message to send when the player who created the waiting room want to start the game because all of his
     * friends have joined.
     *
     * @param hostName the name of the actor who created the game.
     * @param difficulty a {@link String} with the difficulty of the game.
     */
    public StartGameMsg(final String hostName, final String difficulty) {
        super(hostName + ClusterHelper.getWaitingRoomExtension(), difficulty);
    }
}
