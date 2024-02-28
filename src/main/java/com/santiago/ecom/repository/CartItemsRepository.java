package com.santiago.ecom.repository;

import com.santiago.ecom.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {


    Optional<CartItems> findByProductIdAndOrdersIdAndUserId(Long productId, Long orderId, Long userId);


}
