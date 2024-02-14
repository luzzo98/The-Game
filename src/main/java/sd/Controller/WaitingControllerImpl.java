package sd.Controller;

import akka.actor.ActorRef;
import sd.Akka.Messages.StartGameMsg;
import sd.Model.Difficulty;
import sd.View.SimpleWaitingGUI;
import sd.View.WaitingGUI;

import java.util.List;

/**
 * Class with the implementation of the {@link WaitingController} interface.
 */
public class WaitingControllerImpl implements WaitingController {

    private final WaitingGUI waitingGUI;
    private ActorRef waitingRoomActor;
    private final String hostName;
    private final Difficulty difficulty;

    /**
     * Create the {@link WaitingGUI}.
     *
     * @param hostName a {@link String} with the name of player who started the game.
     * @param playerName a {@link String} with the name of the player who uses the GUI.
     * @param difficulty the level of {@link Difficulty}.
     */
    public WaitingControllerImpl(final String hostName, final String playerName, final Difficulty difficulty) {
        waitingGUI = new SimpleWaitingGUI(this, hostName, playerName);
        this.hostName = hostName;
        this.difficulty = difficulty;
    }

    @Override
    public void create() {
        waitingGUI.create();
    }

    @Override
    public void updatePlayers(final List<String> playerNames) {
        waitingGUI.updatePlayers(playerNames);
    }

    @Override
    public void setWaitingRoomActorReference(final ActorRef ref) {
        this.waitingRoomActor = ref;
    }

    @Override
    public void dispose() {
        waitingGUI.dispose();
    }

    @Override
    public void comunicateStart() {
        waitingRoomActor.tell(new StartGameMsg(hostName, difficulty.toString()), ActorRef.noSender());
    }
}
