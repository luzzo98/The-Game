package sd.Akka.Messages;

import sd.Akka.Actor.PlayerActor;

/**
 * Message to send to all the {@link PlayerActor} in the game; it says to a player that his turn can start and
 * notify the others that the last player has drawn at the end of his turn.
 */
public class StartTurnMsg extends StandardMsg {

    private final String nextPlayerName;

    /**
     * Create a message to send when a player can start his turn because the previous has finished while the others
     * will update their {@link sd.Model.GameState} making him draw.
     *
     * @param receiver the name of the actor to whom send the message.
     * @param playerEndedTurn a {@link String} with the name of the player who has ended his turn.
     * @param nextPlayerName a {@link String} with the name of the player who can start to play.
     */
    public StartTurnMsg(final String receiver, final String playerEndedTurn, final String nextPlayerName) {
        super(receiver, playerEndedTurn);
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
