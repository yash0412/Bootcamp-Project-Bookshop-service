package org.bookshop.order;

public enum PaymentType {
    COD("COD");

    private final String paymentType;


    PaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
