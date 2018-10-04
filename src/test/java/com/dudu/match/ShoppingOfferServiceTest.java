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
import java.sql.SQLException;

@Ignore
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@TestPropertySource(value = "/database.properties")
public class ShoppingOfferServiceTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void createOffer() throws SQLException {
        ShoppingOfferService shoppingOfferService = new ShoppingOfferService(dataSource);
        long userId = 1;
        long shoppingRequestId = 1;
        String description = "I have cloth";
        long price = 5000;

        ShoppingOffer shoppingOffer = shoppingOfferService.createOffer(userId, shoppingRequestId, description, price);
        System.out.println(shoppingOffer.toString());
    }

    @Test
    public void rejectOffer() throws SQLException {
        ShoppingOfferService shoppingOfferService = new ShoppingOfferService(dataSource);
        long userId = 1;
        long shoppingOfferId = 5;

        shoppingOfferService.rejectOffer(userId, shoppingOfferId);
    }

    @Test
    public void pullOffer() throws SQLException {
        ShoppingOfferService shoppingOfferService = new ShoppingOfferService(dataSource);
        long userId = 1;
        long shoppingOfferId = 5;

        shoppingOfferService.pullOffer(userId, shoppingOfferId);
    }

}
