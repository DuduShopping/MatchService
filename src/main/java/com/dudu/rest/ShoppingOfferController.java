package com.dudu.rest;

import com.dudu.match.ShoppingOffer;
import com.dudu.match.ShoppingOfferService;
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
public class ShoppingOfferController {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingOfferController.class);
    private ShoppingOfferService shoppingOfferService;

    public ShoppingOfferController(ShoppingOfferService shoppingOfferService) {
        this.shoppingOfferService = shoppingOfferService;
    }

    /**
     *
     * @param user
     * @param shoppingOffer {shoppingRequestId: required, text: required, price: required}
     * @return
     */
    @PostMapping("/shoppingOffer")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingOffer createOffer(@RequestAttribute(OAuthFilter.USER) User user,
                                     ShoppingOffer shoppingOffer) {
        try {
            return shoppingOfferService.createOffer(user.getUserId(), shoppingOffer.getShoppingRequestId(),
                    shoppingOffer.getText(), shoppingOffer.getPrice());
        } catch (Exception e) {
            logger.warn("Failed to create an offer: ", e);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/shoppingOffer")
    @ResponseStatus(HttpStatus.OK)
    public List<ShoppingOffer> searchOffers(@RequestAttribute(OAuthFilter.USER) User user,
                                            @RequestParam(value = "shoppingOfferId") long shoppingOfferId,
                                            @RequestParam(value = "shoppingRequestId") long shoppingRequestId,
                                            @RequestParam(value = "states") String states) {
        try {
            return shoppingOfferService.searchOffers(user.getUserId(), shoppingOfferId, shoppingRequestId, Arrays.asList(states.split(",")));
        } catch (Exception e) {
            logger.warn("Failed to search offers: ", e);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * @param user
     * @param shoppingOffer {shoppingOfferId: , state: }
     */
    @PutMapping("/shoppingOffer")
    @ResponseStatus(HttpStatus.OK)
    public void updateOffer(@RequestAttribute(OAuthFilter.USER) User user, ShoppingOffer shoppingOffer) {
        String state = shoppingOffer.getState();
        long shoppingOfferId = shoppingOffer.getShoppingOfferId();
        try {
            if (state.equals(ShoppingOfferService.SHOPPING_OFFER_STATE_REJECTED))
                shoppingOfferService.rejectOffer(user.getUserId(), shoppingOfferId);
            else if (state.equals(ShoppingOfferService.SHOPPING_OFFER_STATE_PULLED))
                shoppingOfferService.pullOffer(user.getUserId(), shoppingOfferId);
            else
                throw new IllegalArgumentException("Unable to update to state: " + state);
        } catch (Exception e) {
            logger.warn("Failed to update state: ", e);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

}
