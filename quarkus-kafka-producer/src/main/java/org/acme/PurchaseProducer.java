package org.acme;

import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.vertx.core.json.JsonObject;
import org.acme.data.Purchase;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Path("/")
@ApplicationScoped
public class PurchaseProducer {

    @Inject
    @Channel("purchases-emit-out")
    Emitter<Purchase> purchases;

    private static final Map<Integer, String> CUSTOMERS;
    private static final Map<Integer, String> ITEMS;
    private static final Map<Integer, String> CARDS;

    static {
        CUSTOMERS = new HashMap<Integer, String>();
        CUSTOMERS.put(1, "mike");
        CUSTOMERS.put(2, "joe");
        CUSTOMERS.put(3, "sarah");
    }

    static {
        ITEMS = new HashMap<Integer, String>();
        ITEMS.put(1, "late");
        ITEMS.put(2, "cappuccino");
        ITEMS.put(3, "flat white");
        ITEMS.put(4, "long black");
    }

    static {
        CARDS = new HashMap<Integer, String>();
        CARDS.put(1, "4111-1111-1111-1111");
        CARDS.put(2, "5105-1051-0510-5100");
    }

    @GET
    @Path("/buy")
    public Response generatePurchase() {
        BigDecimal price = new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 10)).setScale(2, RoundingMode.HALF_UP);
        Purchase purchase = new Purchase(
                CUSTOMERS.get(ThreadLocalRandom.current().nextInt(1, CUSTOMERS.size() + 1)),
                CARDS.get(ThreadLocalRandom.current().nextInt(1, CARDS.size() + 1)),
                ITEMS.get(ThreadLocalRandom.current().nextInt(1, ITEMS.size() + 1)),
                ThreadLocalRandom.current().nextInt(1, 10),
                price.doubleValue());
        purchases.send(KafkaRecord.of(purchase.getPurchaseKey(), purchase));
        JsonObject response = JsonObject.mapFrom(purchase);
        return Response.status(201).entity(response).build();
    }

}
