package com.santiago.ecom.services.customer.review;

import com.santiago.ecom.dto.OrderedProductsResponseDto;
import com.santiago.ecom.dto.ProductDto;
import com.santiago.ecom.dto.ReviewDto;
import com.santiago.ecom.entity.*;
import com.santiago.ecom.repository.OrdersRepository;
import com.santiago.ecom.repository.ProductRepository;
import com.santiago.ecom.repository.ReviewRepository;
import com.santiago.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{


    private final OrdersRepository ordersRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ReviewRepository reviewRepository;

    public OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId){
        Optional<Orders> optionalOrders = ordersRepository.findById(orderId);
        OrderedProductsResponseDto orderedProductsResponseDto = new OrderedProductsResponseDto();

        if (optionalOrders.isPresent()){
            orderedProductsResponseDto.setOrderAmount(optionalOrders.get().getAmount());
            List<ProductDto> productDtoList = new ArrayList<>();
            for (CartItems cartItems:optionalOrders.get().getCartItems()){
                ProductDto productDto = new ProductDto();

                productDto.setId(cartItems.getProduct().getId());
                productDto.setName(cartItems.getProduct().getName());
                productDto.setPrice(cartItems.getPrice());
                productDto.setQuantity(cartItems.getQuantity());


                productDto.setByteImg(cartItems.getProduct().getImg());

                productDtoList.add(productDto);
            }

            orderedProductsResponseDto.setProductDtoList(productDtoList);

        }

        return orderedProductsResponseDto;
    }

    public ReviewDto giveReview(ReviewDto reviewDto) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(reviewDto.getUserId());

        if (optionalProduct.isPresent() && optionalUser.isPresent()){
            Review review = new Review();

            review.setRating(reviewDto.getRating());
            review.setDescription(reviewDto.getDescription());
            review.setUser(optionalUser.get());
            review.setProduct(optionalProduct.get());
            review.setImg(reviewDto.getImg().getBytes());

            return reviewRepository.save(review).GetDto();
        }
        return null;
    }





}
