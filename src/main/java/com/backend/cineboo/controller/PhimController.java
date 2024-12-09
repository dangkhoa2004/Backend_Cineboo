package com.backend.cineboo.controller;

import com.backend.cineboo.dto.AddPhimDTO;
import com.backend.cineboo.dto.UpdatePhimDTO;
import com.backend.cineboo.entity.DanhSachTLPhim;
import com.backend.cineboo.entity.DoTuoi;
import com.backend.cineboo.entity.Phim;
import com.backend.cineboo.entity.TheLoaiPhim;
import com.backend.cineboo.repository.DanhSachTLPhimReposiory;
import com.backend.cineboo.repository.DoTuoiRepository;
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

import java.util.ArrayList;
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
    DoTuoiRepository dotuoiRepository;

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
     * @param updatePhimDTO
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
    public ResponseEntity update(@Valid @RequestBody UpdatePhimDTO updatePhimDTO, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        if(updatePhimDTO.getId()!=id){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID không trùng khớp");
        }


        ResponseEntity response = RepoUtility.findById(id, phimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Phim toBeUpdated = (Phim) response.getBody();
            toBeUpdated.setMaPhim("MVS00" + phimRepository.getMaxTableId());
            toBeUpdated.setTenPhim(updatePhimDTO.getTenPhim());
            toBeUpdated.setAnhPhim(updatePhimDTO.getAnhPhim());
            toBeUpdated.setDienVien(updatePhimDTO.getDienVien());
            toBeUpdated.setNam(updatePhimDTO.getNam());
            toBeUpdated.setNoiDungMoTa(updatePhimDTO.getNoiDungMoTa());
            toBeUpdated.setTrailer(updatePhimDTO.getTrailer());
            toBeUpdated.setNgayRaMat(updatePhimDTO.getNgayRaMat());
            toBeUpdated.setThoiLuong(updatePhimDTO.getThoiLuong());
            toBeUpdated.setQuocGia(updatePhimDTO.getQuocGia());
            toBeUpdated.setNoiDung(updatePhimDTO.getNoiDung());
            DoTuoi doTuoi = dotuoiRepository.findById(updatePhimDTO.getId_GioiHanDoTuoi()).orElse(null);
            if(doTuoi==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy độ tuổi");
            }
            toBeUpdated.setGioiHanDoTuoi(doTuoi);
            toBeUpdated.setTrangThai(updatePhimDTO.getTrangThai());
            toBeUpdated.setDiem(updatePhimDTO.getDiem());

            danhSachTLPhimReposiory.deleteAllByID_Phim(id.toString());

            List<DanhSachTLPhim> danhSachTLPhimList = new ArrayList<>();
            for(Long theLoaiPhimId : updatePhimDTO.getId_TheLoaiPhims()){
                TheLoaiPhim theLoaiPhim = theLoaiPhimRepository.findById(theLoaiPhimId).orElse(null);
                if(theLoaiPhim==null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có thể loại phim với ID là "+theLoaiPhimId);
                }
                DanhSachTLPhim danhSachTLPhim = new DanhSachTLPhim();
                danhSachTLPhim.setTheLoaiPhim(theLoaiPhim);
                danhSachTLPhim.setPhim(toBeUpdated);
                danhSachTLPhim.setTrangThai(1);
                danhSachTLPhimList.add(danhSachTLPhim);
            }

            phimRepository.save(toBeUpdated);
            danhSachTLPhimReposiory.saveAll(danhSachTLPhimList);
            Phim savedPhim = phimRepository.findById(id).orElse(null);
            return ResponseEntity.status(HttpStatus.OK).body(savedPhim);
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
     * @param
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
    public ResponseEntity add(@Valid @RequestBody AddPhimDTO addPhimDTO, BindingResult bindingResult) {
        //Đổi Mã phim bằng cách chỉnh sửa moviePrefix
        String moviePrefix = "MVS00";

        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }


        Phim toBeAdded = new Phim();
        toBeAdded.setMaPhim(moviePrefix + (phimRepository.getMaxTableId() + 1));
        toBeAdded.setTenPhim(addPhimDTO.getTenPhim());
        toBeAdded.setAnhPhim(addPhimDTO.getAnhPhim());
        toBeAdded.setDienVien(addPhimDTO.getDienVien());
        toBeAdded.setNam(addPhimDTO.getNam());
        toBeAdded.setNoiDungMoTa(addPhimDTO.getNoiDungMoTa());
        toBeAdded.setNoiDung(addPhimDTO.getNoiDung());
        toBeAdded.setNgayRaMat(addPhimDTO.getNgayRaMat());
        toBeAdded.setThoiLuong(addPhimDTO.getThoiLuong());
        toBeAdded.setQuocGia(addPhimDTO.getQuocGia());
        toBeAdded.setTrailer(addPhimDTO.getTrailer());
        toBeAdded.setDanhSachTLPhims(null);
        toBeAdded.setTrangThai(addPhimDTO.getTrangThai());
        toBeAdded.setDiem(addPhimDTO.getDiem());
        DoTuoi doTuoi = dotuoiRepository.findById(addPhimDTO.getId_GioiHanDoTuoi()).orElse(null);
        if(doTuoi==null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sai do tuoi");
        }
        toBeAdded.setGioiHanDoTuoi(doTuoi);
        Phim newPhim =  phimRepository.save(toBeAdded);





        //Lượt qua Phim.DanhSachTLPhim
        for (Long id_TheLoaiPhim : addPhimDTO.getId_TheLoaiPhims()) {
            TheLoaiPhim theLoaiPhim = theLoaiPhimRepository.findById(id_TheLoaiPhim).orElse(null);
            if(theLoaiPhim==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Thể loai phim không tồn tại");
            }
            DanhSachTLPhim danhSachTLPhim = new DanhSachTLPhim();
            danhSachTLPhim.setPhim(newPhim);
            danhSachTLPhim.setTheLoaiPhim(theLoaiPhim);
            danhSachTLPhim.setTrangThai(1);
            danhSachTLPhimReposiory.save(danhSachTLPhim);
            //Tạo bản ghi mới
        }
        newPhim = phimRepository.findById(newPhim.getId()).orElse(null);
        if(newPhim==null){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Loi them phim");
        }
        return ResponseEntity.status(HttpStatus.OK).body(newPhim);
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
