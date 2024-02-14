package sd.Controller;

import akka.actor.ActorRef;
import sd.Akka.Actor.WaitingRoomActor;

import java.util.List;

/**
 * Interface used to describe how to manage a {@link sd.View.WaitingGUI}.
 */
public interface WaitingController {

    /**
     * Create and display the {@link sd.View.WaitingGUI} where the user can wait other player before the start of the
     * game.
     */
    void create();

    /**
     * Refresh the {@link sd.View.WaitingGUI} to show all the players' names that will play the game.
     *
     * @param playerNames the {@link List} with the names of the players.
     */
    void updatePlayers(List<String> playerNames);

    /**
     * Set the reference of the {@link WaitingRoomActor}; it's used to comunicate the start of the game when
     * the host press the button.
     *
     * @param ref the {@link ActorRef} of the {@link WaitingRoomActor}.
     */
    void setWaitingRoomActorReference(ActorRef ref);

    /**
     * Dispose the GUI.
     */
    void dispose();

    /**
     * Comunicate to the other players in the waiting room that the game can start.
     */
    void comunicateStart();
}
