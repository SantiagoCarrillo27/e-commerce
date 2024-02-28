package com.santiago.ecom.services.customer.wishList;

import com.santiago.ecom.dto.WishListDto;
import com.santiago.ecom.entity.Product;
import com.santiago.ecom.entity.User;
import com.santiago.ecom.entity.WishList;
import com.santiago.ecom.repository.ProductRepository;
import com.santiago.ecom.repository.UserRepository;
import com.santiago.ecom.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class WishListServiceImpl implements WishListService{

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final WishListRepository wishListRepository;


    public WishListDto addProductToWishList(WishListDto wishListDto){
        Optional<Product> optionalProduct = productRepository.findById(wishListDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(wishListDto.getUserId());

        if (optionalProduct.isPresent() && optionalUser.isPresent()){
            WishList wishList = new WishList();

            wishList.setProduct(optionalProduct.get());
            wishList.setUser(optionalUser.get());

            return wishListRepository.save(wishList).getWishListDto();

        }
        return null;
    }


    public List<WishListDto> getWishListByUserId(Long userId){
        return wishListRepository.findAllByUserId(userId).stream().map(WishList::getWishListDto).collect(Collectors.toList());
    }

}
