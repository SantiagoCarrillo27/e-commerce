package com.santiago.ecom.repository;

import com.santiago.ecom.entity.Orders;
import com.santiago.ecom.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long> {

    Orders findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

    List<Orders> findAllByOrderStatusIn(List<OrderStatus> orderStatusList);

    List<Orders> findByUserIdAndOrderStatusIn(Long userId,List<OrderStatus> orderStatus);

    Optional<Orders> findByTrackingId(UUID trackingId);

    List<Orders> findByDateBetweenAndOrderStatus(Date startOfMonth, Date endOfMonth, OrderStatus orderStatus);

    Long countByOrderStatus(OrderStatus orderStatus);
}
