package org.bookshop.order;

public record DeliveryAddress(String address, String state, String pincode, Country country, String mobileNumber,
                              String alternateMobileNumber) {
}
