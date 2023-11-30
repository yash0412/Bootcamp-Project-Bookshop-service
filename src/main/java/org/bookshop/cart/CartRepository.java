package org.bookshop.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity,  UserBookKey>{
    List<CartEntity> findCartEntitiesById(UserBookKey userBookId);

    List<CartEntity> findCartEntitiesById_UserId(String userid);

}
