package com.demo.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    private static final String JWT_KEY = "fsdfsdgdfgdfgsdcvzxvxcvxcvxc";
    private Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);

    public String createJWT(Long userId){
        return JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(new Date())
//                .withExpiresAt()
                .sign(algorithm);
    }

    public Long retriveUserId(String jwtString){
        var decodeJWT = JWT.decode(jwtString);
        var userId = Long.valueOf(decodeJWT.getSubject());
        return userId;
    }

}
