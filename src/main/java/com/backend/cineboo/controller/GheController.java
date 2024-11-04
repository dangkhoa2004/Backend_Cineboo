package com.backend.cineboo.controller;

import com.backend.cineboo.dto.GheWithoutSuatChieuId;
import com.backend.cineboo.entity.Ghe;
import com.backend.cineboo.repository.GheRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ghe")
public class GheController {
    @Autowired
    GheRepository gheRepository;



    @GetMapping("/get")
    public ResponseEntity<List<Ghe>> get(){
        List<Ghe> ghes = new ArrayList<>();
        ghes = gheRepository.findAll();
        return ResponseEntity.ok(ghes);
    }
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, gheRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Ghe ghe = (Ghe) response.getBody();
            ghe.setTrangThaiGhe(0);
            gheRepository.save(ghe);
            return ResponseEntity.status(HttpStatus.OK).body("Disable Ghe thành công");
        }
        return response;
    }
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody Ghe ghe, BindingResult bindingResult){
        Map<String,String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Ghe addedGhe = gheRepository.save(ghe);
        return ResponseEntity.ok(gheRepository.save(addedGhe));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody Ghe ghe, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, gheRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Ghe toBeUpdated = (Ghe) response.getBody();
            toBeUpdated.setMaGhe(ghe.getMaGhe());
            toBeUpdated.setGiaTien(ghe.getGiaTien());
            toBeUpdated.setPhongChieu(ghe.getPhongChieu());
            toBeUpdated.setTrangThaiGhe(ghe.getTrangThaiGhe());
            //Lưu vào Database
            return ResponseEntity.status(HttpStatus.OK).body(gheRepository.save(toBeUpdated));
        }
        return response;
    }
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, gheRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Ghe ghe = (Ghe) response.getBody();
            return ResponseEntity.ok(gheRepository.save(ghe));
        }
        return response;
    }
    /**
     * Tìm kiếm Danh sách Ghế theo ID_Phim và ThoiGianChieu
     * Maybe....?
     * @param  phimId
     * @param  thoiGianChieu
     * @return Trả về ResponseEntity(200) nếu thành  công
     * Trả về ResponseEntity(badRequest) nếu cột không tồn tại
     * Trả về ResponseEntity(notFound) nếu bản ghi không tồn tại
     * Trả về ResponseEntity(INTERNAL_SERVER_ERROR) nếu lỗi khác
     */
    @GetMapping("find/ID_PhimAndThoiGianChieu")
    public ResponseEntity findByPhimIdAndThoiGianChieu(@RequestParam("ID_Phim") String phimId, @RequestParam("ThoiGianChieu") String thoiGianChieu) {
        List<GheWithoutSuatChieuId> ghes = gheRepository.findByIdPhimAndThoiGianChieu(phimId,thoiGianChieu);

        System.out.println(ghes);
        return ResponseEntity.ok(ghes);
    }
    /**
     * Hỗ trợ tìm kiểm theo tên cột và giá trị của cột
     *
     * @param columnName
     * @param value
     * @return Trả về ResponseEntity(200) nếu thành  công
     * Trả về ResponseEntity(badRequest) nếu cột không tồn tại
     * Trả về ResponseEntity(notFound) nếu bản ghi không tồn tại
     * Trả về ResponseEntity(INTERNAL_SERVER_ERROR) nếu lỗi khác
     */
    @Operation(summary = "Tìm kiếm phim theo cú pháp findBy+columnName",
            description = "Tìm kiếm theo columnName và value của column đó. Prefix findBy sẽ tự động được thêm vào\n\n" +
                    "*Lưu ý: Chỉ tìm theo các phương thức đã có sẵn hoặc người dùng tự tạo trong Repository\n\n" +
                    "Ví dụ: localhost:8080/find/Something/2\n\n" +
                    "Sẽ gọi tới Repo: whateverMethodType findBySomething(String value)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity findBy(@PathVariable String columnName, @PathVariable String value) {
        ResponseEntity response = RepoUtility.findByCustomColumn(gheRepository, columnName, value);
        return response;
    }
}

