package org.bookshop;

import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "carts")
public class CartEntity {

    @Id
    @Column(name = "id")
    String id;
    String  bookId;
    String userId;
    Integer qty;

    public CartEntity(
            String id,
            String bookId,
            String  userId,
            Integer qty
    ) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.qty = qty;
    }

    public CartEntity() {

    }
}
