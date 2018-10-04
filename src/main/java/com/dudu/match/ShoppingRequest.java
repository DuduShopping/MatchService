package com.dudu.match;

import com.dudu.database.DatabaseRow;

import java.util.Date;

public class ShoppingRequest {
    private long shoppingRequestId;
    private long shoppingOfferAccepted;
    private long userId;
    private String text;
    private Date createdAt;
    private String state;

    public static ShoppingRequest from(DatabaseRow row) {
        ShoppingRequest shoppingRequest = new ShoppingRequest();
        shoppingRequest.setShoppingRequestId(row.getLong("ShoppingRequestId"));
        shoppingRequest.setShoppingOfferAccepted(row.getLong("ShoppingOfferAccepted"));
        shoppingRequest.setUserId(row.getLong("UserId"));
        shoppingRequest.setText(row.getString("Text"));
        shoppingRequest.setCreatedAt(row.getDate("CreatedAt"));
        shoppingRequest.setState(row.getString("State"));

        return shoppingRequest;
    }

    public long getShoppingRequestId() {
        return shoppingRequestId;
    }

    public void setShoppingRequestId(long shoppingRequestId) {
        this.shoppingRequestId = shoppingRequestId;
    }

    public long getShoppingOfferAccepted() {
        return shoppingOfferAccepted;
    }

    public void setShoppingOfferAccepted(long shoppingOfferAccepted) {
        this.shoppingOfferAccepted = shoppingOfferAccepted;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ShoppingRequest{" +
                "shoppingRequestId=" + shoppingRequestId +
                ", shoppingOfferAccepted=" + shoppingOfferAccepted +
                ", userId=" + userId +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", state='" + state + '\'' +
                '}';
    }
}
