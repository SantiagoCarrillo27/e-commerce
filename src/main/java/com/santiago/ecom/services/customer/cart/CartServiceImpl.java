package com.santiago.ecom.services.customer.cart;


import com.santiago.ecom.dto.AddProductInCartDto;
import com.santiago.ecom.dto.CartItemsDto;
import com.santiago.ecom.dto.OrderDto;
import com.santiago.ecom.dto.PlaceOrderDto;
import com.santiago.ecom.entity.*;
import com.santiago.ecom.enums.OrderStatus;
import com.santiago.ecom.exceptions.ValidationException;
import com.santiago.ecom.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{


    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;


    public ResponseEntity<?> addProductToCar(AddProductInCartDto addProductInCartDto){
        Orders activeOrder = ordersRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        if (activeOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una orden activa para el usuario");
        }
        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrdersIdAndUserId
                (addProductInCartDto.getProductId(),activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalCartItems.isPresent()){

            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        }else{
            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
            Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());

            if (optionalProduct.isPresent() && optionalUser.isPresent()){

                CartItems cart = new CartItems();
                cart.setProduct(optionalProduct.get());
                cart.setPrice(optionalProduct.get().getPrice());
                cart.setQuantity(1L);
                cart.setUser(optionalUser.get());
                cart.setOrders(activeOrder);

                CartItems updatedCar = cartItemsRepository.save(cart);

                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
                activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
                activeOrder.getCartItems().add(cart);

                ordersRepository.save(activeOrder);

                return ResponseEntity.status(HttpStatus.CREATED).body(cart);

            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario o producto no encontrado");
            }
        }

    }

public OrderDto getCartByUserId(Long userId){
        Orders activeOrder = ordersRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);

    List<CartItemsDto> cartItemsDtoList = activeOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList());


        OrderDto orderDto =  new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());
        orderDto.setCartItems(cartItemsDtoList);
        if (activeOrder.getCoupon() != null){
            orderDto.setCouponName(activeOrder.getCoupon().getName());
        }


        return orderDto;

}


public OrderDto applyCoupon(Long userId, String code){
    Orders activeOrder = ordersRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);

    Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new ValidationException("Cupón no encontrado"));

    if (couponIsExpired(coupon)){
        throw new ValidationException("El cupón ha expirdado");
    }

    double discountAmount = ((coupon.getDiscount() / 100.0) * activeOrder.getTotalAmount());

    double netAmount = activeOrder.getTotalAmount() - discountAmount;

    activeOrder.setAmount((long)netAmount);
    activeOrder.setDiscount((long)discountAmount);
    activeOrder.setCoupon(coupon);

    ordersRepository.save(activeOrder);
    return activeOrder.getOrderDto();

}

private boolean couponIsExpired(Coupon coupon){
    Date currentDate = new Date();
    Date expirationDate = coupon.getExpirationDate();
    return expirationDate != null && currentDate.after(expirationDate);
}



public OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto){

    Orders activeOrder = ordersRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
    Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

    Optional<CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrdersIdAndUserId(
            addProductInCartDto.getProductId(),activeOrder.getId(),addProductInCartDto.getUserId()
    );

    if (optionalProduct.isPresent() && optionalCartItem.isPresent()){
        CartItems cartItems = optionalCartItem.get();
        Product product = optionalProduct.get();

        activeOrder.setAmount(activeOrder.getTotalAmount() + product.getPrice());
        activeOrder.setTotalAmount(activeOrder.getTotalAmount() + product.getPrice());

        cartItems.setQuantity(cartItems.getQuantity() + 1);


        if (activeOrder.getCoupon() != null){
            double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount());

            double netAmount = activeOrder.getTotalAmount() - discountAmount;

            activeOrder.setAmount((long)netAmount);
            activeOrder.setDiscount((long)discountAmount);
        }
        cartItemsRepository.save(cartItems);
        ordersRepository.save(activeOrder);

        return activeOrder.getOrderDto();
    }
    return null;
}


    public OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto){

        Orders activeOrder = ordersRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

        Optional<CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrdersIdAndUserId(
                addProductInCartDto.getProductId(),activeOrder.getId(),addProductInCartDto.getUserId()
        );

        if (optionalProduct.isPresent() && optionalCartItem.isPresent()){
            CartItems cartItems = optionalCartItem.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount(activeOrder.getTotalAmount() - product.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - product.getPrice());

            cartItems.setQuantity(cartItems.getQuantity() - 1);


            if (activeOrder.getCoupon() != null){
                double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount());

                double netAmount = activeOrder.getTotalAmount() - discountAmount;

                activeOrder.setAmount((long)netAmount);
                activeOrder.setDiscount((long)discountAmount);
            }
            cartItemsRepository.save(cartItems);
            ordersRepository.save(activeOrder);

            return activeOrder.getOrderDto();
        }
        return null;
    }



    public OrderDto placeOrder(PlaceOrderDto placeOrderDto){
        Orders activeOrder = ordersRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.Pending);
        Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());

        if (optionalUser.isPresent()){
            activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
            activeOrder.setAddress(placeOrderDto.getAddress());
            activeOrder.setDate(new Date());
            activeOrder.setOrderStatus(OrderStatus.Placed);
            activeOrder.setTrackingId(UUID.randomUUID());

            ordersRepository.save(activeOrder);

            Orders orders = new Orders();
            orders.setAmount(0L);
            orders.setTotalAmount(0L);
            orders.setDiscount(0L);
            orders.setUser(optionalUser.get());
            orders.setOrderStatus(OrderStatus.Pending);
            ordersRepository.save(orders);

            return activeOrder.getOrderDto();
        }
        return null;
    }

    public List<OrderDto> getMyPlacedOrders(Long userId){
    return ordersRepository.findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus.Placed, OrderStatus.Shipped,
            OrderStatus.Delivered)).stream().map(Orders::getOrderDto).collect(Collectors.toList());
    }

    public OrderDto searchOrderByTrackingId(UUID trackingId){
        Optional<Orders> optionalOrders = ordersRepository.findByTrackingId(trackingId);
        if (optionalOrders.isPresent()){
            return optionalOrders.get().getOrderDto();
        }
        return null;
    }

}
