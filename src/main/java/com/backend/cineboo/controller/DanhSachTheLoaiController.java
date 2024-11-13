package com.backend.cineboo.controller;

import com.backend.cineboo.entity.DanhSachTLPhim;
import com.backend.cineboo.repository.DanhSachTLPhimReposiory;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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
@OpenAPIDefinition(
        info = @Info(
                title = "DanhSachTLPhimAPI",
                version = "0.1",
                description = "CRUD cơ bản cho bảng DanhSachTLPhim"
        ))
@RequestMapping("/dstl")
public class DanhSachTheLoaiController {

    @Autowired
    DanhSachTLPhimReposiory danhSachTLPhimReposiory;

    @Operation(summary = "Lấy danh sách thể loại phim",
            description = "Trả về danh sách tất cả các thể loại phim.")
    @GetMapping("/get")
    public ResponseEntity<List<DanhSachTLPhim>> get(){
        return ResponseEntity.ok((List<DanhSachTLPhim>)danhSachTLPhimReposiory.findAll());
    }

    @Operation(summary = "Vô hiệu hóa thể loại phim",
            description = "Vô hiệu hóa thể loại phim theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vô hiệu hóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại phim")
    })
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, danhSachTLPhimReposiory);
        if (response.getStatusCode().is2xxSuccessful()) {
            DanhSachTLPhim danhSachTLPhim = (DanhSachTLPhim) response.getBody();
            danhSachTLPhim.setTrangThai(0);
            danhSachTLPhimReposiory.save(danhSachTLPhim);
            return ResponseEntity.status(HttpStatus.OK).body("Disable danhsachtheloai thành công");
        }
        return response;
    }

    @Operation(summary = "Thêm thể loại phim",
            description = "Thêm một thể loại phim mới.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thêm thể loại phim thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    @PutMapping("/add")
    public ResponseEntity add(@Valid @RequestBody DanhSachTLPhim danhSachTLPhim, BindingResult bindingResult){
        Map<String,String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        danhSachTLPhim.setId(null);//To make sure its an INSERT and Not Update since both use save()
        return ResponseEntity.ok(danhSachTLPhimReposiory.save(danhSachTLPhim));
    }

    @Operation(summary = "Cập nhật thể loại phim",
            description = "Cập nhật thông tin của thể loại phim theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại phim")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody DanhSachTLPhim danhSachTLPhim, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, danhSachTLPhimReposiory);
        if (response.getStatusCode().is2xxSuccessful()) {
            DanhSachTLPhim toBeUpdated = (DanhSachTLPhim) response.getBody();
            toBeUpdated.setTheLoaiPhim(danhSachTLPhim.getTheLoaiPhim());
            toBeUpdated.setPhim(danhSachTLPhim.getPhim());
            toBeUpdated.setTrangThai(danhSachTLPhim.getTrangThai());
            //Lưu vào Database
            return ResponseEntity.status(HttpStatus.OK).body(danhSachTLPhimReposiory.save(toBeUpdated));
        }
        return response;
    }

    @Operation(summary = "Tìm kiếm thể loại phim theo ID",
            description = "Tìm kiếm thể loại phim theo ID và trả về thông tin chi tiết.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm thấy thể loại phim"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại phim")
    })
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, danhSachTLPhimReposiory);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(danhSachTLPhimReposiory.save((DanhSachTLPhim) response.getBody()));
        }
        return response;
    }
}
