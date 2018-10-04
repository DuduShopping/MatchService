package com.dudu.match;

import com.dudu.database.DatabaseHelper;
import com.dudu.database.DatabaseResult;
import com.dudu.database.DatabaseRow;
import com.dudu.database.StoredProcedure;
import com.dudu.kafka.NewOrderEvent;
import com.dudu.kafka.OrderEventDispatcher;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class ShoppingOrderService {
    private static final DatabaseHelper databaseHelper = DatabaseHelper.getHelper();
    private DataSource source;
    private OrderEventDispatcher orderEventDispatcher;

    public ShoppingOrderService(DataSource source, OrderEventDispatcher orderEventDispatcher) {
        this.source = source;
        this.orderEventDispatcher = orderEventDispatcher;
    }

    public ShoppingOrder createOrder(long userId, long shoppingOfferId, long shoppingRequestId) throws SQLException {
        try (Connection con = source.getConnection()) {
            StoredProcedure sp = new StoredProcedure(con, "sp_CreateOrder");
            sp.addParameter("UserId", userId);
            sp.addParameter("ShoppingOfferId", shoppingOfferId);
            sp.addParameter("ShoppingRequestId", shoppingRequestId);

            DatabaseResult databaseResult = sp.query();
            DatabaseRow databaseRow = databaseResult.get(0);

            int error = databaseRow.getInt("Error", -1);
            if (error != 0)
                throw new SQLException("Something is wrong when creating an order: error = " + error);

            long orderId = databaseRow.getLong("OrderId");

            ShoppingOrder shoppingOrder = getOrder(userId, orderId);
            NewOrderEvent newOrderEvent = new NewOrderEvent();
            newOrderEvent.setShoppingOrder(shoppingOrder);

            orderEventDispatcher.addToQueue(newOrderEvent);

            return shoppingOrder;
        }
    }

    public ShoppingOrder getOrder(long userId, long orderId) throws SQLException {
        try (Connection con = source.getConnection()) {
            String select = "SELECT * FROM ShoppingOrders WHERE UserId = ? AND OrderId = ?";
            DatabaseResult databaseResult = databaseHelper.query(con, select, userId, orderId);
            if (databaseResult.isEmpty())
                throw new SQLException("Order with userId=" + userId + ", orderId=" + orderId + " is not found");

            return ShoppingOrder.from(databaseResult.get(0));
        }
    }


}
