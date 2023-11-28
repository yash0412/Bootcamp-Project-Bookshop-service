package org.bookshop;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "carts")
public class CartEntity {

    @Id
    String id;

    @Column(name = "bookid")
    String  bookId;
    @Column(name = "userid")
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

    @Override
    public String toString() {
        return "CartEntity{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                ", userId='" + userId + '\'' +
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
