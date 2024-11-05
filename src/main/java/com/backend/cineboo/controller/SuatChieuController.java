package com.backend.cineboo.controller;

import com.backend.cineboo.entity.DoTuoi;
import com.backend.cineboo.entity.Phim;
import com.backend.cineboo.entity.SuatChieu;
import com.backend.cineboo.repository.SuatChieuRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
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
@RequestMapping("/suatchieu")
@OpenAPIDefinition(
        info = @Info(
                title = "SuatChieuAPI",
                version = "0.1",
                description = "CRUD cơ bản cho bảng SuatChieu"
        ))
public class SuatChieuController {
    @Autowired
    SuatChieuRepository suatChieuRepository;
    private final String idPrefix = "MSC00";

    /**
     * Lấy tất cả các suất chiếu.
     * @return Danh sách các đối tượng SuatChieu.
     */
    @Operation(summary = "Lấy tất cả suất chiếu", description = "Truy xuất danh sách tất cả suất chiếu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy suất chiếu thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
    })
    @GetMapping("/get")
    public ResponseEntity<List<SuatChieu>> getAll() {
        List<SuatChieu> suatChieus = suatChieuRepository.findAll();
        return ResponseEntity.ok(suatChieus);
    }

    /**
     * Vô hiệu hóa một suất chiếu cụ thể theo ID.
     *
     * @param id ID suất chiếu.
     * @return ResponseEntity cho biết kết quả của thao tác.
     */
    @Operation(summary = "Vô hiệu hóa một suất chiếu", description = "Vô hiệu hóa suất chiếu cụ thể theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vô hiệu hóa suất chiếu thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ, ID là null"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy suất chiếu")
    })
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, suatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            SuatChieu suatChieu = (SuatChieu) response.getBody();
            suatChieu.setTrangThaiSuatChieu(0);
            suatChieuRepository.save(suatChieu);
            return ResponseEntity.status(HttpStatus.OK).body("Vô hiệu hóa suất chiếu thành công");
        }
        return response;
    }

    /**
     * Cập nhật một suất chiếu hiện có.
     *
     * @param suatChieu Thông tin suất chiếu để cập nhật.
     * @param bindingResult Kết quả xác thực.
     * @param id ID suất chiếu để cập nhật.
     * @return Đối tượng SuatChieu đã cập nhật hoặc phản hồi lỗi.
     */
    @Operation(summary = "Cập nhật một suất chiếu", description = "Cập nhật thông tin của một suất chiếu hiện có")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật suất chiếu thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ, có lỗi xác thực"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy suất chiếu")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody SuatChieu suatChieu, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id,suatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            SuatChieu toBeUpdated = (SuatChieu) response.getBody();
            toBeUpdated.setMaSuatChieu(idPrefix + suatChieuRepository.getMaxTableId());
            toBeUpdated.setPhongChieu(suatChieu.getPhongChieu());
            toBeUpdated.setThoiGianChieu(suatChieu.getThoiGianChieu());
            toBeUpdated.setTrangThaiSuatChieu(suatChieu.getTrangThaiSuatChieu());
            toBeUpdated.setPhim(suatChieu.getPhim());
            return ResponseEntity.status(HttpStatus.OK).body(suatChieuRepository.save(toBeUpdated));
        }
        return response;
    }

    /**
     * Thêm suất chiếu mới.
     *
     * @param suatChieu Thông tin suất chiếu mới.
     * @param bindingResult Kết quả xác thực.
     * @return SuatChieu đã thêm hoặc phản hồi lỗi.
     */
    @Operation(summary = "Thêm suất chiếu mới", description = "Thêm một suất chiếu mới vào hệ thống")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thêm suất chiếu thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ, có lỗi xác thực")
    })
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody SuatChieu suatChieu, BindingResult bindingResult) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        suatChieu.setMaSuatChieu(idPrefix + (suatChieuRepository.getMaxTableId() + 1));
        return ResponseEntity.status(HttpStatus.OK).body(suatChieuRepository.save(suatChieu));
    }

    /**
     * Tìm kiếm suất chiếu theo ID.
     *
     * @param id ID suất chiếu.
     * @return SuatChieu nếu tìm thấy hoặc phản hồi lỗi.
     */
    @Operation(summary = "Tìm suất chiếu theo ID", description = "Tìm suất chiếu theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm suất chiếu thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ, ID là null"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy suất chiếu")
    })
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id,suatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(suatChieuRepository.save((SuatChieu) response.getBody()));
        }
        return response;
    }

    /**
     * Tìm kiếm suất chiếu theo tên cột và giá trị của cột.
     *
     * @param columnName Tên cột để tìm kiếm.
     * @param value Giá trị để tìm kiếm.
     * @return ResponseEntity với kết quả tìm kiếm.
     */
    @Operation(summary = "Tìm kiếm suất chiếu theo cột", description = "Tìm suất chiếu theo tên cột và giá trị của cột")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm kiếm thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ, cột không tồn tại"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bản ghi"),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
    })
    @GetMapping("find/{columnName}/{value}")
    public ResponseEntity findBy(@PathVariable String columnName, @PathVariable String value) {
        ResponseEntity response = RepoUtility.findByCustomColumn(suatChieuRepository, columnName, value);
        return response;
    }

}
