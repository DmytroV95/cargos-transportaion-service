package com.varukha.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.security.Key;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Key secret;

//    public JwtUtil(@Value("${jwt.secret}") String secretString) {
//        secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
//
//        private final SecretKey secret;

    public JwtUtil() {
            String secretString = "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";
            secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        }


//    public String generateToken(String userName) {
//        return Jwts.builder()
//                .setSubject(userName)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(secret)
//                .compact();
//    }

//    public boolean isValidToken(String token) {
//        try {
//            Jws<Claims> claimsJws = Jwts.parserBuilder()
//                    .setSigningKey(secret)
//                    .build()
//                    .parseClaimsJws(token);
//            return !claimsJws.getBody().getExpiration().before(new Date());
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new JwtException("Expired or invalid JWT token");
//        }
//    }

    public String getUserName(String token) {
        String userName = getClaimFromToken(token, Claims::getSubject);
        System.out.println("userName" + userName);
        return userName;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);

    }
}
