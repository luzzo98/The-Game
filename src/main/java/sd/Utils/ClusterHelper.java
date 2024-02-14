package sd.Utils;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import sd.Akka.Actor.ClusterListener;
import sd.Akka.Actor.GameStateActor;
import sd.Akka.Actor.PlayerActor;
import sd.Akka.MessageExtractor;
import sd.Akka.Actor.WaitingRoomActor;

import java.io.IOException;
import java.net.Socket;

/**
 * Utility class with the main configuration and function of the akka cluster; contains only static methods because has
 * to be used also from the actors in the distributed environment.
 */
public class ClusterHelper {

    private static final int DEFAULT_PORT = 2551;
    private static final int RANDOM_PORT = 0;
    private static final int NUMBER_OF_SHARDS = 10;

    /**
     * Create the akka cluster with the configuration present in the file "application.conf" and the specified port.
     * Create also an actor to print the log of the cluster.
     *
     * @return the {@link ActorSystem} that coordinate the actors and manage the environment.
     */
    public static ActorSystem startCluster() {
        Config config = ConfigFactory.load(); // load the file "src/main/resources/application.conf"
        String path = "akka.remote.artery.canonical.port";
        int port = isDefaultPortAvailable() ? DEFAULT_PORT : RANDOM_PORT;
        ActorSystem system =
                ActorSystem.create("ClusterSystem", config.withValue(path, ConfigValueFactory.fromAnyRef(port)));
        system.actorOf(ClusterListener.props(), "clusterListener");
        return system;
    }

    /**
     * Static method to create a new {@link PlayerActor}.
     *
     * @param playerName the name of the player.
     * @param system the {@link ActorSystem} that coordinate the actors and manage the environment.
     * @return the {@link ActorRef} of the player.
     */
    public static ActorRef createPlayer(final String playerName, final ActorSystem system) {
        return system.actorOf(PlayerActor.props(playerName), playerName);
    }

    /**
     * Start the akka sharding region for {@link WaitingRoomActor} used to find a game already created by a friend.
     *
     * @param system the {@link ActorSystem} that coordinate the actors and manage the environment.
     * @return the {@link ActorRef} of the sharding region with the waiting rooms.
     */
    public static ActorRef getWaitingRegion(final ActorSystem system) {
        return ClusterSharding.get(system).start(
                "Waiting",
                WaitingRoomActor.props(),
                ClusterShardingSettings.create(system),
                new MessageExtractor()
        );
    }

    /**
     * Static method to start the akka sharding region for {@link GameStateActor} used to find the
     * {@link sd.Model.GameState} of the match.
     *
     * @param system the {@link ActorSystem} that {@link WaitingRoomActor} takes from the context.
     * @return the {@link ActorRef} of the sharding region with the game states.
     */
    public static ActorRef getGameStateRegion(final ActorSystem system) {
        return ClusterSharding.get(system).start(
                "GameState",
                GameStateActor.props(),
                ClusterShardingSettings.create(system),
                new MessageExtractor()
        );
    }

    /**
     * Static method to get the extension used to find a {@link WaitingRoomActor}; it has to be added to the name of
     * the player who created the match.
     *
     * @return a {@link String} with the extension.
     */
    public static String getWaitingRoomExtension() {
        return "-WaitingRoom";
    }

    /**
     * Static method to get the extension used to find a {@link GameStateActor}; it has to be added to the name of
     * the player who created the match.
     *
     * @return a {@link String} with the extension.
     */
    public static String getGameStateExtension() {
        return "-GameState";
    }

    /**
     * Static method to get the number of shards of the cluster. It should be based on the expected workload, the
     * cluster size, the memory and the processing capacity of the shards.
     *
     * @return the number of shards of the system.
     */
    public static int getNumberOfShards() {
        return NUMBER_OF_SHARDS;
    }

    /**
     * Checks to see if a specific port is available.
     *
     * @return true if the DEFAULT_PORT is available, false otherwise.
     */
    private static boolean isDefaultPortAvailable() {
        try (Socket ignored = new Socket("localhost", ClusterHelper.DEFAULT_PORT)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
