package org.varukha.deliveryservice.model.enums;

import lombok.Getter;

/**
 * Enumeration representing delivery status of cargo.
 */
@Getter
public enum DeliveryStatus {
    IN_TRANSIT("In Transit"),
    DELIVERED("Delivered"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    PENDING("Pending"),
    RETURNED("Returned"),
    LOST("Lost");

    private final String statusName;

    DeliveryStatus(String statusName) {
        this.statusName = statusName;
    }

    public static DeliveryStatus convertToDeliveryStatus(String status) {
        for (DeliveryStatus deliveryStatus : DeliveryStatus.values()) {
            if (deliveryStatus.getStatusName().equals(status)) {
                return deliveryStatus;
            }
        }
        throw new IllegalArgumentException("Invalid delivery status: " + status);
    }

}
