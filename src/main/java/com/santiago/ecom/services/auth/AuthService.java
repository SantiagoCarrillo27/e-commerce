package com.santiago.ecom.services.auth;

import com.santiago.ecom.dto.SignupRequest;
import com.santiago.ecom.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);

    Boolean hasUserWithEmail(String email);
}
