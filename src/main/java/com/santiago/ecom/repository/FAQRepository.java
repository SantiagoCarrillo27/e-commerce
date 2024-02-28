package com.santiago.ecom.repository;

import com.santiago.ecom.entity.FQA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository  extends JpaRepository<FQA,Long> {

   List<FQA> findAllByProductId(Long productId);
}
