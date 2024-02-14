package sd.Controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import sd.Akka.Messages.AddPlayerMsg;
import sd.Akka.Messages.CreateWaitingGUIMsg;
import sd.Utils.ClusterHelper;
import sd.View.InitialGUI;
import sd.View.SimpleInitialGUI;

/**
 * Class with the implementation of the {@link InitialController} interface.
 */
public class InitialControllerImpl implements InitialController {

    private final InitialGUI gui;
    private final ActorSystem system;

    /**
     * Create the {@link InitialGUI}.
     *
     * @param system the {@link ActorSystem} that coordinate the actors and manage the environment.
     */
    public InitialControllerImpl(final ActorSystem system) {
        this.system = system;
        gui = new SimpleInitialGUI(this);
    }

    @Override
    public void createInitialGUI() {
        gui.create();
    }

    @Override
    public void createPlayerAndWaitingActors(final String playerName, final String hostName, final String difficulty) {
        ActorRef player = ClusterHelper.createPlayer(playerName, system);
        player.tell(new CreateWaitingGUIMsg(playerName, hostName, difficulty), player);
        ActorRef waitingRoomRegion = ClusterHelper.getWaitingRegion(system);
        waitingRoomRegion.tell(new AddPlayerMsg(hostName, playerName), player);
    }
}
