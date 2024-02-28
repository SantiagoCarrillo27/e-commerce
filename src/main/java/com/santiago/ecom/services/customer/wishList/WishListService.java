package com.santiago.ecom.services.customer.wishList;

import com.santiago.ecom.dto.WishListDto;

import java.util.List;

public interface WishListService {

    WishListDto addProductToWishList(WishListDto wishListDto);
    List<WishListDto> getWishListByUserId(Long userId);
}
