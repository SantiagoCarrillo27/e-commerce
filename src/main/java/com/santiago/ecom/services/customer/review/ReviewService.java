package com.santiago.ecom.services.customer.review;

import com.santiago.ecom.dto.OrderedProductsResponseDto;
import com.santiago.ecom.dto.ReviewDto;

import java.io.IOException;

public interface ReviewService {

    OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId);

    ReviewDto giveReview(ReviewDto reviewDto) throws IOException;
}
