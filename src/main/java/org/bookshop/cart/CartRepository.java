package org.bookshop.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Integer>{
    List<CartEntity> findCartEntitiesByBookIdAndUserId(String bookId, String userId);
}
