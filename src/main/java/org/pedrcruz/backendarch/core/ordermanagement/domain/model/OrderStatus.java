package org.pedrcruz.backendarch.core.ordermanagement.domain.model;

public enum OrderStatus {
    PENDING("Order is pending confirmation"),
    CONFIRMED("Order has been confirmed"),
    PREPARING("Order is being prepared"),
    READY("Order is ready for pickup/delivery"),
    DELIVERED("Order has been delivered"),
    CANCELLED("Order has been cancelled");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isConfirmed() {
        return this == CONFIRMED;
    }

    public boolean isPreparing() {
        return this == PREPARING;
    }

    public boolean isReady() {
        return this == READY;
    }

    public boolean isDelivered() {
        return this == DELIVERED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean isActive() {
        return this != CANCELLED && this != DELIVERED;
    }

    public boolean canBeModified() {
        return this == PENDING;
    }

    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }
}
