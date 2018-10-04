package com.dudu.match;

import com.dudu.database.DatabaseConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;


@Ignore
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@TestPropertySource(value = "/database.properties")
public class ShoppingRequestServiceTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void createRequest() throws Exception {
        ShoppingRequestService shoppingRequestService = new ShoppingRequestService(dataSource);

        long userId = 1;
        String description = "I want a pair of shoes";
        ShoppingRequest shoppingRequest = shoppingRequestService.createRequest(userId, description);
        System.out.println(shoppingRequest.toString());
    }

    @Test
    public void getRequest() throws Exception {
        ShoppingRequestService shoppingRequestService = new ShoppingRequestService(dataSource);

        long userId = 1;
        long shoppingRequestId = 2;
        ShoppingRequest shoppingRequest = shoppingRequestService.getRequest(userId, shoppingRequestId);
        System.out.println(shoppingRequest);
    }

    @Test
    public void cancelRequest() throws Exception {
        ShoppingRequestService shoppingRequestService = new ShoppingRequestService(dataSource);

        long userId = 1;
        long shoppingRequestId = 2;
        shoppingRequestService.cancelRequest(userId, shoppingRequestId);
    }

    @Test
    public void acceptRequest() throws Exception {
        ShoppingRequestService shoppingRequestService = new ShoppingRequestService(dataSource);

        long userId = 1;
        long shoppingRequestId = 2;
        long shoppingOfferId = 1;
        shoppingRequestService.acceptRequest(userId, shoppingRequestId, shoppingOfferId);
    }

}
