package com.bytes.ms_accounts.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.bytes.ms_accounts.exceptions.AuthException;
import java.security.Key;
import java.util.function.Function;
import javax.crypto.SecretKey;
import java.util.UUID;

@Component
public class JwtUtils {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSignInKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractCustomerId(String token) {
        return UUID.fromString(extractClaim(token, claims -> claims.get("customerId", String.class)));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

     public UUID getCustomerIdFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        
        if (header == null || !header.startsWith("Bearer "))
            throw new AuthException("Invalid or missing Authorization header");
        
        String token = header.substring(7);
        return extractCustomerId(token);
    }
}