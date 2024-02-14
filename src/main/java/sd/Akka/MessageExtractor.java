package sd.Akka;

import akka.cluster.sharding.ShardRegion;
import sd.Akka.Messages.StandardMsg;
import sd.Utils.ClusterHelper;

public class MessageExtractor implements ShardRegion.MessageExtractor {

    /**
     * Define how to extract the identifier (the name of the actor) from the message.
     */
    @Override
    public String entityId(final Object message) {
        if (message instanceof StandardMsg) {
            return ((StandardMsg) message).getReceiver();
        }
        return null;
    }

    /**
     * Define how to extract the message.
     */
    @Override
    public Object entityMessage(final Object message) {
        return message;
    }

    /**
     * Define how to extract the id of the shard (a group of entities that will be managed together) based on the
     * message received.
     */
    @Override
    public String shardId(final Object message) {
        if (message instanceof StandardMsg) {
            int hash = ((StandardMsg) message).getReceiver().hashCode();
            int shardIndex = Math.abs(hash) % ClusterHelper.getNumberOfShards();
            return String.valueOf(shardIndex);
        }
        return null;
    }
}
