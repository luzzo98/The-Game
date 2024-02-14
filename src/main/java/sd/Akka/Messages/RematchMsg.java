package sd.Akka.Messages;

import sd.Akka.Actor.PlayerActor;

/**
 * Message to send to {@link PlayerActor}; it used to make another match so the player will recreate the
 * {@link sd.View.WaitingGUI}.
 */
public class RematchMsg extends StandardMsg {

    /**
     * Create a message to inform the {@link PlayerActor} to recreate the {@link sd.View.WaitingGUI} because the
     * user want a rematch.
     *
     * @param receiver the name of the actor to whom send the message.
     */
    public RematchMsg(final String receiver) {
        super(receiver, null);
    }
}
