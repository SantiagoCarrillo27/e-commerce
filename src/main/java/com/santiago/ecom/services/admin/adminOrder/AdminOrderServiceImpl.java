package com.santiago.ecom.services.admin.adminOrder;


import com.santiago.ecom.dto.AnalyticsResponse;
import com.santiago.ecom.dto.OrderDto;
import com.santiago.ecom.entity.Orders;
import com.santiago.ecom.enums.OrderStatus;
import com.santiago.ecom.repository.OrdersRepository;
import jakarta.persistence.criteria.Order;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService{

    private final OrdersRepository ordersRepository;

    public List<OrderDto> getAllPlacedOrders() {
        List<Orders> orderList = ordersRepository.findAllByOrderStatusIn(List.of(OrderStatus.Placed, OrderStatus.Shipped, OrderStatus.Delivered));
        return orderList.stream().map(Orders::getOrderDto).collect(Collectors.toList());
    }


    public OrderDto changeOrderStatus(Long orderId, String status){

        Optional<Orders> optionalOrders = ordersRepository.findById(orderId);
        if (optionalOrders.isPresent()){
            Orders orders = optionalOrders.get();

            if (Objects.equals(status, "Shipped")){
                orders.setOrderStatus(OrderStatus.Shipped);

            } else if (Objects.equals(status, "Delivered")){
                orders.setOrderStatus(OrderStatus.Delivered);
            }
            return ordersRepository.save(orders).getOrderDto();
        }

        return null;
    }


    public AnalyticsResponse calculateAnalytics(){
        LocalDate currentDate = LocalDate.now();

        LocalDate previousMonthDate = currentDate.minusMonths(1);

        Long currentMonthOrders = getTotalOrdersForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previusMonthOrders = getTotalOrdersForMonth(previousMonthDate.getMonthValue(),previousMonthDate.getYear());

        Long currentMonthEarnings = getTotalEarningsForMonth(currentDate.getMonthValue(), currentDate.getYear());

        Long previusMonthEarnings = getTotalEarningsForMonth(previousMonthDate.getMonthValue(),previousMonthDate.getYear());

        Long placed = ordersRepository.countByOrderStatus(OrderStatus.Placed);
        Long shipped = ordersRepository.countByOrderStatus(OrderStatus.Shipped);
        Long delivered = ordersRepository.countByOrderStatus(OrderStatus.Delivered);

        return new AnalyticsResponse(placed,shipped,delivered,currentMonthOrders,previusMonthOrders,currentMonthEarnings,previusMonthEarnings);
    }




    public Long getTotalOrdersForMonth(int month, int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);

        Date endOfMonth = calendar.getTime();

        List<Orders> orders = ordersRepository.findByDateBetweenAndOrderStatus(startOfMonth, endOfMonth, OrderStatus.Delivered);

        return (long) orders.size();
    }


    public Long getTotalEarningsForMonth(int month, int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);

        Date endOfMonth = calendar.getTime();

        List<Orders> orders = ordersRepository.findByDateBetweenAndOrderStatus(startOfMonth, endOfMonth, OrderStatus.Delivered);

        Long sum = 0L;
        for (Orders order: orders
             ) {
            sum += order.getAmount();
        }
        return sum;
    }

}
