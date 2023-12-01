package org.bookshop.order;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @EmbeddedId
    OrderItemKey id;

    Integer quantity;

    Double unit_price;

    public OrderItemEntity(String orderId, String bookId, Integer quantity, Double unit_price) {
        this.id = new OrderItemKey(bookId, orderId);
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    public OrderItemEntity() {
    }


}
