package com.backend.cineboo.controller;

import com.backend.cineboo.entity.ChiTietHoaDon;
import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.entity.Voucher;
import com.backend.cineboo.repository.HoaDonRepository;
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
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hoadon")
@OpenAPIDefinition(
        info = @Info(
                title = "PhimAPI",
                version = "0.1",
                description = "Controller thực hiện quá trình giao dịch và xử lý hoá đơn"
        ))
public class HoaDonController {
    @Autowired
    HoaDonRepository hoaDonRepository;


    @Autowired
    ChiTietHoaDonController chiTietHoaDonController;

    @Autowired
    VoucherController voucherController;

    @GetMapping("/get")
    @Operation(summary = "Hiển thị tất cả hoaDon",
            description = "Trả về Array trống nếu không có hoaDon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List<Entity>")})
    public ResponseEntity<List<HoaDon>> getAll() {
        List<HoaDon> hoaDons = hoaDonRepository.findAll();
        return ResponseEntity.ok(hoaDons);
    }

    /**
     * Đặt trạng thái HoaDon bằng 0.
     *
     * @param id
     * @param trangThai
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu hoaDon không tồn tại.
     * Trả về ResponseEntity.OK(String) nếu thành công
     */
    //0:Disable
    //1:Enable
    //Yêu cầu cần có sự thống nhất rõ ràng
    //Vì không tách bảng trạng thai
    @Operation(summary = "Huỷ hoá hoaDon",
            description = "setTrangThai HoaDon")
    @PutMapping("/status/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Lỗi ID")})
    public ResponseEntity setStatus(@PathVariable Long id, @RequestParam("trangThai") Integer trangThai) {
        ResponseEntity response = RepoUtility.findById(id, hoaDonRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            HoaDon hoaDon = (HoaDon) response.getBody();
            String newStatus;
            hoaDon.setTrangThaiHoaDon(trangThai);
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
            return ResponseEntity.status(HttpStatus.OK).body("Đặt trạng thái hoá đơn: " + newStatus);
        }
        return response;
    }


    /**
     * @param hoaDon
     * @param bindingResult
     * @param id
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu hoaDon không tồn tại.
     * Trả về ResponseEntity.OK(HoaDon) nếu thành công
     */
    //Receive valid HoaDon Object from FrontEnd(from a Form or something)
    //And then perform save() right away
    //Instead of creating new instance of HoaDon and setting each field
    @Operation(summary = "Cập nhật HoaDon",
            description = "Chỉ yêu cầu thông tin của HoaDon, bao gồm cả ID, và ID của các nested objects\n\n"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity vừa khởi tao"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody HoaDon hoaDon, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, hoaDonRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            HoaDon hoaDonToBeUpdated = (HoaDon) response.getBody();
            hoaDonToBeUpdated.setKhachHang(hoaDon.getKhachHang());
            hoaDonToBeUpdated.setPhim(hoaDon.getPhim());
            hoaDonToBeUpdated.setVoucher(hoaDon.getVoucher());
            hoaDonToBeUpdated.setSoLuong(hoaDon.getSoLuong());
            hoaDonToBeUpdated.setThoiGianThanhToan(hoaDon.getThoiGianThanhToan());
            hoaDonToBeUpdated.setDiem(hoaDon.getDiem());
            hoaDonToBeUpdated.setTongSoTien(hoaDon.getTongSoTien());
            hoaDonToBeUpdated.setTrangThaiHoaDon(hoaDon.getTrangThaiHoaDon());
            hoaDonToBeUpdated.setMaHoaDon(hoaDon.getMaHoaDon());
            return ResponseEntity.ok(hoaDonRepository.save(hoaDonToBeUpdated));
        }
        return response;
    }

    private BigDecimal getDecimal18Point2(BigDecimal value) {
        BigDecimal fractionalPart = value.remainder(BigDecimal.ONE);

        // Check if the fractional part is greater than or equal to 0.5
        if (fractionalPart.compareTo(BigDecimal.valueOf(0.5)) >= 0) {
            // Round up if >= 0.5
            return value.setScale(2, RoundingMode.HALF_UP);
        } else {
            // Floor if < 0.5
            return value.setScale(2, RoundingMode.FLOOR);
        }
    }

    @Operation(summary = "Xử lý số tiền hoá đơn",
            description = "Tính số tiền tổng của hoá đơn dựa trên ChiTietHoaDon và Voucher\n\n" +
                    "Và gửi cho người dùng số tiền\n\n" +
                    "Lưu ý, Phương thức này sẽ loại bỏ Voucher đã áp lên Hoá Đơn\n\n" +
                    "Lưu ý, Gọi /price/round ở bước cuối\n\n" +
                    "Để đưa giá trị số về Decimal (18,2)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity vừa khởi tao"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @PutMapping("/price/invoices/{id}")
    public ResponseEntity calculatePriceFromInvoiceDetails(@PathVariable Long id) {
        //Hiển thị hoá đơn cuối cùng cho người dùng xem
        //Để họ dựa vào đó mà thanh toán/chuyển khoản
        ResponseEntity response = RepoUtility.findById(id, hoaDonRepository);
        //Đặt giá tiền cho hoá đơn
        if (response.getStatusCode().is2xxSuccessful()) {
            HoaDon hoaDon = (HoaDon) response.getBody();
            BigDecimal finalPrice = chiTietHoaDonController.getTotalPrice(id);
            if (finalPrice.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Giá tiền phải lớn hơn 0");
            }
            hoaDon.setTongSoTien(finalPrice);//Set giá gốc
            String appliedVoucher = hoaDon.getVoucher().getMaVoucher();
            hoaDon.setVoucher(null);//Xoá voucher đã áp dụng
            hoaDon = hoaDonRepository.save(hoaDon);//Lưu lại
            voucherController.increaseVoucherCount(appliedVoucher);//Do hoá đơn không dùng nữa, tăng lên 1
            return ResponseEntity.ok(hoaDon);
        }
        return response;
    }

    @Operation(summary = "Làm tròn số tiền trong hoá đơn",
            description = "Đưa giá trị về kiểu Decimal (18,2)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity HoaDon"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @PutMapping("/price/round/{hoaDonId}")
    public ResponseEntity roundAndFloorHoaDonMoney(@PathVariable Long hoaDonId) {
        ResponseEntity response = RepoUtility.findById(hoaDonId, hoaDonRepository);
        //Đặt giá tiền cho hoá đơn
        if (response.getStatusCode().is2xxSuccessful()) {
            HoaDon hoaDon = (HoaDon) response.getBody();
            BigDecimal totalPrice = hoaDon.getTongSoTien();
            totalPrice = getDecimal18Point2(totalPrice);
            hoaDon.setTongSoTien(totalPrice);
            hoaDon = hoaDonRepository.save(hoaDon);
            return ResponseEntity.ok(hoaDon);
        }
        return response;
    }

    /**
     * Thêm hoaDon mới,
     * Mã hoaDon bằng MVS00+ID mới nhất của bảng HoaDon
     * Tạo bản ghi HoaDon trước và lưu
     * Sau đó, tạo các bản ghi của DanhSachTLHoaDon
     * Với ID HoaDon vừa tạo
     * Và ID TheLoaiHoaDon có được từ HoaDon.DanhSachTLHoaDon.TheLoaiHoaDon.id
     *
     * @param hoaDon
     * @param bindingResult
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu hoaDon không tồn tại.
     * Trả về ResponseEntity.OK(HoaDon) nếu thành công
     */
    @Operation(summary = "Thêm HoaDon rỗng mới",
            description = "Chỉ yêu cầu thông tin CƠ BẢN của HoaDon và ID của các nested objects(TRỪ CHITIETHOADON)\n\n" +
             "LƯU Ý: YÊU CẦU CHITIETHOADON(GHE,TRANGTHAICHITIETHOADON) ĐỂ TẠO MỚI\n\n" +
                    "Lưu ý: Số lượng được tính theo số bản ghi chi tiết hoá đơn\n\n" +
                    "Lưu ý: Tổng số tền được tinh theo tổng giá tiền hiện tại của Ghế\n\n" +
                    "Lưu ý: Thời gian thanh toán lấy từ object HoaDop tu Parameter\n\n" +
                    "Lưu ý: Thời gian thanh toán sẽ được cập nhật khi thanh toán thành công\n\n" +
                    "Lưu ý: Số lượng được tính theo số bản ghi chi tiết hoá đơn\n\n" +
                    "Lưu ý: Trạng thái mặc định là 0\n\n" +
                    "Không yêu cầu PTTT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity vừa khởi tao"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody HoaDon hoaDon, BindingResult bindingResult) {
        hoaDon.setId(null);//Just in case this somehow turns into an update operation
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        //Uncomment đoạn này nếu muốn thêm ChiTietHoaDon một cách thủ công
        //Tạo hoá đơn rỗng
        HoaDon blankInvoice = createBlankInvoice(hoaDon);
        //Lưu vào DB để tí còn có ID mà xử lý
        //Trước khi lưu, tách List ra để tí xử lý
        List<ChiTietHoaDon> chiTietHoaDonList = hoaDon.getChiTietHoaDonList();
        blankInvoice.setChiTietHoaDonList(null);
        blankInvoice = hoaDonRepository.save(blankInvoice);
        //Thêm hoá đơn chi tiết
        boolean addInvoiceDetail = createAndAddInvoiceDetailToInvoice(blankInvoice,chiTietHoaDonList);
        if (addInvoiceDetail) {
            return ResponseEntity.status(HttpStatus.OK).body(blankInvoice);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xử lý hoá đơn chi tiết");
//        Uncomment đoạn này nếu muốn thêm ChiTietHoaDon một cách thủ công

        //Thêm kiểu cascade, thêm parent, nested child objects tự động thêm
//        return ResponseEntity.ok(hoaDonRepository.save(hoaDon));
    }


    private HoaDon createBlankInvoice(HoaDon hoaDon) {
        String hoaDonPrefix = "HD00";
        //Tạo hoá đơn rỗng
        //Chứa các thông tin cơ bản
        HoaDon blankHoaDon = new HoaDon();
        blankHoaDon.setKhachHang(hoaDon.getKhachHang());
        blankHoaDon.setChiTietHoaDonList(hoaDon.getChiTietHoaDonList());
        blankHoaDon.setPhim(hoaDon.getPhim());
        blankHoaDon.setVoucher(hoaDon.getVoucher());
        blankHoaDon.setSoLuong(hoaDon.getChiTietHoaDonList().size());
        blankHoaDon.setThoiGianThanhToan(hoaDon.getThoiGianThanhToan());

        int point = hoaDon.getDiem()
                + (hoaDon.getTongSoTien()
                .multiply(BigDecimal.ONE
                        .divide(BigDecimal.TEN.multiply(BigDecimal.TEN)))).intValue();
        blankHoaDon.setDiem(point);
        blankHoaDon.setTongSoTien(new BigDecimal(0));
        blankHoaDon.setTrangThaiHoaDon(0);
        blankHoaDon.setMaHoaDon(hoaDonPrefix + hoaDonRepository.getMaxTableId());
        return hoaDonRepository.save(blankHoaDon);
    }

    @PutMapping("/price/voucher/{hoaDonId}")
    public ResponseEntity applyVoucher(@PathVariable Long hoaDonId, @RequestParam String maVoucher) {
        //Tìm hoá đơn
        ResponseEntity hoaDonResponse = RepoUtility.findById(hoaDonId, hoaDonRepository);
        if (hoaDonResponse.getStatusCode().is2xxSuccessful()) {
            HoaDon hoaDon = (HoaDon) hoaDonResponse.getBody();

            //Tìm voucher xem có tồn tại hay khả dụng không
            ResponseEntity voucherResponse = voucherController.isVoucherAvailable(maVoucher);
            if (voucherResponse.getStatusCode().is2xxSuccessful()) {
                Voucher voucher = (Voucher) voucherResponse.getBody();

                //Lấy số tiền giảm dựa trên voucher và số tiền tổng của hoá đơn
                BigDecimal discountAmount = voucherController.getDiscountAmount(maVoucher, hoaDon);
                //Kiểm lỗi
                if (discountAmount == null) {
                    return ResponseEntity.internalServerError().body("Lỗi xử lý voucher");
                } else if (discountAmount.compareTo(BigDecimal.ZERO) == 0) {
                    return ResponseEntity.badRequest().body("Giá tiền sau giảm phải lớn hơn 0");
                }
                //Nếu hợp lệ, set voucher
                hoaDon.setVoucher(voucher);
                //Nếu hợp lệ, set số tiền mới
                hoaDon.setTongSoTien(discountAmount);
                //Giảm số lượng voucher
                voucherController.decreaseVoucherCount(maVoucher);
                //Lưu lại hoá đơn
                hoaDon = hoaDonRepository.save(hoaDon);
                return ResponseEntity.status(HttpStatus.OK).body(hoaDon);
            }
            return voucherResponse;
        }
        return hoaDonResponse;
    }

    private boolean createAndAddInvoiceDetailToInvoice(HoaDon hoaDon,List<ChiTietHoaDon> chiTietHoaDonList) {
        //Coder is having a cold and therefore low motivation
        //No more validation or constraint check to verify if hoaDon exists
        //Just.....do it
        for (ChiTietHoaDon chiTietHoaDon : chiTietHoaDonList) {
            //Lượt qua danh sách Hoá đơn chi tiết
            //Tạo mới chi tiêt hoá đơn
            ChiTietHoaDon temp = chiTietHoaDonController.createBlankInvoiceDetail(chiTietHoaDon,hoaDon);
            if (temp != null) {
                //Thêm  vào Hoá Đơn
                chiTietHoaDonController.setHoaDonId(temp.getId(), hoaDon);
            } else {
                return false;
            }
        }
        return true;
    }


    /**
     * Tìm kếm hoaDon theo ID
     *
     * @param id
     * @return Trả về Bad Request nếu ID Null.a
     * Trả về Not found nếu hoaDon không tồn tại.
     * Trả về ResponseEntity.ok(HoaDon) nếu thành công
     */
    @Operation(summary = "Tìm kiếm theo index của Database HoaDon",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, hoaDonRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok((HoaDon) response.getBody());
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
    @Operation(summary = "Tìm kiếm hoaDon theo cú pháp findBy+columnName",
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
        ResponseEntity response = RepoUtility.findByCustomColumn(hoaDonRepository, columnName, value);
        return response;
    }

}
