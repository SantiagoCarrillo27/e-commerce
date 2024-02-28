package com.santiago.ecom.repository;


import com.santiago.ecom.entity.FQA;
import com.santiago.ecom.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findAllByProductId(Long productId);

}
