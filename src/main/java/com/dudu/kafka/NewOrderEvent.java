package com.dudu.kafka;

import com.dudu.match.ShoppingOrder;

public class NewOrderEvent extends KafkaEvent {
    private ShoppingOrder shoppingOrder;

    public ShoppingOrder getShoppingOrder() {
        return shoppingOrder;
    }

    public void setShoppingOrder(ShoppingOrder shoppingOrder) {
        this.shoppingOrder = shoppingOrder;
    }
}
