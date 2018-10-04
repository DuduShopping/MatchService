package com.dudu.match;

import com.dudu.database.DatabaseConfiguration;
import com.dudu.kafka.OrderEventDispatcher;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

@Ignore
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class, OrderEventDispatcher.class})
@TestPropertySource(value = "/shoppingOrderServiceTest.properties")
public class ShoppingOrderServiceTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    OrderEventDispatcher orderEventDispatcher;

    @Test
    public void createOrder() throws SQLException {
        ShoppingOrderService shoppingOrderService = new ShoppingOrderService(dataSource, orderEventDispatcher);

        long userId = 1;
        long shoppingOfferId = 5;
        long shoppingRequestId = 1;

        ShoppingOrder shoppingOrder = shoppingOrderService.createOrder(userId, shoppingOfferId, shoppingRequestId);
        System.out.println(shoppingOrder);

        while (true) {}
    }


}
