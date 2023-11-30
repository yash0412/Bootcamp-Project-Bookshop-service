package org.bookshop.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, UserBookKeyEntity> {
    CartEntity findCartEntityById(UserBookKeyEntity userBookId);

    List<CartEntity> findCartEntitiesById_UserId(String userid);

    boolean existsById(UserBookKeyEntity id);

}
