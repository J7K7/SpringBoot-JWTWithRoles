package com.demo.blog.security;

import com.demo.blog.users.UsersService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationManager implements AuthenticationManager {

    private final JWTService jwtService;
    private final UsersService usersService;

    public JWTAuthenticationManager(JWTService jwtService, UsersService usersService) {
        this.jwtService = jwtService;
        this.usersService = usersService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(authentication instanceof JWTAuthentication){
            var jwtAuthentication = (JWTAuthentication) authentication;
            var jwt = jwtAuthentication.getCredentials();
            var userId = jwtService.retriveUserId(jwt);
            var userEntity =  usersService.getUser(userId);

            jwtAuthentication.user = userEntity;
            jwtAuthentication.setAuthenticated(true);

            return jwtAuthentication;
        }

        throw new IllegalAccessError("Cannot authenticate with non-JWT authentication!");
    }
}
