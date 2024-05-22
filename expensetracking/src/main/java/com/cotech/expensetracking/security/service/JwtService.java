package com.cotech.expensetracking.security.service;

import com.cotech.expensetracking.jpa.userauth.UserAuthEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    // Generate via online website
    // Encryption 256
//    @Value("${jwt.sign-in-key}")
    private final static String SECRET_KEY =
            "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";

    public String getUsernameFromToken(final String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(final String token) {
        return this.extractClaim(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

    public boolean isValidToken(final String token, final UserAuthEntity user) {
        String username = this.getUsernameFromToken(token);
        return (username == user.getUsername() && !isTokenExpired(token));
    }

    public String generateToken(final UserAuthEntity userAuth) {
        return this.generateToken(new HashMap<>(), userAuth);
    }

    public String generateToken(final Map<String, String> extraClaims,
                                final UserAuthEntity userAuth) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userAuth.getUsername())
                // Subject should have unique identifier of user for which the token is
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(this.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Claims extractAllClaims(final String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}