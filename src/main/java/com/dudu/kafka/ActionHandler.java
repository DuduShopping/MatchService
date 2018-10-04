package com.dudu.kafka;

public interface ActionHandler {
    void handle(String actionType, String data) throws Exception;
}
