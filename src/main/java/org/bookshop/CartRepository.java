package org.bookshop;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, Integer>{
    CartEntity findByUserId(String uuid);
}
