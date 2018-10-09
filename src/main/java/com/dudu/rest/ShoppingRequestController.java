package com.dudu.rest;

import com.dudu.match.ShoppingRequest;
import com.dudu.match.ShoppingRequestService;
import com.dudu.oauth.OAuthFilter;
import com.dudu.oauth.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@RestController
public class ShoppingRequestController {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingRequestController.class);
    private ShoppingRequestService shoppingRequestService;

    public ShoppingRequestController(ShoppingRequestService shoppingRequestService) {
        this.shoppingRequestService = shoppingRequestService;
    }

    /**
     *
     * @param user
     * @param shoppingRequest {text: required}
     * @return
     */
    @PostMapping(value = "/shoppingRequest")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingRequest createRequest(@RequestAttribute(OAuthFilter.USER) User user,
                                         ShoppingRequest shoppingRequest) {
        try {
            shoppingRequest = shoppingRequestService.createRequest(user.getUserId(), shoppingRequest.getText());
            return shoppingRequest;
        } catch (SQLException e) {
            logger.warn("Failed to create a request: ", e);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * @param user
     * @param requestId
     * @param states csv format
     * @return
     */
    @GetMapping(value = "/shoppingRequest")
    @ResponseStatus(HttpStatus.OK)
    public List<ShoppingRequest> searchRequests(@RequestAttribute(OAuthFilter.USER) User user,
                                                @RequestParam(value = "requestId") long requestId,
                                                @RequestParam(value = "states") String states) {
        try {
            List<String> stateList = states == null ? null : Arrays.asList(states.split(","));
            return shoppingRequestService.searchRequests(user.getUserId(), requestId, stateList);
        } catch (SQLException e) {
            logger.warn("Failed to search request: ", e);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/shoppingRequest")
    @ResponseStatus(HttpStatus.OK)
    public void updateRequestState(@RequestAttribute(OAuthFilter.USER) User user,
                                   ShoppingRequest shoppingRequest) {
        try {
            long shoppingRequestId = shoppingRequest.getShoppingRequestId();
            String newRequestState = shoppingRequest.getState();
            long shoppingOfferAccepted = shoppingRequest.getShoppingOfferAccepted();
            switch (newRequestState) {
                case ShoppingRequestService.SHOPPING_REQUEST_STATE_ACCEPTED:
                    shoppingRequestService.acceptRequest(user.getUserId(), shoppingRequestId, shoppingOfferAccepted);
                    return;

                case ShoppingRequestService.SHOPPING_REQUEST_STATE_CANCELED:
                    shoppingRequestService.cancelRequest(user.getUserId(), shoppingRequestId);
                    return;

                default:
                    throw new IllegalArgumentException("Unexpected request state: " + newRequestState);
            }
        } catch (Exception e) {
            logger.warn("Failed to update request state.");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

}
