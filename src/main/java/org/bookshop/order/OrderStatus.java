package org.bookshop.order;

public enum OrderStatus {

    Created("Created"),
    Pending("Pending"),
    Completed("Completed"),
    Failed("Failed");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
