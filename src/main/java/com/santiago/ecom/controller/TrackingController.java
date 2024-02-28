package com.santiago.ecom.controller;

import com.santiago.ecom.dto.OrderDto;
import com.santiago.ecom.services.customer.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final CartService cartService;

    @GetMapping("/order/{trackingId}")
    public ResponseEntity<OrderDto> searchOrderByTrackingId(@PathVariable UUID trackingId){
        System.out.println("Received request for trackingId: " + trackingId);

        OrderDto orderDto = cartService.searchOrderByTrackingId(trackingId);

        if (orderDto == null) {
            System.out.println("Order not found for trackingId: " + trackingId);
            return ResponseEntity.notFound().build();
        }

        System.out.println("Order found for trackingId: " + trackingId);
        return ResponseEntity.ok(orderDto);
    }


}
