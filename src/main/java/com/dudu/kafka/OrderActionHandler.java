package com.dudu.kafka;

import com.dudu.database.DatabaseHelper;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class OrderActionHandler implements ActionHandler {
    public static final String ORDER_PAID = "orderPaid";
    public static final String ORDER_STATE_CREATED = "RD1";
    public static final String ORDER_STATE_PAID = "RD5";
    private static final Logger logger = LoggerFactory.getLogger(OrderActionHandler.class);
    private static final DatabaseHelper databaseHelper = DatabaseHelper.getHelper();


    private DataSource source;

    public OrderActionHandler(DataSource source) {
        this.source = source;
    }

    @Override
    public void handle(String actionType, String payload) throws Exception {
        if (actionType == null)
            return;

        switch (actionType) {
            case ORDER_PAID:
                onOrderPaid(payload);
                break;

            default:
                logger.warn("action not handled: " + actionType);
        }
    }

    public void onOrderPaid(String payload) throws SQLException {
        JSONObject data = new JSONObject(new JSONTokener(payload));
        long orderId = data.getLong("orderId");

        try (Connection con = source.getConnection()) {
            String update = "UPDATE Orders SET OrderState = ? WHERE OrderId = ? AND OrderState = ? ";
            int count = databaseHelper.update(con, update, ORDER_STATE_PAID, orderId, ORDER_STATE_CREATED);
            if (count != 1)
                throw new SQLException("Expected a order updated");
        }
    }
}
