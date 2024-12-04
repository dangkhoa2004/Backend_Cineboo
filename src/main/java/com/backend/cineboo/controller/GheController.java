package com.backend.cineboo.controller;

import com.backend.cineboo.dto.AddGheDTO;
import com.backend.cineboo.dto.GheWithoutSuatChieuId;
import com.backend.cineboo.entity.Ghe;
import com.backend.cineboo.entity.GheWithBookingStatus;
import com.backend.cineboo.entity.PhongChieu;
import com.backend.cineboo.entity.RevenuePerMovie;
import com.backend.cineboo.repository.GheRepository;
import com.backend.cineboo.repository.PhongChieuRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.Valid;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ghe")
@OpenAPIDefinition(
        info = @Info(
                title = "GheAPI",
                version = "0.1",
                description = "CRUD cơ bản cho bảng Ghe"
        ))
public class GheController {
    @Autowired
    GheRepository gheRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    PhongChieuRepository phongChieuRepository;

    @Operation(summary = "Lấy danh sách ghế",
            description = "Trả về danh sách tất cả ghế trong hệ thống.")
    @GetMapping("/get")
    public ResponseEntity<List<Ghe>> get() {
        List<Ghe> ghes = new ArrayList<>();
        ghes = gheRepository.findAll();
        return ResponseEntity.ok(ghes);
    }

    @Operation(summary = "Vô hiệu hóa ghế",
            description = "Đặt trạng thái ghế thành không khả dụng bằng cách sử dụng ID.")
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

    @Operation(summary = "Thêm ghế mới",
            description = "Thêm một ghế mới vào hệ thống\n\n" +
                    "Mã Ghế đặt bởi giá trị nhận vào từ request" +
                    "Sẽ check trùng lặp dưa trên MaGhe và ID_PhongChieu\n\n" +
                    "Trạng thái mặc định là 0")
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody AddGheDTO ghe, BindingResult bindingResult) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Ghe addedGhe = new Ghe();
//        String prefix = "GH00";
//        addedGhe.setMaGhe(prefix+gheRepository.getMaxTableId());//Cuz Fe wants to make it manual
        addedGhe.setMaGhe(ghe.getMaGhe());
        ResponseEntity response = RepoUtility.findById(ghe.getId_PhongChieu(), phongChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            PhongChieu phongChieu = (PhongChieu) response.getBody();
            String id_PhongChieu = phongChieu.getId().toString();
            String maGhe = ghe.getMaGhe();
            Ghe duplicate = gheRepository.findByID_PhongChieuAndMaGhe(id_PhongChieu, maGhe).orElse(null);
            if (duplicate != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ghế " + maGhe + " của Phòng chiêu " + id_PhongChieu + " đã tồn tại");
            }
            addedGhe.setPhongChieu(phongChieu);
            addedGhe.setGiaTien(ghe.getGiaTien());
            addedGhe.setTrangThaiGhe(0);
            //Here, will check if it's duplicated
            //Based on MaGhe and PhongChieu
            //MaGhe can be the same, but should appear in each PhongChieu only once


            addedGhe = gheRepository.save(addedGhe);
            return ResponseEntity.ok(addedGhe);
        }
        return response;
    }

    @Operation(summary = "Cập nhật thông tin ghế",
            description = "Cập nhật thông tin ghế dựa trên ID ghế.")
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

    @Operation(summary = "Tìm kiếm ghế theo ID",
            description = "Tìm kiếm ghế bằng ID và trả về thông tin ghế.")
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, gheRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Ghe ghe = (Ghe) response.getBody();
            return ResponseEntity.ok(gheRepository.save(ghe));
        }
        return response;
    }

    @Operation(summary = "Tìm kiếm danh sách ghế theo ID phim và thời gian chiếu",
            description = "Tìm kiếm ghế dựa trên ID phim và thời gian chiếu.")
    @GetMapping("find/ID_PhimAndThoiGianChieu")
    public ResponseEntity findByPhimIdAndThoiGianChieu(@RequestParam("ID_Phim") String phimId, @RequestParam("ThoiGianChieu") String thoiGianChieu) {
        List<GheWithoutSuatChieuId> ghes = gheRepository.findByIdPhimAndThoiGianChieu(phimId, thoiGianChieu);
        System.out.println(ghes);
        return ResponseEntity.ok(ghes);
    }

    @Operation(summary = "Tìm kiếm ghế theo cột và giá trị",
            description = "Hỗ trợ tìm kiếm theo tên cột và giá trị của cột.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy"),
            @ApiResponse(responseCode = "500", description = "Lỗi nội bộ")})
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity findBy(@PathVariable String columnName, @PathVariable String value) {
        ResponseEntity response = RepoUtility.findByCustomColumn(gheRepository, columnName, value);
        return response;
    }

    @Operation(summary = "Tìm danh sách ghế có trạng thái booking dựa trên id_SuatChieu")
    @GetMapping("findWithBooking/{id_SuatChieu}")
    public ResponseEntity findGheWithStatus(@PathVariable String id_SuatChieu) {
        if (id_SuatChieu.matches("[0-9]+")) {
            String sql = "SELECT ghe.id, ghe.MaGhe,ghe.GiaTien, ghe.ID_PhongChieu,ghe.TrangThaiGhe,gheandsuatchieu.TRANGTHAIGHEANDSUATCHIEU\n" +
                    "FROM GheAndSuatChieu \n" +
                    "JOIN ghe on ghe.ID=gheandsuatchieu.ID_GHE\n" +
                    "WHERE  ID_SuatChieu = :id ";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("id", id_SuatChieu);
            List<Object[]> rows = query.getResultList();
            List<GheWithBookingStatus> result = new ArrayList<>(rows.size());
            for (Object[] row : rows) {
                GheWithBookingStatus gheWithBookingStatus = new GheWithBookingStatus();
                gheWithBookingStatus.setId((Integer) row[0]);
                gheWithBookingStatus.setMaGhe((String) row[1]);
                gheWithBookingStatus.setGiaTien((BigDecimal) row[2]);
                gheWithBookingStatus.setId_PhongChieu((Integer) row[3]);
                gheWithBookingStatus.setTrangThaiGhe((Integer) row[4]);
                gheWithBookingStatus.setTrangThaiGheAndSuatChieu((Byte) row[5]);
                result.add(gheWithBookingStatus);
            }
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sai mã suất chiếu");
    }
}
