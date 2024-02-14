package sd.Akka.Actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import sd.Akka.Messages.AddPlayerMsg;
import sd.Akka.Messages.DealCardsMsg;
import sd.Akka.Messages.StartGameMsg;
import sd.Akka.Messages.UpdateWaitingRoomMsg;
import sd.Utils.ClusterHelper;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * An actor used to collect the name of the player that want to play a match together; it collects the name of the
 * players and will notify them when the game will start.
 */
public class WaitingRoomActor extends AbstractLoggingActor {

    private Map<String, ActorRef> players = new LinkedHashMap<>(); // name of a player and his reference
    private String hostName;
    private ActorRef gameStateRegion;

    /**
     * Creates a Props configuration for the PlayerActor. This method is used to define the properties and
     * configuration for creating instances of the PlayerActor.
     *
     * @return A {@link Props} object configured for the PlayerActor.
     */
    public static Props props() {
        return Props.create(WaitingRoomActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddPlayerMsg.class, this::handleAddPlayer)
                .match(StartGameMsg.class, this::handleStartGame)
                .matchAny(message -> System.out.println("\n# UNEXPECTED MESSAGE TO " + getSelf() + ": " + message))
                .build();
    }

    /**
     * Add a new player to the list of who will play the game.
     *
     * @param addPlayerMsg the {@link AddPlayerMsg} with the name of the new player who joined the game.
     */
    private void handleAddPlayer(final AddPlayerMsg addPlayerMsg) {
        if (hostName == null && players.isEmpty()) {
            hostName = addPlayerMsg.getContent();
        }
        players.put(addPlayerMsg.getContent(), sender());
        players.forEach(
                (name, ref) -> ref.tell(new UpdateWaitingRoomMsg(name, new LinkedList<>(players.keySet())), getSelf())
        );
    }

    /**
     * The player who created the game decided to start so the {@link GameStateActor} can be created.
     *
     * @param startGameMsg the {@link StartGameMsg} with the difficulty as msg.
     */
    private void handleStartGame(final StartGameMsg startGameMsg) {
        if (gameStateRegion == null) {
            gameStateRegion = ClusterHelper.getGameStateRegion(getContext().getSystem());
        }
        gameStateRegion.tell(new DealCardsMsg(hostName, startGameMsg.getContent(), players), getSelf());
        players = new LinkedHashMap<>();
    }
}
