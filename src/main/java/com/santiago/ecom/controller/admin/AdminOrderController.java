package com.santiago.ecom.controller.admin;


import com.santiago.ecom.dto.AnalyticsResponse;
import com.santiago.ecom.dto.OrderDto;
import com.santiago.ecom.services.admin.adminOrder.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;


    @GetMapping("/placeOrders")
    public ResponseEntity<List<OrderDto>> getAllPlacedOrders(){
        return ResponseEntity.ok(adminOrderService.getAllPlacedOrders());
    }

    @GetMapping("/order/{orderId}/{status}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long orderId, @PathVariable String status){
    OrderDto orderDto = adminOrderService.changeOrderStatus(orderId,status);

    if (orderDto == null)

        return new ResponseEntity<>("Algo salió mal", HttpStatus.BAD_REQUEST);

    return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }


    @GetMapping("/order/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(){
        return ResponseEntity.ok(adminOrderService.calculateAnalytics());
    }

}
