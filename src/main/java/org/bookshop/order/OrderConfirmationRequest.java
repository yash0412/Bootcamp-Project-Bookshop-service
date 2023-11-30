package org.bookshop.order;

public record OrderConfirmationRequest(DeliveryAddress deliveryAddress, PaymentType paymentType) {
}
