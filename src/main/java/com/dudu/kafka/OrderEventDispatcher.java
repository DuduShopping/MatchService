package com.dudu.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class OrderEventDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventDispatcher.class);
    private static final String ORDER_EVENT_NEW_ORDER = "orderNew";

    private BlockingQueue<KafkaEvent> kafkaEvents;
    private KafkaProducer<String, String> kafkaProducer;
    private Thread thread;
    private String orderTopic;

    public OrderEventDispatcher(@Value("${kafka.orders.server}") String server,
                                @Value("${kafka.orders.topic}") String orderTopic) {
        this.orderTopic = orderTopic;
        kafkaEvents = new LinkedBlockingDeque<>(10000);

        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", server);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        kafkaProducer = new KafkaProducer<>(props);
        thread = new Thread(new Worker());

        thread.start();
    }

    public void addToQueue(KafkaEvent kafkaEvent) {
        try {
            kafkaEvents.put(kafkaEvent);
        } catch (InterruptedException interruptedException) {
            logger.warn(interruptedException.toString());
        }
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    process(kafkaEvents.take());
                } catch (InterruptedException e) {
                    logger.warn("Taking a NewOrderEvent interrupted.", e);
                }
            }
        }

        private void process(KafkaEvent kafkaEvent) {
            if (kafkaEvent instanceof  NewOrderEvent)
                process((NewOrderEvent) kafkaEvent);
            else
                logger.warn("A kafka event not handled: " + kafkaEvent.getClass().getSimpleName());
        }

        private void process(NewOrderEvent newOrderEvent) {
            JSONObject payload = new JSONObject();
            payload.put("orderId", newOrderEvent.getShoppingOrder().getOrderId());
            payload.put("amount", newOrderEvent.getShoppingOrder().getOfferPrice());

            ProducerRecord<String, String> record = new ProducerRecord<>(orderTopic, ORDER_EVENT_NEW_ORDER, payload.toString());
            kafkaProducer.send(record);
        }
    }
}
