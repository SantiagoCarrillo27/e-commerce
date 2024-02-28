package com.santiago.ecom.services.auth;


import com.santiago.ecom.dto.SignupRequest;
import com.santiago.ecom.dto.UserDto;
import com.santiago.ecom.entity.Orders;
import com.santiago.ecom.entity.User;
import com.santiago.ecom.enums.OrderStatus;
import com.santiago.ecom.enums.UserRole;
import com.santiago.ecom.repository.OrdersRepository;
import com.santiago.ecom.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private OrdersRepository ordersRepository;


    public UserDto createUser(SignupRequest signupRequest){

        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);


        Orders orders = new Orders();
        orders.setAmount(0L);
        orders.setTotalAmount(0L);
        orders.setDiscount(0L);
        orders.setUser(createdUser);
        orders.setOrderStatus(OrderStatus.Pending);
        ordersRepository.save(orders);



        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());

        return userDto;

    }

    public Boolean hasUserWithEmail(String email){
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);
        if (null == adminAccount){
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
    }


}
