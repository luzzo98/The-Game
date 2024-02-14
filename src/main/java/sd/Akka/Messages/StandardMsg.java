package sd.Akka.Messages;

import java.io.Serializable;

/**
 * A standard message to send to an actor, with a message and a receiver. It is an abstract class because it has to be
 * extended to specify a specific type of message that can be handled correctly by the actors.
 */
public abstract class StandardMsg implements Serializable {

    private final String receiver;
    private final String msg;

    /**
     * Create a message to send to an actor with the passed text.
     *
     * @param receiver the name of the actor to whom send the message.
     * @param msg the text of the message.
     */
    public StandardMsg(final String receiver, final String msg) {
        this.receiver = receiver;
        this.msg = msg;
    }

    /**
     * Get the name of the actor to whom send the message.
     *
     * @return A {@link String} with the name of the actor.
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Get te content of the message.
     *
     * @return A {@link String} with the message.
     */
    public String getContent() {
        return msg;
    }
}
