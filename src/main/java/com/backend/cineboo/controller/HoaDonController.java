package com.backend.cineboo.controller;

import com.backend.cineboo.dto.AddHoaDonDTO;
import com.backend.cineboo.dto.ChiTietHoaDonListDTO;
import com.backend.cineboo.entity.*;
import com.backend.cineboo.repository.*;
import com.backend.cineboo.scheduledJobs.EndSuatChieuJob;
import com.backend.cineboo.scheduledJobs.UnreserveGheJobs;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.InvoiceGenerator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

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
    Scheduler scheduler;

    @Autowired
    KhachHangRepository khachHangRepository;


    @Autowired
    ChiTietHoaDonController chiTietHoaDonController;

    @Autowired
    PTTTRepository ptttRepository;

    @Autowired
    VoucherController voucherController;

    @Autowired
    GheRepository gheRepository;
    @Autowired
    PhimRepository phimRepository;

    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    SuatChieuRepository suatChieuRepository;

    @GetMapping("/get")
    @Operation(summary = "Hiển thị tất cả hoaDon",
            description = "Trả về Array trống nếu không có hoaDon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List<Entity>")})
    public ResponseEntity<List<HoaDon>> getAll() {
        List<HoaDon> hoaDons = hoaDonRepository.findAll();
        return ResponseEntity.ok(hoaDons);
    }

    private void revertSeatsAfter5Mins(Long hoaDonId) {
        //Check if HoaDon is paid
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElse(null);
        if (hoaDon != null && hoaDon.getTrangThaiHoaDon() == 1) {
            //If HoaDon exists and is already paid
            //Then do nothing
            return;
        }
        //Otherwise nuke it to oblivion
        try {
            JobDetail unbookGheJob = JobBuilder.newJob(UnreserveGheJobs.class)
                    .withIdentity("unreserveGheJob" + hoaDonId, "hoaDonGroup")
                    .build();

            SimpleTrigger trigger = newTrigger()
                    .withIdentity("unreserveGheTrigger" + hoaDonId, "hoaDonGroup")
                    .forJob(unbookGheJob)
                    .usingJobData("id", hoaDonId)
                    .startAt(futureDate(5, DateBuilder.IntervalUnit.MINUTE))
                    .withSchedule(simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();
            scheduler.start();
            scheduler.scheduleJob(unbookGheJob, trigger);
            // Keep the app running to observe the scheduler
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
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
    @Operation(summary = "Thay đổi trạng thái hoá hoaDon",
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
            switch (trangThai) {
                case 0:
                    newStatus = "Hoá đơn rỗng";
                    hoaDon.setTrangThaiHoaDon(trangThai);
                    break;
                case 1:
                    newStatus = "Hoá đơn đã thanh toán";
                    hoaDon.setTrangThaiHoaDon(trangThai);
                    break;
                case 2:
                    newStatus = "Hoá đơn huỷ do khách không thanh toán";
                    hoaDon.setTrangThaiHoaDon(trangThai);
                    break;
                default:
                    newStatus = "Trạng thái không xác định";
                    //Không set Status
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
            hoaDonToBeUpdated.setSuatChieu(hoaDon.getSuatChieu());
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
            Voucher voucher = hoaDon.getVoucher();
            if (voucher != null) {
                String appliedVoucher = hoaDon.getVoucher().getMaVoucher();
                hoaDon.setVoucher(null);//Xoá voucher đã áp dụng
                voucherController.increaseVoucherCount(appliedVoucher);//Do hoá đơn không dùng nữa, tăng lên 1
            }
            hoaDon = hoaDonRepository.save(hoaDon);//Lưu lại
            return ResponseEntity.ok(hoaDon);
        }
        return response;
    }

    @Operation(summary = "Đặt PTTT",
            description = "Đặt Phương thức thanh toán cho hoá đơn, yêu cầu ID của HoaDon và PTTT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity vừa thay đổi"),
            @ApiResponse(responseCode = "404", description = "Not Found")})
    @PutMapping("/hoaDon/{ID_HoaDon}/setPTTT/{ID_PTTT}")
    public ResponseEntity setPTTT(@PathVariable Long ID_PTTT, @PathVariable Long ID_HoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(ID_HoaDon).orElse(null);
        PTTT pttt = ptttRepository.findById(ID_PTTT).orElse(null);
        if (pttt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PTTT không tồn tại");
        }
        if (hoaDon != null) {
            hoaDon.setPttt(pttt);
            return ResponseEntity.ok(hoaDonRepository.save(hoaDon));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm được hoá đơn");
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
                    "LƯU Ý: YÊU CẦU ChiTietHoaDonListDTO(MaGhe,ID_PhongChieu)\n\n" +
                    "Lưu ý: Số lượng được tính theo số bản ghi ChiTietHoaDonListDTO\n\n" +
                    "Lưu ý: Tổng số tền được tinh theo ChiTietHoaDonListDTO\n\n" +
                    "Lưu ý: Thời gian thanh toán mặc định là thời gian tạo HoaDon\n\n" +
                    "Lưu ý: Thời gian thanh toán sẽ được cập nhật khi thanh toán thành công\n\n" +
                    "Lưu ý: Số lượng được tính theo ChiTietHoaDonListDTO\n\n" +
                    "Lưu ý: Trạng thái mặc định là 0\n\n" +
                    "Lưu ý: PTTT mặc định là null\\" +
                    "Lưu ý: Voucher mặc định là null\n\n" +
                    "Ghế chưa book sẽ chuyển lên trangThai(2) giữ ghế\n\n" +
                    "Sau khi tạo hoá đơn thành oông, cho lên trangThai(1) đã book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hoá đơn vừa khởi tao"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "403", description = "Ghế đã được chọn"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody AddHoaDonDTO hoaDon, BindingResult bindingResult
    ) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        List<ChiTietHoaDonListDTO> chiTietHoaDonList = hoaDon.getChiTietHoaDonList();
        if (chiTietHoaDonList == null || chiTietHoaDonList.isEmpty()) {
            return ResponseEntity.badRequest().body("Thiếu List<ChiTietHoaDonListDTO>");
        }
        ResponseEntity suatchieuResponse = RepoUtility.findById(hoaDon.getSuatChieuId(), suatChieuRepository);
        if (suatchieuResponse.getStatusCode().is2xxSuccessful()) {
            SuatChieu suatChieu = (SuatChieu) suatchieuResponse.getBody();
            String id_PhongChieu = suatChieu.getPhongChieu().getId().toString();
            List<Ghe> gheList = new ArrayList<Ghe>();
            for (ChiTietHoaDonListDTO helpMeImTooTiredForThis : chiTietHoaDonList) {
                String maGhe = helpMeImTooTiredForThis.getMaGhe();
                Ghe queriedGhe = gheRepository.findByID_PhongChieuAndMaGhe(id_PhongChieu, maGhe).orElse(null);
                if (queriedGhe == null) {
                    //Kiểm tra ghế lần cuối.
                    // Nếu ghế đã được chọn thì ngừng tạo hoá đơn
                    // Chưa được chọn thì lập tức cho giữ ghế
                    if (queriedGhe.getTrangThaiGhe() != 0) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ghế " + queriedGhe.getMaGhe() + " đã được chọn");
                    }
                    queriedGhe.setTrangThaiGhe(2);//2 Tạm thời giữ ghế
                    gheRepository.save(queriedGhe);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ghế không tồn tại, ngừng tạo hoá đơn");
                }
                ;//Giữ ghế
                gheList.add(queriedGhe);
            }
            //Tạo hoá đơn rỗng
            HoaDon blankInvoice = createBlankInvoice(hoaDon, gheList);
            if (blankInvoice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khách hàng hoặc suất chiếu không tồn tại");
            }
            //Lưu vào DB để tí còn có ID mà xử lý

            //Thêm hoá đơn chi tiết
            boolean createdInvoiceDetail = createAndAddInvoiceDetailToInvoice(blankInvoice, gheList);
            if (!createdInvoiceDetail) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Lỗi khi tạo ChiTietHoaDon, ngừng tạo HoaDon");
            }


            HoaDon finalHoaDon = hoaDonRepository.findById(blankInvoice.getId()).orElse(null);
            if (finalHoaDon != null) {
                //Reverse Seat here, more checks
                //This step is extra, kinda not needed, but i'm doing it anyway
                //Just to be sure
                List<ChiTietHoaDon> finalList = chiTietHoaDonRepository.getChiTietHoaDonsByID_HoaDon(finalHoaDon.getId().toString());
                for (ChiTietHoaDon reverse : finalList) {
                    Ghe reverseGhe = reverse.getGhe();
                    if (reverseGhe != null) {
                        reverseGhe.setTrangThaiGhe(1);
                        gheRepository.save(reverseGhe);
                    }
                }
                revertSeatsAfter5Mins(finalHoaDon.getId());
                //Schedule a revert if not booked
                return ResponseEntity.status(HttpStatus.OK).body(finalHoaDon);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xử lý hoá đơn");
        }
        return suatchieuResponse;
    }


    private BigDecimal resetToOriginalPrice(Long hoaDonId) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElse(null);
        if (hoaDon == null) {
            return BigDecimal.ZERO;
        }


        BigDecimal originalPrice = chiTietHoaDonController.getTotalPrice(hoaDonId);
        hoaDon.setTongSoTien(originalPrice);
        hoaDonRepository.save(hoaDon);
        return originalPrice;
    }

    private HoaDon createBlankInvoice(AddHoaDonDTO hoaDon, List<Ghe> gheList) {
        String hoaDonPrefix = "HD00";
        //Tạo hoá đơn rỗng
        //Chứa các thông tin cơ bản
        HoaDon blankHoaDon = new HoaDon();
        KhachHang khachHang = khachHangRepository.findById(hoaDon.getKhachHangId()).orElse(null);
        if (khachHang == null) {
            return null;
        }
        SuatChieu suatChieu = suatChieuRepository.findById(hoaDon.getSuatChieuId()).orElse(null);
        if (suatChieu == null) {
            return null;
        }
        blankHoaDon.setKhachHang(khachHang);

        blankHoaDon.setSuatChieu(suatChieu);
        blankHoaDon.setVoucher(null);
        blankHoaDon.setChiTietHoaDonList(null);
        blankHoaDon.setThoiGianThanhToan(LocalDateTime.now());


        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Ghe ghe : gheList) {
            //Already checked whether the items exist
            totalPrice = totalPrice.add(ghe.getGiaTien());
        }
        blankHoaDon.setTongSoTien(totalPrice);
        int point = (totalPrice
                .multiply(BigDecimal.ONE
                        .divide(BigDecimal.TEN.multiply(BigDecimal.TEN)))).intValue();
        blankHoaDon.setDiem(point);
        blankHoaDon.setTrangThaiHoaDon(0);//Hoá đơn mới
        blankHoaDon.setMaHoaDon(hoaDonPrefix + hoaDonRepository.getMaxTableId());
        blankHoaDon.setPttt(null);
        blankHoaDon.setSoLuong(gheList.size());
        return hoaDonRepository.save(blankHoaDon);
    }


    @Operation(summary = "Áp dụng Voucher"
            , description = "Cập nhật TongSoTien sau khi áp dụng Voucher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity vừa khởi tao"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal_Server_Error")})
    @PutMapping("/price/voucher/{hoaDonId}")
    public ResponseEntity setVoucher(@PathVariable Long hoaDonId, @RequestParam String maVoucher) {
        //Tìm hoá đơn
        ResponseEntity hoaDonResponse = RepoUtility.findById(hoaDonId, hoaDonRepository);
        if (hoaDonResponse.getStatusCode().is2xxSuccessful()) {
            HoaDon hoaDon = (HoaDon) hoaDonResponse.getBody();

            Voucher oldVoucher = hoaDon.getVoucher();
            if (oldVoucher != null) {
                String appliedVoucher = hoaDon.getVoucher().getMaVoucher();
                hoaDon.setVoucher(null);//Xoá voucher đã áp dụng
                BigDecimal originalPrice = resetToOriginalPrice(hoaDonId);//Tính toán lại giá cũ
                if (originalPrice.compareTo(BigDecimal.ZERO) == 0) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Không thể lấy được giá gốc của HoaDon");
                }
                hoaDon.setTongSoTien(originalPrice);//Đặt về giá gốc, tí tính toán sau
                voucherController.increaseVoucherCount(appliedVoucher);//Do hoá đơn không dùng nữa, tăng lên 1
            }

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
                } else if (discountAmount.compareTo(BigDecimal.ZERO) < 0) {
                    return ResponseEntity.badRequest().body("Không đạt điều kiện áp dụng voucher");
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

    private boolean createAndAddInvoiceDetailToInvoice(HoaDon hoaDon, List<Ghe> gheList) {
        //Coder is having a cold and therefore low motivation
        //No more validation or constraint check to verify if hoaDon exists
        //Just.....do it
        for (Ghe ghe : gheList) {
            //Lượt qua danh sách Hoá đơn chi tiết
            //Tạo mới chi tiêt hoá đơn
            ChiTietHoaDon temp = chiTietHoaDonController.createBlankInvoiceDetail(ghe, hoaDon);
            if (temp == null) {
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

    @Operation(summary = "Tải PDF hoá đơn", description = "Nhận vào ID Hoá đơn và trả về PDF hoá đơn nếu tồn tại")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Invoice has not been paid"),
            @ApiResponse(responseCode = "404", description = "HoaDon not found or file not available"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/download/{idHoaDon}")
    public ResponseEntity displayPDF(@PathVariable Long idHoaDon) throws IOException {
        if (idHoaDon == null || StringUtils.isBlank(idHoaDon.toString())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("idHoaDon không hợp lệ");
        }
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElse(null);
        if (hoaDon == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy HoaDon");
        }
        String path = InvoiceGenerator.createInvoice(hoaDon);
        if (StringUtils.isBlank(path)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hoá đơn đã huỷ hoặc hết hạn");
        }
        File pdfFile = new File(path);
        if (pdfFile.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdfFile.getName() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(new FileInputStream(pdfFile)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
