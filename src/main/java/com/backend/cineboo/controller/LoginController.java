package com.backend.cineboo.controller;

import com.backend.cineboo.dto.LoginDTO;
import com.backend.cineboo.entity.KhachHang;
import com.backend.cineboo.entity.NhanVien;
import com.backend.cineboo.entity.TaiKhoan;
import com.backend.cineboo.repository.KhachHangRepository;
import com.backend.cineboo.repository.NhanVienRepository;
import com.backend.cineboo.repository.TaiKhoanRepository;
import com.backend.cineboo.utility.JWTUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
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

    @Autowired
    NhanVienRepository nhanVienRepository;

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Operation(summary = "Đăng nhập người dùng",
            description = "Gửi thông tin đăng nhập (username, password) và nhận token nếu thông tin hợp lệ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công và trả về token"),
            @ApiResponse(responseCode = "401", description = "Tài khoản không hợp lệ")
    })
    @PostMapping("/user/login")
//    Gimme username
//    Gimme password
//    Gimme secret code
//    I give you token if correct
    public ResponseEntity login(@RequestBody LoginDTO request) {
        String inputUsername = request.getUsername().trim();
        String inputPassword = request.getPassword().trim();

        if(StringUtils.isEmpty(inputPassword)|| StringUtils.isEmpty(inputUsername)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không xác định được thông tin đăng nhập");
        }
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(request.getUsername()).orElse(null);
        String dbHashedPassword = taiKhoan.getMatKhau().trim();
        String dbUsername=taiKhoan.getTenDangNhap().trim();
        Long dbIdTaiKhoan = taiKhoan.getId();

        //1 là nhân viên
        //2 là khách hàng
        Long phanLoaiTaiKhoan =taiKhoan.getPhanLoaiTaiKhoan().getId();
        Integer trangThaiTaiKHoan = taiKhoan.getTrangThaiTaiKhoan();
        if(trangThaiTaiKHoan==0){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản đã bị vô hiệu hoá");
        }
        if(taiKhoan==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản hoặc mật khẩu không chính xác");
        }
        if (dbUsername.equals(inputUsername) && !StringUtils.isEmpty(dbHashedPassword) && BCrypt.checkpw(inputPassword,dbHashedPassword)) {
            String token = JWTUtil.generateToken(inputUsername);
            Map success = new HashMap<>();
            success.put("TenTaiKhoan",dbUsername);
            success.put("token","Bearer "+token);
            if(phanLoaiTaiKhoan==1){
                NhanVien nhanVien = nhanVienRepository.findByID_TaiKhoan(dbIdTaiKhoan.toString()).orElse(null);
                if(nhanVien!=null){
                    success.put("nhanVien",nhanVien);
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile không tồn tại");
                }
            }else if(phanLoaiTaiKhoan==2){
                KhachHang khachHang = khachHangRepository.findByID_TaiKhoan(dbIdTaiKhoan.toString()).orElse(null);
                if(khachHang!=null){
                    success.put("khachHang",khachHang);
                }
                else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile không tồn tại");
                }
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không xác định được loại tài khoản");
            }
            return ResponseEntity.ok(success);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tài khoản hoặc mật khẩu");
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
