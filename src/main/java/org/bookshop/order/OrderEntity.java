package org.bookshop.order;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(name = "id")
    String id;

    String userid;

    String address;

    String state;

    @Enumerated(EnumType.STRING)
    Country country;

    String pincode;

    String mobilenum;

    String alt_mobile_num;

    double total_amt;

    @Enumerated(EnumType.STRING)
    PaymentType payment_mode;

    String payment_id;
    @Enumerated(EnumType.STRING)
    OrderStatus status;

    Timestamp createdDate;
    Timestamp updatedDate;

    public OrderEntity(
            String userid,
            DeliveryAddress deliveryAddress,
            double total_amt,
            PaymentType payment_mode,
            String payment_id
    ) {
        this.id = java.util.UUID.randomUUID().toString();
        this.userid = userid;
        this.address = deliveryAddress.address();
        this.state = deliveryAddress.state();
        this.country = deliveryAddress.country();
        this.pincode = deliveryAddress.pincode();
        this.mobilenum = deliveryAddress.mobileNumber();
        this.alt_mobile_num = deliveryAddress.alternateMobileNumber();
        this.total_amt = total_amt;
        this.payment_mode = payment_mode;
        this.payment_id = payment_id;
        this.status = OrderStatus.Created;
        this.createdDate = new Timestamp(System.currentTimeMillis());
        this.updatedDate = new Timestamp(System.currentTimeMillis());
    }

    public OrderEntity() {
    }

    public void setTotalAmount(double total_amt) {
        this.total_amt = total_amt;
    }

    public void setPaymentId(String payment_id) {
        this.payment_id = payment_id;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public void setUpdatedDate() {
        this.updatedDate = new Timestamp(System.currentTimeMillis());
    }
}
