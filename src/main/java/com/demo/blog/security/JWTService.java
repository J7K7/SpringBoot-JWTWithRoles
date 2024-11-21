package com.demo.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JWTService {
    private static final String JWT_KEY = "fsdfsdgdfgdfgsdcvzxvxcvxcvxc";
    private static final String PASS_JWT_KEY = "MeHoonNa";
    private static final String JWT_ISSUER = "STPL";
    private Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);
    private Algorithm passAlgorithm = Algorithm.HMAC256(PASS_JWT_KEY);

    public String createJWT(String username, List<String> roles){
        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withSubject(username)
                .withClaim("roles", roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 1000))
                .sign(algorithm);
    }

    public String createPasswordJWT(String email){
        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .sign(passAlgorithm);
    }

    public String retriveUsername(String jwtString){
        var decodeJWT = JWT.decode(jwtString);
        return String.valueOf(decodeJWT.getSubject());
    }

    public String retrivePassEmail(String jwtString){
        var decodeJWT = JWT.decode(jwtString);
        return String.valueOf(decodeJWT.getSubject());
    }

    public boolean isValidPassToken(String token){
        try {
            JWT.require(Algorithm.HMAC256(PASS_JWT_KEY))
                    .withIssuer(JWT_ISSUER)
                    .build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public boolean isValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(JWT_KEY))
                    .withIssuer(JWT_ISSUER)
                    .build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public List<String> getRolesFromToken(String token){
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim("roles").asList(String.class);
    }

    private DecodedJWT verifyToken(String jwtString){
        return JWT.require(Algorithm.HMAC256(JWT_KEY))
                .withIssuer(JWT_ISSUER)
                .build().verify(jwtString);
    }

}
