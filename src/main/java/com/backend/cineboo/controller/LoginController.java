package com.backend.cineboo.controller;

import com.backend.cineboo.dto.LoginDTO;
import com.backend.cineboo.entity.KhachHang;
import com.backend.cineboo.repository.KhachHangRepository;
import com.backend.cineboo.utility.JWTUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Controller
@RequestMapping("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "LoginAPI",
                version = "0.1",
                description = "Controller hỗ trợ đăng nhập(lấy JWT token) và kiểm tra token"
        ))
public class LoginController {
    @Autowired
    KhachHangRepository khachHangRepository;

    @Operation(summary = "Đăng nhập người dùng",
            description = "Gửi thông tin đăng nhập (username, password) và nhận token nếu thông tin hợp lệ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công và trả về token"),
            @ApiResponse(responseCode = "401", description = "Tài khoản không hợp lệ")
    })
    @PostMapping("/user/login")
    //Gimme username
    //Gimme password
    //Gimme secret code
    //I give you token if correct
    public ResponseEntity login(@RequestBody LoginDTO request) {
        String username = request.getUsername();
        KhachHang khachHang = khachHangRepository.findByTaiKhoan(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (khachHang.getMatKhau().equals(request.getPassword())) {
            String token = JWTUtil.generateToken(khachHang.getTaiKhoan());
            System.out.println("token is: " + token);
            Map<String,String> success = new HashMap<>();
            success.put("username",username);
            success.put("token","Bearer "+token);
            System.out.println("BTW: By extracting this ass token, i got username:");
            System.out.println(JWTUtil.extractUsername(token));
            return ResponseEntity.ok(success);
        }
        System.out.println("This is so ass. Function failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    //Method to test token
    //Add "Bearer" prefix before your token
    //e.g.: Bearer eyJhbGciOiJIUzUxMiJ9.eyS9...................
    //Nếu dùng PostMan thì dùng Auth type là API KEY
    //Key: Authorization
    //Value: Bearer <Token vừa trả về bởi login()>
    @Operation(summary = "Kiểm tra token",
            description = "Phương thức kiểm tra token. Gửi token với định dạng 'Bearer <token>' để kiểm tra.")
    @ApiResponse(responseCode = "200", description = "Token hợp lệ")
    @GetMapping("/login/test")
    public ResponseEntity test() {
        return ResponseEntity.ok("THIS IS SO ASSSSSSSSSSSSSSSSSSSS");
    }
}
