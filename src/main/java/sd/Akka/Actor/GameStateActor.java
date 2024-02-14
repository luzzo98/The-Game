package sd.Akka.Actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import sd.Akka.Messages.CreateGameGUIMsg;
import sd.Akka.Messages.DealCardsMsg;
import sd.Akka.Messages.EndTurnMsg;
import sd.Akka.Messages.GameOverMsg;
import sd.Akka.Messages.PlayedCardMsg;
import sd.Akka.Messages.StartTurnMsg;
import sd.Model.Difficulty;
import sd.Model.GameState;
import sd.Model.GameStateImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An actor used to create only one {@link GameState} for all the player so in this way there is no problem with the
 * shuffling and the distribution of the cards. It's also used to notify to all players when a card is played, when the
 * turn of a player is finished and when a Game Over occurs.
 */
public class GameStateActor extends AbstractLoggingActor {

    private GameState gameState;
    private Map<String, ActorRef> players; // name of a player and his reference

    /**
     * Creates a Props configuration for the PlayerActor. This method is used to define the properties and
     * configuration for creating instances of the PlayerActor.
     *
     * @return A {@link Props} object configured for the PlayerActor.
     */
    public static Props props() {
        return Props.create(GameStateActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DealCardsMsg.class, this::handleDealCards)
                .match(PlayedCardMsg.class, this::handlePlayedCard)
                .match(EndTurnMsg.class, this::handleEndTurn)
                .match(GameOverMsg.class, this::handleGameOver)
                .matchAny(message -> System.out.println("# UNEXPECTED MESSAGE TO " + getSelf() + ": " + message))
                .build();
    }

    /**
     * Create the {@link GameState} for all the players, distribute the initial cards, define the order of the players
     * and notify them that they can create the {@link sd.View.GameGUI}.
     *
     * @param dealCardsMsg the {@link DealCardsMsg} sent by the {@link WaitingRoomActor} with the name of the host, the
     *                     {@link Difficulty} of the game and a {@link Map} with the name of each player and their
     *                     {@link ActorRef}.
     */
    private void handleDealCards(final DealCardsMsg dealCardsMsg) {
        players = dealCardsMsg.getPlayers();
        Difficulty difficulty = Difficulty.fromString(dealCardsMsg.getDifficulty());
        gameState = new GameStateImpl(new LinkedList<>(players.keySet()), difficulty); // create the GameState
        gameState.initialHand();
        Map<String, String> nextPlayerOf = decideTurnOrder();
        players.forEach((name, ref) -> ref.tell(
                new CreateGameGUIMsg(name, dealCardsMsg.getContent(), nextPlayerOf.get(name), gameState, difficulty),
                getSelf()
        ));
    }

    /**
     * Comunicate the fact that a card was played in a specific deck by a specific player to the other players; to the
     * other players will be sent the same message.
     *
     * @param playedCardMsg the {@link PlayedCardMsg} with the name of the player who has played the card, the value of
     *                      the card and the number of the deck where it was played.
     */
    private void handlePlayedCard(final PlayedCardMsg playedCardMsg) {
        players.forEach((name, ref) -> {
            if (!name.equals(playedCardMsg.getContent())) { // if he isn't the player who played the card
                ref.tell(playedCardMsg, getSelf());
            }
        });
    }

    /**
     * Comunicate to the other players that a player that has ended his turn.
     *
     * @param endTurnMsg the {@link EndTurnMsg} with the name of the player who has ended his turn and the name of the
     *                   next player whose turn will start.
     */
    private void handleEndTurn(final EndTurnMsg endTurnMsg) {
        players.forEach((name, ref) -> ref.tell(
                new StartTurnMsg(name, endTurnMsg.getContent(), endTurnMsg.getNextPlayerName()), getSelf()));
    }

    /**
     * Comunicate to all the players that a Game Over has occurred.
     *
     * @param gameOverMsg the {@link GameOverMsg} that comunicate the Game Over.
     */
    private void handleGameOver(final GameOverMsg gameOverMsg) {
        players.forEach((name, ref) -> ref.tell(new GameOverMsg(name), getSelf()));
    }

    /**
     * Define the order of the players.
     *
     * @return a {@link Map} with the name of a player as a key and the name of the next player after him as value.
     */
    private Map<String, String> decideTurnOrder() {
        Map<String, String> nextPlayers = new HashMap<>();
        List<String> playersList = new LinkedList<>(players.keySet());
        for (int i = 0; i < playersList.size(); i++) {
            nextPlayers.put(playersList.get(i), playersList.get((i + 1) % playersList.size()));
        }
        return nextPlayers;
    }
}
