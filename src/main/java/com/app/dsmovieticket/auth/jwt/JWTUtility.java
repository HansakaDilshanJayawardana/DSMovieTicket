package com.app.dsmovieticket.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtility implements Serializable {

    //JWT Token Validation Time
    private final int JWT_TOKEN_VALIDITY = 60 * 60 * 1000; // 60min

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    //Retrieve username from JWT Token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //Retrieve Expiration Date from JWT Token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //Get Role from Token
    public String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role").toString();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //For retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    //Check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //Generate Token for User
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername() , userDetails.getAuthorities());
    }

    //While Creating the Token -
    //1.Define claims of the Token, like Issuer, Expiration, Subject, and the ID
    //2.Sign the JWT using the HS512 Algorithm and Secret Key.
    private String doGenerateToken(Map<String, Object> claims, String subject, Collection<? extends GrantedAuthority> authorities) {
        String role = authorities.stream().findFirst().get().getAuthority();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .claim("role" , role )
                .compact();
    }

    //Validate Token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}