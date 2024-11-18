package com.backend.cineboo.controller;

import com.backend.cineboo.entity.Ghe;
import com.backend.cineboo.entity.PhongChieu;
import com.backend.cineboo.repository.GheRepository;
import com.backend.cineboo.repository.PhongChieuRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import jakarta.validation.Valid;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Controller
@RequestMapping("/phongchieu")
public class PhongChieuController {

    @Autowired
    PhongChieuRepository phongChieuRepository;

    @Operation(summary = "Lấy danh sách phòng chiếu",
            description = "Trả về danh sách tất cả phòng chiếu trong hệ thống.")
    @GetMapping("/get")
    public ResponseEntity<List<PhongChieu>> get(){
        return ResponseEntity.ok((List<PhongChieu>)phongChieuRepository.findAll());
    }

    @Operation(summary = "Vô hiệu hóa phòng chiếu",
            description = "Đặt trạng thái phòng chiếu thành không khả dụng bằng cách sử dụng ID.")
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, phongChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            PhongChieu phongChieu = (PhongChieu) response.getBody();
            phongChieuRepository.save(phongChieu);
            return ResponseEntity.status(HttpStatus.OK).body("Disable phòng chiếu thành công");
        }
        return response;
    }

    @Operation(summary = "Thêm phòng chiếu mới",
            description = "Thêm một phòng chiếu mới vào hệ thống\n\n" +
                    "Mã phòng chiếu tự tăng\n\n" +
                    "Trạng thái mặc định là 0\n\n" +
                    "ID mặc định là null\n\n"+
                    "Tổng số ghế lấy từ request")
    @PutMapping("/add")
    public ResponseEntity add(@Valid @RequestBody PhongChieu phongChieu, BindingResult bindingResult){
        Map<String,String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        String prefix = "PC00";
        phongChieu.setMaPhong(prefix+phongChieuRepository.getMaxTableId());//Tự tăng
        phongChieu.setId(null);//To make sure its an INSERT and Not Update since both use save()
        phongChieu.setTrangThaiPhongChieu(0);
        phongChieu = phongChieuRepository.save(phongChieu);
        return ResponseEntity.ok(phongChieu);
    }

    @Operation(summary = "Cập nhật thông tin phòng chiếu",
            description = "Cập nhật thông tin phòng chiếu dựa trên ID phòng chiếu.")
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody PhongChieu phongChieu, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, phongChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            PhongChieu toBeUpdated = (PhongChieu) response.getBody();
            toBeUpdated.setTongSoGhe(phongChieu.getTongSoGhe());
            toBeUpdated.setMaPhong(phongChieu.getMaPhong());
            toBeUpdated.setTrangThaiPhongChieu(phongChieu.getTrangThaiPhongChieu());
            //Lưu vào Database
            toBeUpdated = phongChieuRepository.save(toBeUpdated);
            return ResponseEntity.status(HttpStatus.OK).body(toBeUpdated);
        }
        return response;
    }

    @Operation(summary = "Tìm kiếm phòng chiếu theo ID",
            description = "Tìm kiếm phòng chiếu bằng ID và trả về thông tin phòng chiếu.")
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, phongChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(phongChieuRepository.save((PhongChieu) response.getBody()));
        }
        return response;
    }
}
