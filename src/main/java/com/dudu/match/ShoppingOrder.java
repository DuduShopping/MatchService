package com.dudu.match;

import com.dudu.database.DatabaseRow;

import java.util.Date;

public class ShoppingOrder {
    private long orderId;
    private long requestId;
    private String requestText;
    private Date requestCreatedAt;
    private long offerId;
    private String offerText;
    private long offerPrice;
    private Date offerCreatedAt;
    private Date createdAt;
    private String orderState;
    private String shipmentTrackingNumber;

    public static ShoppingOrder from(DatabaseRow row) {
        ShoppingOrder shoppingOrder = new ShoppingOrder();
        shoppingOrder.setOrderId(row.getLong("OrderId"));
        shoppingOrder.setRequestId(row.getLong("RequestId"));
        shoppingOrder.setRequestText(row.getString("RequestText"));
        shoppingOrder.setRequestCreatedAt(row.getDate("RequestCreatedAt"));
        shoppingOrder.setOfferId(row.getLong("OfferId"));
        shoppingOrder.setOfferText(row.getString("OfferText"));
        shoppingOrder.setOfferPrice(row.getLong("OfferPrice"));
        shoppingOrder.setOfferCreatedAt(row.getDate("CreatedAt"));
        shoppingOrder.setOrderState(row.getString("OrderState"));
        shoppingOrder.setShipmentTrackingNumber(row.getString("ShipmentTrackingNumber"));

        return shoppingOrder;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public Date getRequestCreatedAt() {
        return requestCreatedAt;
    }

    public void setRequestCreatedAt(Date requestCreatedAt) {
        this.requestCreatedAt = requestCreatedAt;
    }

    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public long getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(long offerPrice) {
        this.offerPrice = offerPrice;
    }

    public Date getOfferCreatedAt() {
        return offerCreatedAt;
    }

    public void setOfferCreatedAt(Date offerCreatedAt) {
        this.offerCreatedAt = offerCreatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getShipmentTrackingNumber() {
        return shipmentTrackingNumber;
    }

    public void setShipmentTrackingNumber(String shipmentTrackingNumber) {
        this.shipmentTrackingNumber = shipmentTrackingNumber;
    }

    @Override
    public String toString() {
        return "ShoppingOrder{" +
                "orderId=" + orderId +
                ", requestId=" + requestId +
                ", requestText='" + requestText + '\'' +
                ", requestCreatedAt=" + requestCreatedAt +
                ", offerId=" + offerId +
                ", offerText='" + offerText + '\'' +
                ", offerPrice=" + offerPrice +
                ", offerCreatedAt=" + offerCreatedAt +
                ", createdAt=" + createdAt +
                ", orderState='" + orderState + '\'' +
                ", shipmentTrackingNumber='" + shipmentTrackingNumber + '\'' +
                '}';
    }
}
