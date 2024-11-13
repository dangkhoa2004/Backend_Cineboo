package com.backend.cineboo.controller;

import com.backend.cineboo.entity.DanhSachTLPhim;
import com.backend.cineboo.entity.DoTuoi;
import com.backend.cineboo.entity.Phim;
import com.backend.cineboo.repository.DanhSachTLPhimReposiory;
import com.backend.cineboo.repository.PhimRepository;
import com.backend.cineboo.repository.TheLoaiPhimRepository;
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
@OpenAPIDefinition(
        info = @Info(
                title = "PhimAPI",
                version = "0.1",
                description = "CRUD cơ bản cho bảng Phim"
        ))
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
    @Operation(summary = "Hiển thị tất cả phim",
            description = "Trả về Array trống nếu không có phim")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List<Entity>")})
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
    @Operation(summary = "Vô hiệu hoá phim",
            description = "setTrangThai Phim bằng 0")
    @PutMapping("/disable/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Lỗi ID")})
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, phimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Phim phim = (Phim) response.getBody();
            phim.setTrangThai(0);
            phimRepository.save(phim);
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
    @Operation(summary = "Cập nhật phim",
            description = "Chỉ yêu cầu Thông tin cơ bản của Phim, các nested object chỉ cần ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity vừa cập nhật"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
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
    @Operation(summary = "Thêm Phim mới",
            description = "Chỉ yêu cầu thông tin của Phim và ID của các nested objects ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity vừa khởi tao"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
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
            newPhimAndTheLoai.setPhim(phimRepository.findById(phimId).get());
            newPhimAndTheLoai.setTheLoaiPhim(theLoaiPhimRepository.findById(theLoaiPhimId).get());
            newPhimAndTheLoai.setTrangThai(1);

            //Thêm bản ghi mới vào DanhSachTLPhim

            danhSachTLPhimReposiory.save(newPhimAndTheLoai);
        }
        phim.setId(null);//To make sure its an INSERT and Not Update since both use save()
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
    @Operation(summary = "Tìm kiếm theo index của Database Phim",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, phimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(((Phim) response.getBody()));
        }
        return response;
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
        ResponseEntity response = RepoUtility.findByCustomColumn(phimRepository, columnName, value);
        return response;
    }

}
