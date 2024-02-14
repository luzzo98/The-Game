package sd.Akka.Actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberJoined;
import akka.cluster.ClusterEvent.MemberLeft;
import akka.cluster.ClusterEvent.MemberExited;
import akka.cluster.Member;

/**
 * An actor used to print the log of the event happened in the cluster.
 */
public class ClusterListener extends AbstractLoggingActor {

    private final Cluster cluster = Cluster.get(getContext().getSystem());

    /**
     * Creates a Props configuration for the ClusterListener actor. This method is used to define the properties and
     * configuration for creating instances of the ClusterListener actor.
     *
     * @return A {@link Props} object configured for the ClusterListener actor.
     */
    public static Props props() {
        return Props.create(ClusterListener.class, ClusterListener::new);
    }

    @Override
    public void preStart() {
        // subscribe to the cluster's event (change notifications of the cluster membership)
        cluster.subscribe(
                getSelf(),
                ClusterEvent.initialStateAsEvents(), // to avoid the CurrentClusterState msg at the beginning
                MemberEvent.class,
                UnreachableMember.class
        );
    }

    @Override
    public void postStop() {
        // unsubscribe to the cluster's event when is stopped
        cluster.unsubscribe(getSelf());
    }

    @Override
    public Receive createReceive() {
        // define how to react to the classic ClusterEvents
        return receiveBuilder()
                .match(MemberUp.class, m -> logEvent("Member is UP: {}", m.member()))
                .match(MemberRemoved.class, m -> logEvent("Member is REMOVED: {}", m.member()))
                .match(UnreachableMember.class, m -> logEvent("Member detected as UNREACHABLE: {}", m.member()))
                .match(MemberJoined.class, m -> logEvent("Member JOINED: {}", m.member()))
                .match(MemberLeft.class, m -> logEvent("Member LEFT: {}", m.member()))
                .match(MemberExited.class, m -> logEvent("Member EXITED: {}", m.member()))
                .matchAny(message -> System.out.println("# UNEXPECTED MESSAGE TO Listener: " + message))
                .build();
    }

    /**
     * Log of an event.
     *
     * @param message the message to insert in the log.
     * @param member the member of the cluster to which the log refers.
     */
    private void logEvent(final String message, final Member member) {
        log().info(message, member.address());
    }
}
