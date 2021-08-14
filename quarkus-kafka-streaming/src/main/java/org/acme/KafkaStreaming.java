package org.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import org.acme.data.Purchase;
import org.acme.data.PurchaseKey;
import org.acme.data.Reward;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.Path;

@Path("/")
@ApplicationScoped
public class KafkaStreaming {

    private final String PURCHASES = "purchases";
    private final String MASKED = "masked";
    private final String REWARDS = "rewards";
    private final String BIGSPENDERS = "bigspenders";
    private final String NORTH = "north";
    private final String SOUTH = "south";
    private final String STORAGE = "storage";

    @Produces
    public Topology startPurchasingTopology() {
        final StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<PurchaseKey> purchaseKeySerde = new ObjectMapperSerde<>(PurchaseKey.class);
        ObjectMapperSerde<Purchase> purchaseSerde = new ObjectMapperSerde<>(Purchase.class);
        ObjectMapperSerde<Reward> rewardSerde = new ObjectMapperSerde<>(Reward.class);

        // all purchases are stored
        builder.stream(PURCHASES, Consumed.with(purchaseKeySerde, purchaseSerde))
                .to(STORAGE, Produced.with(purchaseKeySerde, purchaseSerde));

//        builder.stream(PURCHASES, Consumed.with(purchaseKeySerde, purchaseSerde))
//                .mapValues(Purchase::getItem)
//                .to(STORAGE, Produced.with(purchaseKeySerde, Serdes.String()));

//        builder.stream(PURCHASES, Consumed.with(purchaseKeySerde, purchaseSerde))
//                .mapValues(p -> p.getItem().toUpperCase())
//                .to(MASKED, Produced.with(purchaseKeySerde, Serdes.String()));

        // mask pii
        builder.stream(PURCHASES, Consumed.with(purchaseKeySerde, purchaseSerde))
                .mapValues(p -> p.builder(p).build())
                .to(MASKED, Produced.with(purchaseKeySerde, purchaseSerde));

        // rewards
        builder.stream(MASKED, Consumed.with(purchaseKeySerde, purchaseSerde))
                .mapValues(p -> new Reward(p))
                .to(REWARDS, Produced.with(purchaseKeySerde, rewardSerde));

        // bigspenders
        KeyValueMapper<PurchaseKey, Purchase, Long> purchaseDateAsKey = (purchaseKey, purchase) -> purchase.getPurchaseKey().getTransactionDate().getTime();
        builder.stream(MASKED, Consumed.with(purchaseKeySerde, purchaseSerde))
                .filter((purchaseKey, purchase) -> purchase.getPrice() > 5.00)
                .selectKey(purchaseDateAsKey)
                .to(BIGSPENDERS, Produced.with(Serdes.Long(), purchaseSerde));

        // branch split for north and south regions
        builder.stream(MASKED, Consumed.with(purchaseKeySerde, purchaseSerde))
                .split()
                .branch((purchaseKey, purchase) -> purchase.getStore().equalsIgnoreCase("syd")
                        || purchase.getStore().equalsIgnoreCase("bne"), Branched.withConsumer(ks -> ks.to(NORTH)))
                .branch((purchaseKey, purchase) -> purchase.getStore().equalsIgnoreCase("mlb")
                        || purchase.getStore().equalsIgnoreCase("ade")
                        || purchase.getStore().equalsIgnoreCase("pth"), Branched.withConsumer(ks -> ks.to(SOUTH)));

        // build the streams topology
        return builder.build();
    }

}
