package com.santiago.ecom.controller.customer;

import com.santiago.ecom.dto.ProductDetailDto;
import com.santiago.ecom.dto.ProductDto;
import com.santiago.ecom.services.customer.CustomerProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerProductController {

    private final CustomerProductService customerProductService;


    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = customerProductService.getAllProducts();

        if (productDtos == null || productDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDtos);
    }


    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductByName(@PathVariable String name ) {
        List<ProductDto> productDtos = customerProductService.searchProductByTitle(name);

        if (productDtos == null || productDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/product/{productId}")
    public  ResponseEntity<ProductDetailDto> getProductDetailById(@PathVariable Long productId){

        ProductDetailDto productDetailDto = customerProductService.getProductDetailById(productId);
        if (productDetailDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(productDetailDto);
    }


}
