package com.backend.cineboo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    RequestThrottleFilter requestThrottleFilter;

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
                .requestMatchers("/phim/get", "/phim/find/**").permitAll()//done
                .requestMatchers("/phim/add").hasAnyAuthority(ERoles.QL, ERoles.QTV)//done
                .requestMatchers("/phim/update/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)//done
                .requestMatchers("/phim/disable/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)//done
//
// API Prefix: /hoadon
                .requestMatchers("/hoadon/add").hasAnyAuthority(CRoles.GENERAL)
                .requestMatchers("/hoadon/get","/hoadon/get/**").hasAnyAuthority(ERoles.QL, ERoles.QTV, ERoles.NV_KT_TOAN, ERoles.NV_TN)
                .requestMatchers("/hoadon/find/**").hasAnyAuthority(CRoles.GENERAL, ERoles.QL, ERoles.QTV, ERoles.NV_KT_TOAN, ERoles.NV_TN)
                .requestMatchers("/hoadon/update/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)
                .requestMatchers("/hoadon/cancel/**").hasAnyAuthority(CRoles.GENERAL)
                .requestMatchers("/hoadon/cancel/confirm").hasAnyAuthority(ERoles.NV_KT_TOAN)
                .requestMatchers("/hoadon/disable/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)
                .requestMatchers("/hoadon/price/**").permitAll()
                .requestMatchers("/hoadon/print/**").hasAnyAuthority(ERoles.QL, ERoles.QTV, ERoles.NV_BV)//done
                .requestMatchers("/hoadon/status/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)
                .requestMatchers("/hoadon/download/**").hasAnyAuthority(ERoles.GENERAL,CRoles.GENERAL)
//
//// API Prefix: /khachhang
                .requestMatchers("/khachhang/add").permitAll()
                .requestMatchers("/khachhang/find/**").hasAnyAuthority(ERoles.QL, ERoles.QTV, ERoles.NV_KT_TOAN, ERoles.NV_BV, ERoles.NV_TN, CRoles.GENERAL)
                .requestMatchers("/khachhang/get/**").hasAnyAuthority(ERoles.QL, ERoles.QTV, ERoles.NV_KT_TOAN, ERoles.NV_BV, ERoles.NV_TN)
                .requestMatchers("/khachhang/get").hasAnyAuthority(ERoles.QL, ERoles.QTV, ERoles.NV_KT_TOAN, ERoles.NV_BV, ERoles.NV_TN)
                .requestMatchers("/khachhang/update/**", "/khachhang/disable/**").hasAnyAuthority(ERoles.QL,ERoles.QTV)
                .requestMatchers("/khachhang/update", "/khachhang/disable").hasAnyAuthority(ERoles.QL,ERoles.QTV)

//// API Prefix: /voucher
                .requestMatchers("/voucher/get", "/voucher/find/**").permitAll()
                .requestMatchers("/voucher/add", "/voucher/update/**", "/voucher/disable/**").hasAnyAuthority(ERoles.QL,ERoles.QTV)
//
//// API Prefix: /theloaiphim
                .requestMatchers("/theloaiphim/get", "/theloaiphim/find/**").permitAll()
                .requestMatchers("/theloaiphim/add", "/theloaiphim/update/**", "/theloaiphim/disable/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)
//
//// API Prefix: /pttt (Payment Methods)
                .requestMatchers("/pttt/get", "/pttt/find/**").permitAll()
                .requestMatchers("/pttt/add", "/pttt/update/**", "/pttt/disable/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)
//
//// API Prefix: /dotuoi (Age Restrictions)
                .requestMatchers("/dotuoi/get", "/dotuoi/find/**").permitAll()
                .requestMatchers("/dotuoi/add", "/dotuoi/update/**", "/dotuoi/disable/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)
//
//// API Prefix: /ghe (Seats)
                .requestMatchers("/ghe/get", "/ghe/find/**").hasAnyAuthority(CRoles.GENERAL, ERoles.GENERAL)
                .requestMatchers("/ghe/findWithBooking/**").hasAnyAuthority(CRoles.GENERAL, ERoles.GENERAL)
                .requestMatchers("/ghe/add", "/ghe/update/**", "/ghe/disable/**").hasAnyAuthority(ERoles.QTV, ERoles.QL)
//// API Prefix: /review
                .requestMatchers("/review/add").hasAnyAuthority(CRoles.GENERAL)
                .requestMatchers("/review/update/**", "/review/disable/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)
//
//// API Prefix: /thongke (Statistics)
                .requestMatchers("/thongke/**").hasAnyAuthority(ERoles.QL, ERoles.QTV, ERoles.NV_KT_TOAN, ERoles.NV_MKT)
//
// API Prefix: /payos (Payment-related APIs)
                .requestMatchers("/payos/create-payment-link/**").hasAnyAuthority(CRoles.GENERAL)
                .requestMatchers("/payos/get/**").permitAll()
                .requestMatchers("/payos/cancel/**").hasAnyAuthority(CRoles.GENERAL, ERoles.NV_BV, ERoles.QL)
                .requestMatchers("/payos/confirm-webhook").permitAll()
// API Prefix: /suatchieu (Showtimes)
                .requestMatchers("/suatchieu/get/**", "/suatchieu/find/**", "/suatchieu/find/ID_Phim/**").permitAll()
                .requestMatchers("/suatchieu/add","/suatchieu/add/**", "/suatchieu/update/**", "/suatchieu/disable/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)
                .requestMatchers("/suatchieu/get").permitAll()
                // API Prefix: /phongchieu
                .requestMatchers("/phongchieu/get/**", "/phongchieu/find/**").permitAll()
                .requestMatchers("/phongchieu/get").permitAll()
                .requestMatchers("/phongchieu/add", "/phongchieu/update/**", "/phongchieu/disable/**").hasAnyAuthority(ERoles.QL, ERoles.QTV)
                //API Prefix: /taikhoan
                .requestMatchers("/taikhoan/**").permitAll()
                //API Prefix: /nhanvien
                .requestMatchers("/nhanvien/get").hasAnyAuthority(ERoles.QL,ERoles.QTV)
                .requestMatchers("/nhanvien/add").hasAnyAuthority(ERoles.QL,ERoles.QTV)
                .requestMatchers("/nhanvien/update").hasAnyAuthority(ERoles.QL,ERoles.QTV)
                .requestMatchers("/nhanvien/update/**").hasAnyAuthority(ERoles.QL,ERoles.QTV)
                .requestMatchers("/nhanvien/find").hasAnyAuthority(ERoles.GENERAL)
                .requestMatchers("/nhanvien/find/**").hasAnyAuthority(ERoles.GENERAL)
                .requestMatchers("/nhanvien/disable/**").hasAnyAuthority(ERoles.QL,ERoles.QTV)
                //API Prefix: /hoanve
                .requestMatchers("/hoanve/**").hasAnyAuthority(ERoles.QL,ERoles.QTV,ERoles.NV_KT_TOAN)

                //API Prefix: /chitiethoadon
                .requestMatchers("/cthoadon/get","/cthoadon/status/**","/cthoadon/update/**","/cthoadon/disable/**").hasAnyAuthority(ERoles.QTV,ERoles.QL,ERoles.NV_BV)
                .requestMatchers( "/cthoadon/add","/cthoadon/find/**").permitAll()

                //API Prefix: /dstl
                .requestMatchers("/dstl/**").permitAll()

                //API Prefix: /gheandsuatchieu
                .requestMatchers("/gheandsuatchieu/get").permitAll()
                .requestMatchers("/gheandsuatchieu/add").hasAnyAuthority(ERoles.QL,ERoles.QTV)
                .requestMatchers("/gheandsuatchieu/update/**").hasAnyAuthority(ERoles.QL,ERoles.QTV)
                .requestMatchers("/gheandsuatchieu/status/**").hasAnyAuthority(ERoles.QL,ERoles.QTV)
                .requestMatchers("/gheandsuatchieu/find/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(requestThrottleFilter, TokenAuthenticationFilter.class);
        return http.build();
    }
}
