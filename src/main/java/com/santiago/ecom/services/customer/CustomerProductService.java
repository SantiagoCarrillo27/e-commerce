package com.santiago.ecom.services.customer;

import com.santiago.ecom.dto.ProductDetailDto;
import com.santiago.ecom.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface CustomerProductService {


    List<ProductDto> searchProductByTitle(String title);

    List<ProductDto> getAllProducts();

    ProductDetailDto getProductDetailById(Long productId);

}
