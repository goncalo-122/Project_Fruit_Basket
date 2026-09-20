package org.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@ApplicationScoped
public class BasketProducer {

    @Inject
    @Channel("basket-events-out")
    Emitter<String> emitter;

    public void sendRemoveStockEvent(Long fruitId, int quantity) {

        JsonObject obj = Json.createObjectBuilder()
                .add("fruitId", fruitId)
                .add("quantity", quantity)
                .add("action", "REMOVE_STOCK")
                .build();

        emitter.send(obj.toString());
    }

    public void sendAddStockEvent(Long fruitId, int quantity) {

        JsonObject obj = Json.createObjectBuilder()
                .add("fruitId", fruitId)
                .add("quantity", quantity)
                .add("action", "ADD_STOCK")
                .build();

        emitter.send(obj.toString());
    }
}
