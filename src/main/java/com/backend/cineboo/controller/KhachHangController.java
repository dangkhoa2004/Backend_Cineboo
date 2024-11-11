package com.backend.cineboo.controller;

import com.backend.cineboo.dto.KhachHangRegister;
import com.backend.cineboo.entity.KhachHang;
import com.backend.cineboo.entity.PhanLoaiKhachHang;
import com.backend.cineboo.entity.PhanLoaiTaiKhoan;
import com.backend.cineboo.entity.TaiKhoan;
import com.backend.cineboo.repository.KhachHangRepository;
import com.backend.cineboo.repository.PhanLoaiTaiKhoanRepository;
import com.backend.cineboo.repository.TaiKhoanRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/khachhang")
public class KhachHangController {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private PhanLoaiTaiKhoanRepository phanLoaiTaiKhoanRepository;

    @Operation(summary = "Lấy danh sách khách hàng",
            description = "Trả về danh sách tất cả khách hàng trong hệ thống.")
    @GetMapping("/get")
    public ResponseEntity<List<KhachHang>> getAllKhachHang() {
        List<KhachHang> khachHangs = khachHangRepository.findAll();
        return ResponseEntity.ok(khachHangs);
    }

    @Operation(summary = "Thêm khách hàng mới",
            description = "Thêm một khách hàng mới vào hệ thống.")
    @PostMapping("/add")
    public ResponseEntity addKhachHang(@Valid @RequestBody KhachHangRegister khachHangRegister, BindingResult bindingResult,
                                       @RequestParam("username")  String username,
                                       @RequestParam("password") String password) {
        String inputUsername = username.trim();
        String inputPassword = password.trim();

        if(StringUtils.isEmpty(inputPassword)|| StringUtils.isEmpty(inputUsername)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không xác định được thông tin đăng nhập");
        }
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        //Create new TaiKhoan
        TaiKhoan taiKhoan = new TaiKhoan();
        Integer row = taiKhoanRepository.checkAvailable(inputUsername).orElse(null);
        if(row==null) {
            taiKhoan.setTenDangNhap(inputUsername);
            taiKhoan.setMatKhau(BCrypt.hashpw(inputPassword, BCrypt.gensalt()));
            taiKhoan.setGhiChu("");//None
            taiKhoan.setOtp("");//None
            taiKhoan.setPhanLoaiTaiKhoan(new PhanLoaiTaiKhoan(Long.valueOf("2"), "KhachHang", 0));//Khách hàng = 2
            taiKhoan.setTrangThaiTaiKhoan(0);//Chưa kích hoạt
            taiKhoanRepository.save(taiKhoan);//Tạo mới tài khoản

            //Create new KhachHang
            KhachHang newKhachHang = new KhachHang();
            String maKhachHang = String.valueOf(khachHangRepository.getMaxTableId() + 1);
            newKhachHang.setMaKhachHang(maKhachHang);
            newKhachHang.setTen(khachHangRegister.getTen());
            newKhachHang.setTenDem(khachHangRegister.getTenDem());
            newKhachHang.setHo(khachHangRegister.getHo());
            newKhachHang.setNgaySinh(khachHangRegister.getNgaySinh());
            newKhachHang.setSoDienThoai(khachHangRegister.getSoDienThoai());
            newKhachHang.setGioiTinh(khachHangRegister.getGioiTinh());
            newKhachHang.setEmail(khachHangRegister.getEmail());
            newKhachHang.setDanToc(khachHangRegister.getDanToc());
            newKhachHang.setDiaChi(khachHangRegister.getDiaChi());
            newKhachHang.setDiem(0);
            newKhachHang.setTrangThaiKhachHang(0);
            newKhachHang.setPhanLoaiKhachHang(new PhanLoaiKhachHang(Long.valueOf("1"), "", "", 0));
            KhachHang addedKhachHang = khachHangRepository.save(newKhachHang);//Thêm profile khách hàng
            if (addedKhachHang != null) {
                //Nếu thêm profile thành công
                //Chuyển trạng thái tài khoản
                taiKhoan.setTrangThaiTaiKhoan(1); //1 là hoạt động
                taiKhoanRepository.save(taiKhoan);//Lưu lại cho chắc, không dựa vào JPA Cascade
                //Liên kết profile tới tài khoản
                newKhachHang.setTaiKhoan(taiKhoan);
                //Lưu lại sau khi liên kết
                newKhachHang = khachHangRepository.save(newKhachHang);
                if (newKhachHang != null) {
                    return ResponseEntity.ok(newKhachHang);
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo profile tài khoản");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Tên đăng nhập đã tồn tại");
    }

    @Operation(summary = "Cập nhật thông tin khách hàng",
            description = "Cập nhật thông tin khách hàng dựa trên ID khách hàng.")
    @PutMapping("/update/{id}")
    public ResponseEntity updateKhachHang(@Valid @RequestBody KhachHang khachHang, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        ResponseEntity response = RepoUtility.findById(id, khachHangRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            KhachHang toBeUpdated = (KhachHang) response.getBody();
//            toBeUpdated.setMaKhachHang(khachHang.getMaKhachHang());//Không cho SET
            toBeUpdated.setTen(khachHang.getTen());
            toBeUpdated.setTenDem(khachHang.getTenDem());
            toBeUpdated.setHo(khachHang.getHo());
            toBeUpdated.setNgaySinh(khachHang.getNgaySinh());
            toBeUpdated.setSoDienThoai(khachHang.getSoDienThoai());
            toBeUpdated.setGioiTinh(khachHang.getGioiTinh());
            toBeUpdated.setEmail(khachHang.getEmail());
            toBeUpdated.setDanToc(khachHang.getDanToc());
            toBeUpdated.setDiaChi(khachHang.getDiaChi());
//            toBeUpdated.setDiem(khachHang.getDiem());//Không cho SET
//            toBeUpdated.setTrangThaiKhachHang(khachHang.getTrangThaiKhachHang());//Không cho SET

            return ResponseEntity.ok(khachHangRepository.save(toBeUpdated));
        }
        return response;
    }

    @Operation(summary = "Tìm kiếm khách hàng theo ID",
            description = "Tìm kiếm khách hàng bằng ID và trả về thông tin khách hàng.")
    @GetMapping("/find/{id}")
    public ResponseEntity findKhachHangById(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, khachHangRepository);
        return response;
    }

    @Operation(summary = "Tìm kiếm khách hàng theo cột và giá trị",
            description = "Hỗ trợ tìm kiếm khách hàng theo tên cột và giá trị của cột.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy"),
            @ApiResponse(responseCode = "500", description = "Lỗi nội bộ")})
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity findKhachHangBy(@PathVariable String columnName, @PathVariable String value) {
        ResponseEntity response = RepoUtility.findByCustomColumn(khachHangRepository, columnName, value);
        return response;
    }
}