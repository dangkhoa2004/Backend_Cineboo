package com.backend.cineboo.controller;

import com.backend.cineboo.entity.DanhSachTLPhim;
import com.backend.cineboo.entity.DoTuoi;
import com.backend.cineboo.entity.Phim;
import com.backend.cineboo.repository.DanhSachTLPhimReposiory;
import com.backend.cineboo.repository.PhimRepository;
import com.backend.cineboo.repository.TheLoaiPhimRepository;
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

@Controller
@RequestMapping("/phim")
public class PhimController {
    @Autowired
    PhimRepository phimRepository;

    @Autowired
    TheLoaiPhimRepository theLoaiPhimRepository;

    @Autowired
    DanhSachTLPhimReposiory danhSachTLPhimReposiory;


    /**
     * Hiển thị danh sách phim
     *
     * @return Trả về danh sách phim.
     * Trả về một danh sách kiểu List với size = 0 nếu thất bại
     */
    @GetMapping("/get")
    public ResponseEntity<List<Phim>> getAll() {
        List<Phim> phims = phimRepository.findAll();
        return ResponseEntity.ok(phims);
    }

    /**
     * Đặt trạng thái Phim bằng 0.
     *
     * @param id
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu phim không tồn tại.
     * Trả về ResponseEntity.OK(Phim) nếu thành công
     */
    //0:Disable
    //1:Enable
    //Yêu cầu cần có sự thống nhất rõ ràng
    //Vì không tách bảng trạng thai
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, phimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Phim phim = (Phim) response.getBody();
            phim.setTrangThai(0);
            return ResponseEntity.status(HttpStatus.OK).body("Disable phim thành công");
        }
        return response;
    }


    /**
     * @param phim
     * @param bindingResult
     * @param id
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu phim không tồn tại.
     * Trả về ResponseEntity.OK(Phim) nếu thành công
     */
    //Receive valid Phim Object from FrontEnd(from a Form or something)
    //And then perform save() right away
    //Instead of creating new instance of Phim and setting each field
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody Phim phim, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, phimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Phim toBeUpdated = (Phim) response.getBody();
            toBeUpdated.setMaPhim("MVS00" + phimRepository.getMaxTableId());
            toBeUpdated.setTenPhim(phim.getTenPhim());
            toBeUpdated.setAnhPhim(phim.getAnhPhim());
            toBeUpdated.setDienVien(phim.getDienVien());
            toBeUpdated.setNam(phim.getNam());
            toBeUpdated.setNoiDungMoTa(phim.getNoiDungMoTa());
            toBeUpdated.setTrailer(phim.getTrailer());
            toBeUpdated.setThoiLuong(phim.getThoiLuong());
            toBeUpdated.setQuocGia(phim.getQuocGia());
            toBeUpdated.setNoiDung(phim.getNoiDung());

            //Xử lý độ tuổi
            DoTuoi fakeDoTuoi = new DoTuoi();
            fakeDoTuoi.setId(phim.getGioiHanDoTuoi().getId());
            toBeUpdated.setGioiHanDoTuoi(fakeDoTuoi);
            //TẠM THỜI Không thực hiện Set trạng thái
            //Lưu vào Database
            return ResponseEntity.status(HttpStatus.OK).body(phimRepository.save(toBeUpdated));
        }
        return response;
    }


    /**
     * Thêm phim mới,
     * Mã phim bằng MVS00+ID mới nhất của bảng Phim
     * Tạo bản ghi Phim trước và lưu
     * Sau đó, tạo các bản ghi của DanhSachTLPhim
     * Với ID Phim vừa tạo
     * Và ID TheLoaiPhim có được từ Phim.DanhSachTLPhim.TheLoaiPhim.id
     *
     * @param phim
     * @param bindingResult
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu phim không tồn tại.
     * Trả về ResponseEntity.OK(Phim) nếu thành công
     */
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody Phim phim, BindingResult bindingResult) {
        //Đổi Mã phim bằng cách chỉnh sửa moviePrefix
        String moviePrefix = "MVS00";

        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        //Đặt mã phim MVS00 + ID mới nhất
        phim.setMaPhim(moviePrefix + (phimRepository.getMaxTableId() + 1));
        phim.setDanhSachTLPhims(phim.getDanhSachTLPhims());
        //TẠM THỜI SET trạng thái là 1 khi vừa khởi tạo Phim
        //Thêm Phim vào Database
        Phim toBeAdded = phimRepository.save(phim);
        //Lấy ID phim
        Long phimId = toBeAdded.getId();

        //Lượt qua Phim.DanhSachTLPhim
        for (DanhSachTLPhim ds : phim.getDanhSachTLPhims()) {
            //Lấy ID TheLoaiPhim để lưu vào DanhSachTLPhim sau đó
            Long theLoaiPhimId = ds.getTheLoaiPhim().getId();
            //Tạo bản ghi mới
            DanhSachTLPhim newPhimAndTheLoai = new DanhSachTLPhim();

            //Setup các property
            newPhimAndTheLoai.setPhim(phimRepository.getReferenceById(phimId));
            newPhimAndTheLoai.setTheLoaiPhim(theLoaiPhimRepository.getReferenceById(theLoaiPhimId));
            newPhimAndTheLoai.setTrangThai(1);
            //Thêm bản ghi mới vào DanhSachTLPhim
            danhSachTLPhimReposiory.save(newPhimAndTheLoai);
        }
        return ResponseEntity.status(HttpStatus.OK).body(phimRepository.save(phim));
    }

    /**
     * Tìm kếm phim theo ID
     *
     * @param id
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu phim không tồn tại.
     * Trả về ResponseEntity.ok(Phim) nếu thành công
     */
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, phimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(phimRepository.save((Phim) response.getBody()));
        }
        return response;
    }

    /**
     * Hỗ trợ tìm kiểm theo tên cột và giá trị của cột
     * @param  columnName
     * @param  value
     * @return Trả về ResponseEntity(200) nếu thành  công
     * Trả về ResponseEntity(badRequest) nếu cột không tồn tại
     * Trả về ResponseEntity(notFound) nếu bản ghi không tồn tại
     * Trả về ResponseEntity(INTERNAL_SERVER_ERROR) nếu lỗi khác
     */
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity findBy(@PathVariable String columnName, @PathVariable String value) {
        ResponseEntity response = RepoUtility.findByCustomColumn(phimRepository, columnName, value);
        return response;
    }

}
