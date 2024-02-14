package sd.Akka.Messages;

import sd.Akka.Actor.GameStateActor;
import sd.Akka.Actor.PlayerActor;

/**
 * Message to send to {@link GameStateActor} or {@link PlayerActor}; it says that a card was played.
 */
public class PlayedCardMsg extends StandardMsg {

    private final int cardValue;
    private final int deckNumber;

    /**
     * Create a message to send when a player play a card and the other players must be informed.
     *
     * @param receiver a {@link String} with the name of the actor to whom send the message.
     * @param playerName a {@link String} with the name of the player who played the card.
     * @param cardValue the value of the card.
     * @param deckNumber the number of the deck where the card was played.
     */
    public PlayedCardMsg(final String receiver, final String playerName, final int cardValue, final int deckNumber) {
        super(receiver, playerName);
        this.cardValue = cardValue;
        this.deckNumber = deckNumber;
    }

    /**
     * Get the value of the played card.
     *
     * @return an int with the value of the card.
     */
    public int getCardValue() {
        return cardValue;
    }

    /**
     * Get the number of the deck where the card was played.
     *
     * @return an int with the number of the deck.
     */
    public int getDeckNumber() {
        return deckNumber;
    }
}
