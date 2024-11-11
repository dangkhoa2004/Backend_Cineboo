package com.backend.cineboo.config;

import com.backend.cineboo.repository.TaiKhoanRepository;
import com.backend.cineboo.utility.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private  TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private  JWTUtil jwtUtil;

//    public TokenAuthenticationFilter(TaiKhoanRepository taiKhoanRepository, JWTUtil jwtUtil) {
//        this.taiKhoanRepository = taiKhoanRepository;
//        this.jwtUtil = jwtUtil;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String cutToken = token.substring(7);
            String username = jwtUtil.extractUsername(cutToken);
            if (username != null) {
                taiKhoanRepository.findByTenDangNhap(username).ifPresent(taiKhoan -> {
                    if (jwtUtil.validateToken(cutToken, taiKhoan)) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                taiKhoan.getTenDangNhap(), null, new ArrayList<>()
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                });
            }
        }
        filterChain.doFilter(request, response);
    }
}