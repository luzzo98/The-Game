package sd.Akka.Messages;

import sd.Akka.Actor.PlayerActor;

/**
 * Message to send to {@link PlayerActor}; it says that it's possible to create the {@link sd.View.WaitingGUI}.
 */
public class CreateWaitingGUIMsg extends StandardMsg {

    private final String hostName;

    /**
     * Create a message to send when the player creates a new game or joins an existing one, to create the GUI with
     * the waiting room.
     *
     * @param receiver the name of the actor to whom send the message.
     * @param hostName the name of the player who created the game.
     * @param difficulty a {@link String} with the {@link sd.Model.Difficulty} value of the game.
     */
    public CreateWaitingGUIMsg(final String receiver, final String hostName, final String difficulty) {
        super(receiver, difficulty);
        this.hostName = hostName;
    }

    /**
     * Get the name of the player who created the game.
     *
     * @return a {@link String} with the name of the player.
     */
    public String getHostName() {
        return hostName;
    }
}
