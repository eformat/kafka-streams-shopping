package org.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import org.acme.data.Purchase;
import org.acme.data.PurchaseKey;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.Path;

@Path("/")
@ApplicationScoped
public class KafkaStreaming {

    private final String PURCHASES = "purchases";
    private final String MASKED = "masked";
    private final String STORAGE = "storage";

    @Produces
    public Topology startPurchasingTopology() {
        final StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<PurchaseKey> purchaseKeySerde = new ObjectMapperSerde<>(PurchaseKey.class);
        ObjectMapperSerde<Purchase> purchaseSerde = new ObjectMapperSerde<>(Purchase.class);

        builder.stream(PURCHASES, Consumed.with(purchaseKeySerde, purchaseSerde))
                .to(STORAGE, Produced.with(purchaseKeySerde, purchaseSerde));

//        builder.stream(PURCHASES, Consumed.with(purchaseKeySerde, purchaseSerde))
//                .mapValues(Purchase::getItem)
//                .to(STORAGE, Produced.with(purchaseKeySerde, Serdes.String()));

//        builder.stream(PURCHASES, Consumed.with(purchaseKeySerde, purchaseSerde))
//                .mapValues(p -> p.getItem().toUpperCase())
//                .to(MASKED, Produced.with(purchaseKeySerde, Serdes.String()));

        builder.stream(PURCHASES, Consumed.with(purchaseKeySerde, purchaseSerde))
                .mapValues(p -> p.builder(p).build())
                .to(MASKED, Produced.with(purchaseKeySerde, purchaseSerde));

        // build the streams topology
        return builder.build();
    }

}
