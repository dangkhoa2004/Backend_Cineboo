package com.backend.cineboo.controller;

import com.backend.cineboo.dto.NhanVienRegister;
import com.backend.cineboo.dto.NhanVienRegister;
import com.backend.cineboo.entity.*;
import com.backend.cineboo.repository.ChucVuRepository;
import com.backend.cineboo.repository.NhanVienRepository;
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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/nhanvien")
public class NhanVienController {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private ChucVuRepository chucVuRepository;

    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees.")
    @GetMapping("/get")
    public ResponseEntity<List<NhanVien>> getAllNhanVien() {
        List<NhanVien> nhanViens = nhanVienRepository.findAll();
        return ResponseEntity.ok(nhanViens);
    }

    @Operation(summary = "Add a new employee", description = "Add a new employee to the system.")
    @PostMapping("/add")
    public ResponseEntity addNhanVien(@Valid @RequestBody NhanVienRegister nhanVienRegister, BindingResult bindingResult,
                                      @RequestParam("username") String username,
                                      @RequestParam("password") String password) {
        String inputUsername = username.trim();
        String inputPassword = password.trim();

        if (StringUtils.isEmpty(inputPassword) || StringUtils.isEmpty(inputUsername)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không xác định được thông tin đăng nhập");
        }
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        //Create new TaiKhoan
        TaiKhoan taiKhoan = new TaiKhoan();
        Integer row = taiKhoanRepository.checkAvailable(inputUsername).orElse(null);
        if (row == null) {
            taiKhoan.setTenDangNhap(inputUsername);
            taiKhoan.setMatKhau(BCrypt.hashpw(inputPassword, BCrypt.gensalt()));
            taiKhoan.setGhiChu("");//None
            taiKhoan.setOtp("");//None
            taiKhoan.setPhanLoaiTaiKhoan(new PhanLoaiTaiKhoan(Long.valueOf("1"), "NhanVien", 0));//Khách hàng = 2
            taiKhoan.setTrangThaiTaiKhoan(0);//Chưa kích hoạt
            taiKhoanRepository.save(taiKhoan);//Tạo mới tài khoản
            String prefix = "NV00";
            //Create new NhanVien
            NhanVien newNhanVien = new NhanVien();
            String maNhanVien = String.valueOf(nhanVienRepository.getMaxTableId() + 1);
            newNhanVien.setMaNhanVien(prefix+maNhanVien);
            newNhanVien.setTen(nhanVienRegister.getTen());
            newNhanVien.setTenDem(nhanVienRegister.getTenDem());
            newNhanVien.setHo(nhanVienRegister.getHo());
            newNhanVien.setNgaySinh(nhanVienRegister.getNgaySinh());
            newNhanVien.setGioiTinh(nhanVienRegister.getGioiTinh());
            newNhanVien.setEmail(nhanVienRegister.getEmail());
            newNhanVien.setDanToc(nhanVienRegister.getDanToc());
            newNhanVien.setDiaChi(nhanVienRegister.getDiaChi());


            Long idChucVu = nhanVienRegister.getIdChucVu();
            PhanLoaiChucVu chucVu = chucVuRepository.findById(idChucVu).get();
            newNhanVien.setChucVu(chucVu) ;
           
            NhanVien addedNhanVien = nhanVienRepository.save(newNhanVien);//Thêm profile khách hàng
            if (addedNhanVien != null) {
                //Nếu thêm profile thành công
                //Chuyển trạng thái tài khoản
                taiKhoan.setTrangThaiTaiKhoan(1); //1 là hoạt động
                taiKhoanRepository.save(taiKhoan);//Lưu lại cho chắc, không dựa vào JPA Cascade
                //Liên kết profile tới tài khoản
                newNhanVien.setTaiKhoan(taiKhoan);
                newNhanVien.setTrangThai(1);
                //Lưu lại sau khi liên kết
                newNhanVien = nhanVienRepository.save(newNhanVien);
                if (newNhanVien != null) {
                    return ResponseEntity.ok(newNhanVien);
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo profile tài khoản");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Tên đăng nhập đã tồn tại");
    }


    @Operation(summary = "Vô hiệu hóa nhân viên",
            description = "Vô hiệu hóa nhân viên\n\n" +
                    "Và tài khoản đi kèm(cái này chưa test).")
    @PutMapping("/disable/{id_NhanVien}")
    public ResponseEntity disable(@PathVariable Long id_NhanVien){
        ResponseEntity response = RepoUtility.findById(id_NhanVien,nhanVienRepository);
        if(response.getStatusCode().is2xxSuccessful()){
            NhanVien nhanVien = (NhanVien) response.getBody();

            TaiKhoan taiKhoan = nhanVien.getTaiKhoan();
            taiKhoan.setTrangThaiTaiKhoan(0);
            taiKhoanRepository.save(taiKhoan);

            nhanVien.setTrangThai(0);//Only need this
            nhanVienRepository.save(nhanVien);
            return ResponseEntity.ok("Disable thành công");
        }
        return response;
    }


    @Operation(summary = "Update employee information", description = "Update employee details by ID.")
    @PutMapping("/update/{id}")
    public ResponseEntity updateNhanVien(@Valid @RequestBody NhanVien nhanVien, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        ResponseEntity response = RepoUtility.findById(id, nhanVienRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            NhanVien toBeUpdated = (NhanVien) response.getBody();
            toBeUpdated.setTen(nhanVien.getTen());
            toBeUpdated.setTenDem(nhanVien.getTenDem());
            toBeUpdated.setHo(nhanVien.getHo());
            toBeUpdated.setNgaySinh(nhanVien.getNgaySinh());
            toBeUpdated.setGioiTinh(nhanVien.getGioiTinh());
            toBeUpdated.setEmail(nhanVien.getEmail());
            toBeUpdated.setDanToc(nhanVien.getDanToc());
            toBeUpdated.setDiaChi(nhanVien.getDiaChi());
            return ResponseEntity.ok(nhanVienRepository.save(toBeUpdated));
        }
        return response;
    }

    @Operation(summary = "Find employee by ID", description = "Retrieve employee details by ID.")
    @GetMapping("/find/{id}")
    public ResponseEntity findNhanVienById(@PathVariable Long id) {
        return RepoUtility.findById(id, nhanVienRepository);
    }

    @Operation(summary = "Find employee by column and value", description = "Search employees by a column and value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity found"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal error")})
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity findNhanVienBy(@PathVariable String columnName, @PathVariable String value) {
        return RepoUtility.findByCustomColumn(nhanVienRepository, columnName, value);
    }
}
