package com.santiago.ecom.services.admin.adminOrder;

import com.santiago.ecom.dto.AnalyticsResponse;
import com.santiago.ecom.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {


    List<OrderDto> getAllPlacedOrders();


    OrderDto changeOrderStatus(Long orderId, String status);

    AnalyticsResponse calculateAnalytics();
}
