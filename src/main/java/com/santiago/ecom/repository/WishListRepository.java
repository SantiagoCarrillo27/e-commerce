package com.santiago.ecom.repository;

import com.santiago.ecom.dto.WishListDto;
import com.santiago.ecom.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList,Long> {


    List<WishList> findAllByUserId(Long userId);


}
