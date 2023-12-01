package org.bookshop.order;

import org.bookshop.cart.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemKey> {

    List<OrderItemEntity> findOrderItemEntitiesById_Orderid(String orderid);

}
