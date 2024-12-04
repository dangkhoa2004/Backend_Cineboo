package com.backend.cineboo.controller;

import com.backend.cineboo.dto.AddGheAndSuatChieuDTO;
import com.backend.cineboo.entity.Ghe;
import com.backend.cineboo.entity.GheAndSuatChieu;
import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.entity.SuatChieu;
import com.backend.cineboo.repository.GheAndSuatChieuRepository;
import com.backend.cineboo.repository.GheRepository;
import com.backend.cineboo.repository.PhongChieuRepository;
import com.backend.cineboo.repository.SuatChieuRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Controller

@RequestMapping("/ghe-suat-chieu")
public class GheAndSuatChieuController {

    @Autowired
    GheRepository gheRepository;

    @Autowired
    SuatChieuRepository suatChieuRepository;


    @Autowired
    private GheAndSuatChieuRepository gheAndSuatChieuRepository;

    @Operation(summary = "Lấy danh sách Ghế và Suất Chiếu",
            description = "Trả về danh sách tất cả Ghế và Suất Chiếu.")
    @GetMapping("/get")
    public ResponseEntity<List<GheAndSuatChieu>> getAll() {
        return ResponseEntity.ok(gheAndSuatChieuRepository.findAll());
    }

    @Operation(summary = "Thêm Ghế và Suất Chiếu",
            description = "Thêm một bản ghi mới vào bảng Ghế và Suất Chiếu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thêm thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody AddGheAndSuatChieuDTO gheAndSuatChieuDTO, BindingResult bindingResult) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Long suatChieuId = gheAndSuatChieuDTO.getId_SuatChieu();
        SuatChieu suatChieu = suatChieuRepository.findById(suatChieuId).orElse(null);
        if(suatChieu==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy suất chiếu");
        }
        List<Ghe> ghes = gheRepository.findByID_PhongChieu(gheAndSuatChieuDTO.getId_PhongChieu().toString());
        for (Ghe ghe : ghes) {
            //Add ghe to GheAndSuatChieu
            GheAndSuatChieu gheAndSuatChieu = new GheAndSuatChieu();
            gheAndSuatChieu.setId_Ghe(ghe);
            gheAndSuatChieu.setId_SuatChieu(suatChieu);
            gheAndSuatChieu.setTrangThaiGheAndSuatChieu(0);
            gheAndSuatChieuRepository.save(gheAndSuatChieu);
        }
        List<Ghe> queriedGheBySuatChieu = gheRepository.findByID_SuatChieu(suatChieuId.toString());
        return ResponseEntity.status(HttpStatus.OK).body(queriedGheBySuatChieu);
    }

    @Operation(summary = "Cập nhật Ghế và Suất Chiếu",
            description = "Cập nhật thông tin theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bản ghi")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody GheAndSuatChieu gheAndSuatChieu, BindingResult bindingResult, @PathVariable Long id) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, gheAndSuatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            GheAndSuatChieu toBeUpdated = (GheAndSuatChieu) response.getBody();
            toBeUpdated.setId_Ghe(gheAndSuatChieu.getId_Ghe());
            toBeUpdated.setId_SuatChieu(gheAndSuatChieu.getId_SuatChieu());
            toBeUpdated.setTrangThaiGheAndSuatChieu(gheAndSuatChieu.getTrangThaiGheAndSuatChieu());
            return ResponseEntity.ok(gheAndSuatChieuRepository.save(toBeUpdated));
        }
        return response;
    }


    @Operation(summary = "Thay đổi trạng thái hoá gheAndSuatChieu",
            description =
                    "Thay đổi trạng thái gheAndSuatChieu theo giá trị truyền vào\n\n" +
                            "Tuy nhiên, do trạng thái ghế được quản lý bằng QuartzScheduler\n\n" +
                            "nên không nên đặt thủ công để tránh lỗi\n\n" +
                            "0: Ghế available\n\n" +
                            "1: Ghế đã book\n\n" +
                            "2: Ghế đang chờ\n\n" +
                            "Các trạng thái khác sẽ báo lỗi và không update")
    @PutMapping("/status/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Lỗi ID")})
    public ResponseEntity setStatus(@PathVariable Long id, @RequestParam("trangThai") Integer trangThai) {
        ResponseEntity response = RepoUtility.findById(id, gheAndSuatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            GheAndSuatChieu gheAndSuatChieu = (GheAndSuatChieu) response.getBody();
            String newStatus;
            switch (trangThai) {
                case 0:
                    newStatus = "Ghế available";
                    break;
                case 1:
                    newStatus = "Ghế đã book";
                    break;
                case 2:
                    newStatus = "Ghế đang chờ";
                    break;
                default:
                    newStatus = "Trạng thái không xác định";
                    return ResponseEntity.status(HttpStatus.OK).body("Không trạng thái Ghế: " + newStatus);
            }
            gheAndSuatChieu.setTrangThaiGheAndSuatChieu(trangThai);
            return ResponseEntity.status(HttpStatus.OK).body("Đặt trạng thái Ghế: " + newStatus);
        }
        return response;
    }

    @Operation(summary = "Tìm Ghế và Suất Chiếu theo ID",
            description = "Trả về thông tin chi tiết theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm thấy bản ghi"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bản ghi")
    })
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, gheAndSuatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        }
        return response;
    }


    @Operation(summary = "Tìm theo repo",
            description = "Tìm theo các cột đã hỗ trợ trong repo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm thấy bản ghi"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bản ghi")
    })
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity find(@PathVariable String columnName,@PathVariable String value) {
        ResponseEntity response = RepoUtility.findByCustomColumn(gheAndSuatChieuRepository,columnName,value);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        }
        return response;
    }
}
