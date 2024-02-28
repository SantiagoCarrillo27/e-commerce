package com.santiago.ecom.services.customer.cart;


import com.santiago.ecom.dto.AddProductInCartDto;
import com.santiago.ecom.dto.OrderDto;
import com.santiago.ecom.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {

    ResponseEntity<?> addProductToCar(AddProductInCartDto addProductInCartDto);

    OrderDto getCartByUserId(Long userId);
    OrderDto applyCoupon(Long userId, String code);

    OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);

    OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto);

    OrderDto placeOrder(PlaceOrderDto placeOrderDto);

    List<OrderDto> getMyPlacedOrders(Long userId);


    OrderDto searchOrderByTrackingId(UUID trackingId);
}
