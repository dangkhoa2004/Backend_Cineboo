package com.backend.cineboo.controller;

import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.entity.Voucher;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/availability/{id}")
    public ResponseEntity isVoucherAvailable(@PathVariable Long voucherId) {
        Voucher voucher = voucherRepository.checkAvailabilityByMaVoucher(voucherId).orElse(null);
        if (voucher != null) {
            return ResponseEntity.ok(voucher);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voucher không khả dụng");
    }

    public BigDecimal applyVoucher(Long voucherId, HoaDon hoadon) {
        ResponseEntity response = isVoucherAvailable(voucherId);
        if (response.getStatusCode().is2xxSuccessful()) {
            Voucher voucher = (Voucher) response.getBody();
            BigDecimal originalPrice = hoadon.getTongSoTien();
            BigDecimal finalPrice = BigDecimal.ZERO;

            if (voucher.getTruTienPhanTram() != null) {
                BigDecimal discountPercentage = BigDecimal.valueOf(voucher.getTruTienPhanTram()).divide(BigDecimal.valueOf(100)); // Assuming it is a percentage
                BigDecimal discountAmount = originalPrice.multiply(discountPercentage);
                finalPrice = finalPrice.subtract(discountAmount);
            } else if (voucher.getTruTienSo() != null) {
                BigDecimal absoluteDiscount = voucher.getTruTienSo();
                finalPrice = finalPrice.subtract(absoluteDiscount);
            } else {
                return null;
            }
                // Ensure the final price does not go below zero
                if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
                    finalPrice = BigDecimal.ZERO;
                }

                return finalPrice ; // Return the calculated final price
            }
        return null;
    }

}
