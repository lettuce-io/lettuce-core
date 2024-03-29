package io.lettuce.core.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;

/**
 * Implementations for {@link PartitionsConsensus}.
 *
 * @author Mark Paluch
 * @since 4.2
 */
class PartitionsConsensusImpl {

    /**
     * Votes for {@link Partitions} that contains the most known (previously existing) nodes.
     */
    static final class KnownMajority extends PartitionsConsensus {

        @Override
        Partitions getPartitions(Partitions current, Map<RedisURI, Partitions> topologyViews) {

            if (topologyViews.isEmpty()) {
                return current;
            }

            List<VotedPartitions> votedList = new ArrayList<>();

            for (Partitions partitions : topologyViews.values()) {

                int knownNodes = 0;
                for (RedisClusterNode knownNode : current) {

                    if (partitions.getPartitionByNodeId(knownNode.getNodeId()) != null) {
                        knownNodes++;
                    }
                }

                votedList.add(new VotedPartitions(knownNodes, partitions));
            }

            Collections.shuffle(votedList);
            Collections.sort(votedList, (o1, o2) -> Integer.compare(o2.votes, o1.votes));

            return votedList.get(0).partitions;
        }

    }

    /**
     * Votes for {@link Partitions} that contains the most active (in total) nodes.
     */
    static final class HealthyMajority extends PartitionsConsensus {

        @Override
        Partitions getPartitions(Partitions current, Map<RedisURI, Partitions> topologyViews) {

            if (topologyViews.isEmpty()) {
                return current;
            }

            List<VotedPartitions> votedList = new ArrayList<>();

            for (Partitions partitions : topologyViews.values()) {

                int votes = 0;

                for (RedisClusterNode node : partitions) {

                    if (node.is(RedisClusterNode.NodeFlag.FAIL) || node.is(RedisClusterNode.NodeFlag.EVENTUAL_FAIL)
                            || node.is(RedisClusterNode.NodeFlag.NOADDR)) {
                        continue;
                    }

                    votes++;

                }

                votedList.add(new VotedPartitions(votes, partitions));
            }

            Collections.shuffle(votedList);
            Collections.sort(votedList, (o1, o2) -> Integer.compare(o2.votes, o1.votes));

            return votedList.get(0).partitions;
        }

    }

    static final class VotedPartitions {

        final int votes;

        final Partitions partitions;

        public VotedPartitions(int votes, Partitions partitions) {
            this.votes = votes;
            this.partitions = partitions;
        }

    }

}
