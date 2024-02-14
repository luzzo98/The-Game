package sd.Akka.Messages;

import sd.Akka.Actor.GameStateActor;

/**
 * Message to send to {@link GameStateActor}; it says that a player has ended his turn.
 */
public class EndTurnMsg extends StandardMsg {

    private final String nextPlayerName;

    /**
     * Create a message to send when a player ends his turn and the other players must be informed.
     *
     * @param receiver the name of the actor to whom send the message.
     * @param playerName a {@link String} with the name of the player who has ended his turn
     * @param nextPlayerName a {@link String} with the name of the next player who will play after the end of the turn.
     */
    public EndTurnMsg(final String receiver, final String playerName, final String nextPlayerName) {
        super(receiver, playerName);
        this.nextPlayerName = nextPlayerName;
    }

    /**
     * Get the name of the next player who will play because start his turn.
     *
     * @return a {@link String} with the name of the next player.
     */
    public String getNextPlayerName() {
        return nextPlayerName;
    }
}
