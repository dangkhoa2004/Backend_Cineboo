package com.backend.cineboo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http
//                .csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.disable())
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS, "/suatchieu/ID_Phim/*.").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs/swagger-config",
                        "/api-docs",
                        "/swagger-resources/**",
                        "/swagger-resources",
                        "/swagger-ui/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/api/user/login").permitAll()
// API Prefix: /phim
                .requestMatchers("/phim/get", "/phim/find/**").permitAll()
                .requestMatchers("/phim/add").hasAnyAuthority("Quản trị viên")
                .requestMatchers("/phim/update/**", "/phim/disable/**").hasAnyAuthority("Quản lý", "Quản trị viên")

// API Prefix: /hoadon
                .requestMatchers("/hoadon/add").hasAnyAuthority("Nhân viên bán vé", "Quản lý", "Quản trị viên", "KhachHang")
                .requestMatchers("/hoadon/get/**", "/hoadon/find/**").hasAnyAuthority("KhachHang", "Nhân viên bán vé", "Quản lý", "Quản trị viên")
                .requestMatchers("/hoadon/update/**").hasAnyAuthority("Nhân viên bán vé", "Quản lý", "Quản trị viên")
                .requestMatchers("/hoadon/cancel").hasAnyAuthority("KhachHang")
                .requestMatchers("/hoadon/cancel/confirm").hasAnyAuthority("Nhân viên kế toán")
                .requestMatchers("/hoadon/disable/**").hasAnyAuthority("Quản lý", "Quản trị viên")

// API Prefix: /khachhang
                .requestMatchers("/khachhang/add").hasAnyAuthority("Quản lý", "Quản trị viên")
                .requestMatchers("/khachhang/get/**", "/khachhang/find/**").hasAnyAuthority("Quản lý", "Quản trị viên")
                .requestMatchers("/khachhang/update/**", "/khachhang/disable/**").hasAnyAuthority("Quản lý", "Quản trị viên")

// API Prefix: /voucher
                .requestMatchers("/voucher/get", "/voucher/find/**").hasAnyAuthority("Nhân viên bán vé", "Quản lý", "Quản trị viên", "KhachHang")
                .requestMatchers("/voucher/add", "/voucher/update/**", "/voucher/disable/**").hasAnyAuthority("Quản lý", "Quản trị viên")

// API Prefix: /theloaiphim
                .requestMatchers("/theloaiphim/get", "/theloaiphim/find/**").hasAnyAuthority("Nhân viên bán vé", "Quản lý", "Quản trị viên")
                .requestMatchers("/theloaiphim/add", "/theloaiphim/update/**", "/theloaiphim/disable/**").hasAnyAuthority("Quản trị viên")

// API Prefix: /pttt (Payment Methods)
                .requestMatchers("/pttt/get", "/pttt/find/**").hasAnyAuthority("Quản lý", "Quản trị viên")
                .requestMatchers("/pttt/add", "/pttt/update/**", "/pttt/disable/**").hasAnyAuthority("Quản trị viên")

// API Prefix: /dotuoi (Age Restrictions)
                .requestMatchers("/dotuoi/get", "/dotuoi/find/**").hasAnyAuthority("Quản trị viên")
                .requestMatchers("/dotuoi/add", "/dotuoi/update/**", "/dotuoi/disable/**").hasAnyAuthority("Quản trị viên")

// API Prefix: /ghe (Seats)
                .requestMatchers("/ghe/get", "/ghe/find/**").hasAnyAuthority("Quản trị viên","KhachHang","Quản lý","Nhân viên bán vé")
                .requestMatchers("/ghe/add", "/ghe/update/**", "/ghe/disable/**").hasAnyAuthority("Quản trị viên","KhachHang","Quản lý","Nhân viên bán vé","NhanVien")
//                .requestMatchers("/ghe/*").permitAll()
//                .requestMatchers("/ghe/**").permitAll()
// API Prefix: /review
                .requestMatchers("/review/add").hasAnyAuthority("KhachHang")
                .requestMatchers("/review/update/**", "/review/disable/**").hasAnyAuthority("Quản lý", "Quản trị viên")

// API Prefix: /thongke (Statistics)
                .requestMatchers("/thongke/**").hasAnyAuthority("Quản lý", "Nhân viên Marketing")

// API Prefix: /payos (Payment-related APIs)
                .requestMatchers("/payos/create-payment-link/**").permitAll()
                .requestMatchers("/payos/cancel/**", "/payos/get/**").permitAll()
// API Prefix: /suatchieu (Showtimes)
//                                .requestMatchers("/suatchieu/get/**", "/suatchieu/find/**","/suatchieu/find/ID_Phim/**").hasAnyAuthority("KhachHang", "Nhân viên bán vé", "Quản lý", "Quản trị viên")
//                                .requestMatchers("/suatchieu/add", "/suatchieu/update/**", "/suatchieu/disable/**").hasAnyAuthority("Quản trị viên", "Quản lý")
                .anyRequest().permitAll()

                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
