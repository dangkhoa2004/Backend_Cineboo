package com.backend.cineboo.config;

import com.backend.cineboo.repository.KhachHangRepository;
import com.backend.cineboo.utility.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final KhachHangRepository khachHangRepository;
    private final JWTUtil jwtUtil;

    public TokenAuthenticationFilter(KhachHangRepository khachHangRepository, JWTUtil jwtUtil) {
        this.khachHangRepository = khachHangRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String cutToken = token.substring(7);
            String username = jwtUtil.extractUsername(cutToken);
            if (username != null) {
                khachHangRepository.findByTaiKhoan(username).ifPresent(khachHang -> {
                    if (jwtUtil.validateToken(cutToken, khachHang)) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                khachHang.getTaiKhoan(), null, new ArrayList<>()
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                });
            }
        }
        filterChain.doFilter(request, response);
    }
}
