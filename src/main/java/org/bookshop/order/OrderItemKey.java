package org.bookshop.order;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemKey implements Serializable {

    @Column(name = "bookid")
    String bookid;

    @Column(name = "orderid")
    String orderid;

    public OrderItemKey() {
    }

    public OrderItemKey(String bookId, String orderId) {
        this.bookid = bookId;
        this.orderid = orderId;
    }

    public String getBookId() {
        return this.bookid;
    }

    public String getOrderid() {
        return this.orderid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemKey that)) return false;

        return Objects.equals(bookid, that.bookid) && Objects.equals(orderid, that.orderid);
    }

    @Override
    public int hashCode() {
        if (orderid != null && bookid != null) {
            return bookid.hashCode() + orderid.hashCode();
        } else {
            return 0;
        }
    }
}
