package sd.Akka.Messages;

import sd.Akka.Actor.GameStateActor;
import sd.Akka.Actor.PlayerActor;

/**
 * Message to send to {@link GameStateActor} and {@link PlayerActor}; it says that a player couldn't
 * play the minimum amount of cards in his turn, so it's a Game Over and all the player have to be informed. First the
 * player who generate the Game Over sends this message to the {@link GameStateActor} and then he sends the same
 * message to all the players.
 */
public class GameOverMsg extends StandardMsg {

    /**
     * Create a message to inform that there is a Game Over.
     *
     * @param receiver the name of the actor to whom send the message.
     */
    public GameOverMsg(final String receiver) {
        super(receiver, null);
    }
}
