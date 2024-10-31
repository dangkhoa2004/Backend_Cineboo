package com.backend.cineboo.controller;

import com.backend.cineboo.entity.DoTuoi;
import com.backend.cineboo.entity.Phim;
import com.backend.cineboo.entity.SuatChieu;
import com.backend.cineboo.entity.SuatChieu;
import com.backend.cineboo.repository.SuatChieuRepository;
import com.backend.cineboo.repository.SuatChieuRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import jakarta.validation.Valid;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/suatchieu")
public class SuatChieuController {
    @Autowired
    SuatChieuRepository suatChieuRepository;
    private final String idPrefix = "MSC00";

    /**
     *
     * @return Trả về danh sách suatChieu.
     * Trả về một danh sách kiểu List với size = 0 nếu thất bại
     */
    @GetMapping("/get")
    public ResponseEntity<List<SuatChieu>> getAll() {
        List<SuatChieu> suatChieus = suatChieuRepository.findAll();
        return ResponseEntity.ok(suatChieus);
    }

    /**
     * Đặt trạng thái Suat Chieu bằng 0.
     *
     * @param id
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu suatChieu không tồn tại.
     * Trả về ResponseEntity.OK(SuatChieu) nếu thành công
     */
    //0:Disable
    //1:Enable
    //Yêu cầu cần có sự thống nhất rõ ràng
    //Vì không tách bảng trạng thai
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, suatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            SuatChieu suatChieu = (SuatChieu) response.getBody();
            suatChieu.setTrangThai(0);
            suatChieuRepository.save(suatChieu);
            return ResponseEntity.status(HttpStatus.OK).body("Disable SuatChieu thành công");
        }
        return response;
    }

    /**
     * @param suatChieu
     * @param bindingResult
     * @param id
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu suatChieu không tồn tại.
     * Trả về ResponseEntity.OK(SuatChieu) nếu thành công
     */
    //Receive valid SuatChieu Object from FrontEnd(from a Form or something)
    //And then perform save() right away
    //Instead of creating new instance of SuatChieu and setting each field
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
             toBeUpdated.setTrangThai(suatChieu.getTrangThai());
             toBeUpdated.setPhim(suatChieu.getPhim());
            return ResponseEntity.status(HttpStatus.OK).body(suatChieuRepository.save(toBeUpdated));
        }
        return response;
    }


    /**
     * Thêm suatChieu mới,
     * Mã suatChieu bằng MVS00+ID mới nhất của bảng SuatChieu
     * Tạo bản ghi SuatChieu trước và lưu
     * Sau đó, tạo các bản ghi của DanhSachTLSuatChieu
     * Với ID SuatChieu vừa tạo
     * Và ID TheLoaiSuatChieu có được từ SuatChieu.DanhSachTLSuatChieu.TheLoaiSuatChieu.id
     *
     * @param suatChieu
     * @param bindingResult
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu suatChieu không tồn tại.
     * Trả về ResponseEntity.OK(SuatChieu) nếu thành công
     */
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
     * Tìm kếm suatChieu theo ID
     *
     * @param id
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu suatChieu không tồn tại.
     * Trả về ResponseEntity.ok(SuatChieu) nếu thành công
     */
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id,suatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(suatChieuRepository.save((SuatChieu) response.getBody()));
        }
        return response;
    }


    /**
     * Hỗ trợ tìm kiểm theo tên cột và giá trị của cột theo custsom Query findBy + columnName
     * @param  columnName
     * @param  value
     * @return Trả về ResponseEntity(200) nếu thành  công
     * Trả về ResponseEntity(badRequest) nếu cột không tồn tại
     * Trả về ResponseEntity(notFound) nếu bản ghi không tồn tại
     * Trả về ResponseEntity(INTERNAL_SERVER_ERROR) nếu lỗi khác
     */
    @GetMapping("/{columnName}/{value}")
    public ResponseEntity findBy(@PathVariable String columnName, @PathVariable String value) {
        ResponseEntity response = RepoUtility.findByCustomColumn(suatChieuRepository, columnName, value);
        return response;
    }

}
