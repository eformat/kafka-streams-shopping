package org.acme;

import org.acme.data.Purchase;
import org.acme.data.Reward;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Objects;

/*
   Accumulate reward points
 */
public class RewardTransformer implements ValueTransformer<Purchase, Reward> {

    private KeyValueStore<String, Integer> stateStore;
    private String storeName;
    private ProcessorContext context;

    public RewardTransformer(String storeName) {
        Objects.requireNonNull(storeName,"Store Name can't be null");
        this.storeName = storeName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        stateStore = (KeyValueStore) this.context.getStateStore(storeName);
    }

    @Override
    public void close() {
        // no-op
    }

    @Override
    public Reward transform(Purchase purchase) {
        Reward reward = Reward.builder(purchase).build();
        Integer accumulatedSoFar = stateStore.get(reward.getCustomerId());
        if (accumulatedSoFar != null) {
            reward.addRewardPoints(accumulatedSoFar);
        }
        stateStore.put(reward.getCustomerId(), reward.getTotalPoints());
        return reward;
    }
}
