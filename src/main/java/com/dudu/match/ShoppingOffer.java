package com.dudu.match;

import com.dudu.database.DatabaseRow;

import java.util.Date;

public class ShoppingOffer {
    private long userId;
    private long shoppingOfferId;
    private long shoppingRequestId;
    private String text;
    private long price;
    private String state;
    private Date createdAt;

    public static ShoppingOffer from(DatabaseRow databaseRow) {
        ShoppingOffer shoppingOffer = new ShoppingOffer();
        shoppingOffer.setUserId(databaseRow.getLong("UserId"));
        shoppingOffer.setShoppingOfferId(databaseRow.getLong("ShoppingOfferId"));
        shoppingOffer.setShoppingRequestId(databaseRow.getLong("ShoppingRequestId"));
        shoppingOffer.setText(databaseRow.getString("Text"));
        shoppingOffer.setPrice(databaseRow.getLong("Price"));
        shoppingOffer.setState(databaseRow.getString("State"));
        shoppingOffer.setCreatedAt(databaseRow.getDate("CreatedAt"));

        return shoppingOffer;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getShoppingOfferId() {
        return shoppingOfferId;
    }

    public void setShoppingOfferId(long shoppingOfferId) {
        this.shoppingOfferId = shoppingOfferId;
    }

    public long getShoppingRequestId() {
        return shoppingRequestId;
    }

    public void setShoppingRequestId(long shoppingRequestId) {
        this.shoppingRequestId = shoppingRequestId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ShoppingOffer{" +
                "userId=" + userId +
                ", shoppingOfferId=" + shoppingOfferId +
                ", shoppingRequestId=" + shoppingRequestId +
                ", text='" + text + '\'' +
                ", price=" + price +
                ", state='" + state + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
