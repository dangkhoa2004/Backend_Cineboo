package com.backend.cineboo.controller;

import com.backend.cineboo.entity.DoTuoi;
import com.backend.cineboo.entity.ChiTietHoaDon;
import com.backend.cineboo.entity.ChiTietHoaDon;
import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.repository.ChiTietHoaDonRepository;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cthoadon")
@OpenAPIDefinition(
        info = @Info(
                title = "ChiTietHoaDonAPI",
                version = "0.1",
                description = "Controller xử lý bảng ChiTietHoaDon"
        ))
public class ChiTietHoaDonController {
    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;


    @GetMapping("/get")
    @Operation(summary = "Hiển thị tất cả chiTietHoaDon",
            description = "Trả về Array trống nếu không có chiTietHoaDon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List<Entity>")})
    public ResponseEntity<List<ChiTietHoaDon>> getAll() {
        List<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDonRepository.findAll();
        return ResponseEntity.ok(chiTietHoaDons);
    }

    /**
     * Đặt trạng thái ChiTietHoaDon
     *
     * @param id
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu chiTietHoaDon không tồn tại.
     * Trả về ResponseEntity.OK(ChiTietHoaDon) nếu thành công
     */
    //0:Disable
    //1:Enable
    //Yêu cầu cần có sự thống nhất rõ ràng
    //Vì không tách bảng trạng thai
    @Operation(summary = "Vô hiệu hoá chiTietHoaDon",
            description = "setTrangThai ChiTietHoaDon bằng 0")
    @PutMapping("/status/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Lỗi ID")})
    public ResponseEntity disable(@PathVariable Long id, @RequestParam Integer trangThai) {
        ResponseEntity response = RepoUtility.findById(id, chiTietHoaDonRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            ChiTietHoaDon chiTietHoaDon = (ChiTietHoaDon) response.getBody();
            String newStatus;
            chiTietHoaDon.setTrangThaiChiTietHoaDon(trangThai);
            switch (trangThai) {
                case 0:
                    newStatus = "Hoá đơn rỗng";
                    break;
                case 1:
                    newStatus = "Hoá đơn đã thanh toán";
                    break;
                case 2:
                    newStatus = "Hoá đơn huỷ do khách không thanh toán";
                    break;
                default:
                    newStatus = "Trạng thái không xác định";
            }
            chiTietHoaDon = chiTietHoaDonRepository.save(chiTietHoaDon);
            Map result = new HashMap();
            result.put("value", chiTietHoaDon);
            result.put("status", trangThai + ": " + newStatus);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return response;
    }


    /**
     * @param chiTietHoaDon
     * @param bindingResult
     * @param id
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu chiTietHoaDon không tồn tại.
     * Trả về ResponseEntity.OK(ChiTietHoaDon) nếu thành công
     */
    //Receive valid ChiTietHoaDon Object from FrontEnd(from a Form or something)
    //And then perform save() right away
    //Instead of creating new instance of ChiTietHoaDon and setting each field
    @Operation(summary = "Cập nhật chiTietHoaDon",
            description = "Chỉ yêu cầu Thông tin cơ bản của ChiTietHoaDon, các nested object chỉ cần ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity vừa cập nhật"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody ChiTietHoaDon chiTietHoaDon, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, chiTietHoaDonRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            ChiTietHoaDon toBeUpdated = (ChiTietHoaDon) response.getBody();
            toBeUpdated.setGhe(chiTietHoaDon.getGhe());
            toBeUpdated.setHoaDon((chiTietHoaDon.getHoaDon()));
            toBeUpdated.setTrangThaiChiTietHoaDon(chiTietHoaDon.getTrangThaiChiTietHoaDon());
            return ResponseEntity.ok(chiTietHoaDonRepository.save(toBeUpdated));
        }
        return response;
    }


    /**
     * Thêm chiTietHoaDon mới
     *
     * @param chiTietHoaDon
     * @param bindingResult
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu chiTietHoaDon không tồn tại.
     * Trả về ResponseEntity.OK(ChiTietHoaDon) nếu thành công
     */
    @Operation(summary = "Thêm ChiTietHoaDon rỗng mới",
            description = "Chỉ yêu cầu thông tin của ChiTietHoaDon và ID của các nested objects\n\n" +
                    "Không yêu cầu PTTT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity vừa khởi tao"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody ChiTietHoaDon chiTietHoaDon, BindingResult bindingResult) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        //After confirming that Invoice Detail is okay-ish
        //Create it anew and add it to db
        ChiTietHoaDon blankChiTietHoaDon = createBlankInvoiceDetail(chiTietHoaDon,chiTietHoaDon.getHoaDon());
        if(blankChiTietHoaDon==null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Đã tồn tại ChiTietHoaDon");
        }
        return ResponseEntity.status(HttpStatus.OK).body(chiTietHoaDonRepository.save(blankChiTietHoaDon));
    }

    /**
     * Thêm Hoá đơn chi tiết
     * Phương thức nầy không kiểm tra ràng buộc
     * Nếu muốn Validation, sử dụng mapping /add
     * @param chiTietHoaDon
     * @return
     */
    public ChiTietHoaDon createBlankInvoiceDetail(ChiTietHoaDon chiTietHoaDon,HoaDon hoaDon){
        ChiTietHoaDon blankChiTietHoaDon = new ChiTietHoaDon();
        blankChiTietHoaDon.setHoaDon(hoaDon);//ChiTietHoaDon la nestedObject cua HoaDon. HoaDon su dung JSonIgnore de tranh lap vo han
        blankChiTietHoaDon.setGhe(chiTietHoaDon.getGhe());
        blankChiTietHoaDon.setTrangThaiChiTietHoaDon(0);//Set 0 by default
        //Check dups first, if already there
        //Just in case somehow the user can magically call this method again using the same ID_HoaDon
        ChiTietHoaDon checkDups = chiTietHoaDonRepository.checkDuplicate(hoaDon.getId(),chiTietHoaDon.getGhe().getId()).orElse(null);
        if(checkDups==null) {
            return chiTietHoaDonRepository.save(blankChiTietHoaDon);//If no dups, add anew
        }
        return null;// or else return null
    }


    /**
     * Thêm ID_HoaDon và ChiTietHoaDon
     * Hoặc là Đặt một HoaDonChiTiet thuộc về một HoaDon
     *
     * @param chiTietHoaDonId
     * @param hoaDon
     * @return
     */
    public ChiTietHoaDon setHoaDonId(Long chiTietHoaDonId, HoaDon hoaDon) {

        ChiTietHoaDon chiTietHoaDon = chiTietHoaDonRepository.findById(chiTietHoaDonId).orElseThrow();
        if (hoaDon.getId() == null || hoaDon.getId() <= 0) {
            return null;
        }
        //Just need ID_HoaDon
        //But since I don't want to modify Entity
        //So whatever
        chiTietHoaDon.setHoaDon(hoaDon);
        return chiTietHoaDonRepository.save(chiTietHoaDon);
    }

    public BigDecimal getTotalPrice(Long id_HoaDon) {
        return chiTietHoaDonRepository.getFinalPrice(id_HoaDon).orElse(new BigDecimal(0));
    }

    /**
     * Tìm kếm chiTietHoaDon theo ID
     *
     * @param id
     * @return Trả về Bad Request nếu ID Null.a
     * Trả về Not found nếu chiTietHoaDon không tồn tại.
     * Trả về ResponseEntity.ok(ChiTietHoaDon) nếu thành công
     */
    @Operation(summary = "Tìm kiếm theo index của Database ChiTietHoaDon",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, chiTietHoaDonRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok((ChiTietHoaDon) response.getBody());
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
    @Operation(summary = "Tìm kiếm chiTietHoaDon theo cú pháp findBy+columnName",
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
        ResponseEntity response = RepoUtility.findByCustomColumn(chiTietHoaDonRepository, columnName, value);
        return response;
    }

}
