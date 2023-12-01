package org.bookshop.cart;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class UserBookKey implements Serializable {
    @Column(name = "userid")
    private String userId;

    @Column(name = "bookid")
    private String bookId;

    public UserBookKey(String userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }
    public String getBookId() {
        return this.bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBookKey that)) return false;

        return Objects.equals(userId, that.userId);
    }
}
