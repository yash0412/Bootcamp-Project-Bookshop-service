package org.bookshop.cart;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "carts")
public class CartEntity {
    @EmbeddedId
    UserBookKey id;
    Integer qty;

    Timestamp createdDate;
    Timestamp updatedDate;

    public CartEntity(
            UserBookKey id,
            Integer qty
    ) {
        this.id = id;
        this.qty = qty;
        this.createdDate = new Timestamp(System.currentTimeMillis());
        this.updatedDate = new Timestamp(System.currentTimeMillis());
    }

    public CartEntity() {

    }

    @Override
    public String toString() {
        return "CartEntity{" +
                "id='" + id + '\'' +
                ", qty=" + qty +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartEntity that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
