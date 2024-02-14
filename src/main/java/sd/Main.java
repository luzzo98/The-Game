package sd;

import akka.actor.ActorSystem;
import akka.cluster.Cluster;
import sd.Controller.InitialController;
import sd.Controller.InitialControllerImpl;
import sd.Utils.ClusterHelper;

public class Main {

    /**
     * The main of the project.
     *
     * @param args arguments.
     */
    public static void main(final String[] args) {
        ActorSystem system = ClusterHelper.startCluster();

        // after the node joined successfully the cluster
        Cluster.get(system).registerOnMemberUp(() -> {
            InitialController controller = new InitialControllerImpl(system);
            controller.createInitialGUI();
        });
    }
}
