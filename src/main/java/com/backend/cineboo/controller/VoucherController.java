package com.backend.cineboo.controller;

import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.entity.KhachHang;
import com.backend.cineboo.entity.KhoQua;
import com.backend.cineboo.entity.Voucher;
import com.backend.cineboo.repository.KhachHangRepository;
import com.backend.cineboo.repository.KhoQuaReposiory;
import com.backend.cineboo.repository.VoucherRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/voucher")
@OpenAPIDefinition(
        info = @Info(
                title = "VoucherAPI",
                version = "0.1",
                description = "CRUD operations for Voucher entity"
        ))
public class VoucherController {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private KhoQuaReposiory khoQuaReposiory;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Operation(summary = "Get all vouchers",
            description = "Returns a list of all vouchers in the system.")
    @GetMapping("/get")
    public ResponseEntity<List<Voucher>> get() {
        List<Voucher> vouchers = new ArrayList<>(voucherRepository.findAll());
        return ResponseEntity.ok(vouchers);
    }

    @Operation(summary = "Disable a voucher",
            description = "Sets the voucher status to disabled (trangThaiVoucher = 0) by ID.")
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, voucherRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Voucher voucher = (Voucher) response.getBody();
            voucher.setTrangThaiVoucher(0);
            voucherRepository.save(voucher);
            return ResponseEntity.status(HttpStatus.OK).body("Voucher disabled successfully");
        }
        return response;
    }

    @Operation(summary = "Add a new voucher",
            description = "Adds a new voucher to the system.")
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody Voucher voucher, BindingResult bindingResult) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (voucher.getTruTienSo() == null && voucher.getTruTienPhanTram() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TruTienSo & TruTienPhanTram không thể cùng null");
        }
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Voucher exist = voucherRepository.findByMaVoucher(voucher.getMaVoucher()).orElse(null);
        if (exist == null) {
            return ResponseEntity.ok(voucherRepository.save(voucher));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Mã voucher đã tồn tại");
    }

    @Operation(summary = "Update voucher details",
            description = "Updates a voucher's details based on its ID.")
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody Voucher voucher, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        if (voucher.getTruTienSo() == null && voucher.getTruTienPhanTram() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TruTienSo & TruTienPhanTram không thể cùng null");
        }
        ResponseEntity response = RepoUtility.findById(id, voucherRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Voucher toBeUpdated = (Voucher) response.getBody();
            toBeUpdated.setMaVoucher(voucher.getMaVoucher());
            toBeUpdated.setGiaTriDoi(voucher.getGiaTriDoi());
            toBeUpdated.setTruTienPhanTram(voucher.getTruTienPhanTram());
            toBeUpdated.setTruTienSo(voucher.getTruTienSo());
            toBeUpdated.setSoTienToiThieu(voucher.getSoTienToiThieu());
            toBeUpdated.setGiamToiDa(voucher.getGiamToiDa());
            toBeUpdated.setNgayBatDau(voucher.getNgayBatDau());
            toBeUpdated.setNgayKetThuc(voucher.getNgayKetThuc());
            toBeUpdated.setTrangThaiVoucher(voucher.getTrangThaiVoucher());
            return ResponseEntity.status(HttpStatus.OK).body(voucherRepository.save(toBeUpdated));
        }
        return response;
    }

    @Operation(summary = "Find voucher by ID",
            description = "Finds a voucher by its ID.")
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, voucherRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        }
        return response;
    }

    @Operation(summary = "Find voucher by column and value",
            description = "Supports searching by column name and value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal error")})
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity findBy(@PathVariable String columnName, @PathVariable String value) {
        return RepoUtility.findByCustomColumn(voucherRepository, columnName, value);
    }


    @Operation(summary = "Kiểm tra tính khả dụng của voucher",
            description = "Kiểm tra tinh khả dụng dựa trên số lượng, thời hạn, Tính sẵn có.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal error")})
    @GetMapping("/availability/{id}")
    public ResponseEntity isVoucherAvailable(@PathVariable String maVoucher) {
        Voucher voucher = voucherRepository.checkAvailabilityByMaVoucher(maVoucher).orElse(null);
        if (voucher != null) {
            return ResponseEntity.ok(voucher);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voucher không khả dụng");
    }

    public BigDecimal getDiscountAmount(String maVoucher, HoaDon hoadon) {

        Voucher voucher = voucherRepository.checkAvailabilityByMaVoucher(maVoucher).orElse(null);
        if (voucher == null) {
            return null;
        }
        //Already checked by SQL Query
        //But this is not a DB Management Program
        //This is a Java application
        //We don't trust those filthy DB queries here on important things like money
        BigDecimal minimum = voucher.getSoTienToiThieu();
        BigDecimal maximum = voucher.getGiamToiDa();
        int soLuong = voucher.getSoLuong();
        int trangThai = voucher.getTrangThaiVoucher();
        LocalDate now = LocalDate.now();
        LocalDate start = voucher.getNgayBatDau();
        LocalDate end = voucher.getNgayKetThuc();


        BigDecimal originalPrice = hoadon.getTongSoTien();
        BigDecimal finalPrice = BigDecimal.ZERO;

        if (originalPrice.compareTo(minimum) < 0
                || soLuong <= 0
                || now.isAfter(end)
                || now.isBefore(start)
                || trangThai == 0
        ){
            return null;
        }
            if (voucher.getTruTienPhanTram() != null) {
                BigDecimal discountPercentage = BigDecimal.valueOf(voucher.getTruTienPhanTram()).divide(BigDecimal.valueOf(100)); // Assuming it is a percentage
                BigDecimal discountAmount = originalPrice.multiply(discountPercentage);
                if(discountAmount.compareTo(maximum)>0){
                    discountAmount=maximum;
                }
                finalPrice = originalPrice.subtract(discountAmount);
            } else if (voucher.getTruTienSo() != null) {
                BigDecimal absoluteDiscount = voucher.getTruTienSo();
                finalPrice = originalPrice.subtract(absoluteDiscount);
            } else {
                return null;
            }
        // Ensure the final price does not go below zero
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.ZERO;
        }

        return finalPrice; // Return the calculated final price
    }

    public void decreaseVoucherCount(String maVoucher){
       Voucher voucher= voucherRepository.checkAvailabilityByMaVoucher(maVoucher).orElse(null);
       if(voucher.getSoLuong()>0){
           //Duplicate logic from sql and java, i know
           //But what if SQL query fuck itself up, what then?
           voucher.setSoLuong(voucher.getSoLuong()-1);
           voucherRepository.save(voucher);
       }
    }
    public void increaseVoucherCount(String maVoucher){
        Voucher voucher= voucherRepository.checkAvailabilityByMaVoucher(maVoucher).orElse(null);
        if(voucher!=null){
            //Duplicate logic from sql and java, i know
            //But what if SQL query fuck itself up, what then?
            voucher.setSoLuong(voucher.getSoLuong()+1);
            voucherRepository.save(voucher);
        }
    }

    @PutMapping("/exchange/{khachHangId}")
    public ResponseEntity exchangePointsForVoucher(@PathVariable Long khachHangId, @RequestParam String maVoucher) {
        if (StringUtils.isEmpty(maVoucher)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thiếu mã voucher");
        }
        Voucher voucher = voucherRepository.checkAvailabilityByMaVoucher(maVoucher).orElse(null);
        if (voucher == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại voucher");
        }
        KhoQua khoQua = new KhoQua();
        khoQua.setVoucher(voucher);
        ResponseEntity response = RepoUtility.findById(khachHangId, khachHangRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            //Lưu vào kho quà
            KhachHang khachHang = (KhachHang) response.getBody();
            khoQua.setKhachHang(khachHang);
            khoQua = khoQuaReposiory.save(khoQua);
            decreaseVoucherCount(maVoucher);
            return ResponseEntity.status(HttpStatus.OK).body(khoQua);
        }
        return response;

    }
}
