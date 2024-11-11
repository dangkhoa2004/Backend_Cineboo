package com.backend.cineboo.utility;

import com.backend.cineboo.entity.KhachHang;
import com.backend.cineboo.entity.TaiKhoan;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    private static String secret = "VID2019";  // Use a secure secret
    private static int validityInMillis = 3600000;  // 1 hour

    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityInMillis))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    public static String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)  // Correct method call to parse the token
                .getBody();
        return claims.getSubject(); // Get the subject (username)
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, TaiKhoan taiKhoan) {
        final String username = extractUsername(token);
        return (username.equals(taiKhoan.getTenDangNhap()) && !isTokenExpired(token));
    }

}