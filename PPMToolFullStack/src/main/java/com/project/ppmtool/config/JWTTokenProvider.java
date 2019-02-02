package com.project.ppmtool.config;


import com.project.ppmtool.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.project.ppmtool.config.SecurityConstants.EXPIRATION_TIME;
import static com.project.ppmtool.config.SecurityConstants.SECRET_KEY;

@Component
public class JWTTokenProvider {

    //Generate the token
    public String generateToken(Authentication authentication){

        User user = (User) authentication.getPrincipal();
        Date currentDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(currentDate.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String,Object> claims = new HashMap<>();
        claims.put("id",(Long.toString(user.getId())));
        claims.put("userName",user.getUserName());
        claims.put("fullName",user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .compact();
    }

    //Validate the token

    //Get user Id from token
}
