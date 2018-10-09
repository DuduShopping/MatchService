package com.dudu.rest;

import com.dudu.match.ShoppingOrder;
import com.dudu.match.ShoppingOrderService;
import com.dudu.oauth.OAuthFilter;
import com.dudu.oauth.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;

@RestController
public class ShoppingOrderController {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingOrderController.class);
    private ShoppingOrderService shoppingOrderService;

    public ShoppingOrderController(ShoppingOrderService shoppingOrderService) {
        this.shoppingOrderService = shoppingOrderService;
    }

    /**
     *
     * @param user
     * @param order {shoppingOfferId: , shoppingRequestId: }
     * @return
     */
    @PostMapping("/shoppingOrder")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingOrder createOrder(@RequestAttribute(OAuthFilter.USER) User user,
                                     ShoppingOrder order) {
        try {
            return shoppingOrderService.createOrder(user.getUserId(), order.getOfferId(), order.getRequestId());
        } catch (Exception e) {
            logger.warn("Failed to create an order: ", e);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/shoppingOrder")
    @ResponseStatus(HttpStatus.OK)
    public List<ShoppingOrder> searchOrders(@RequestAttribute(OAuthFilter.USER) User user,
                                            @RequestParam(value = "shoppingOrderId") long shoppingOrderId,
                                            @RequestParam(value = "shoppingRequestId") long shoppingRequestId,
                                            @RequestParam(value = "shoppingOfferId") long shoppingOfferId,
                                            @RequestParam(value = "states") String states) {
        try {
            List<String> stateList = null;
            if (states != null)
                stateList = Arrays.asList(states.split(","));

            return shoppingOrderService.searchOrders(user.getUserId(), shoppingOrderId, shoppingRequestId, shoppingOfferId, stateList);
        } catch (Exception e) {
            logger.warn("Failed to search orders: ", e);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }


}
