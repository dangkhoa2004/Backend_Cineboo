package com.backend.cineboo.controller;

import com.backend.cineboo.dto.AddGheAndSuatChieuDTO;
import com.backend.cineboo.dto.AddMultipleSuatChieuDTO;
import com.backend.cineboo.dto.AddSuatChieuDTO;
import com.backend.cineboo.entity.*;
import com.backend.cineboo.repository.*;
import com.backend.cineboo.scheduledJobs.EndSuatChieuJob;
import com.backend.cineboo.scheduledJobs.FreeAllGheFromSuatChieuJob;
import com.backend.cineboo.scheduledJobs.StartSuatChieuJob;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.apache.commons.collections.MapUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Controller
@RequestMapping("/suatchieu")
public class SuatChieuController {

    @Autowired
    Scheduler scheduler;

    @Autowired
    SuatChieuRepository suatChieuRepository;

    @Autowired
    PhimRepository phimRepository;

    @Autowired
    GheAndSuatChieuRepository gheAndSuatChieuRepository;

    @Autowired
    PhongChieuRepository phongChieuRepository;

    @Autowired
    GheRepository gheRepository;

    @Autowired
    GheAndSuatChieuController gheAndSuatChieuController;
    private final String idPrefix = "MSC00";

    /**
     * Lấy tất cả các suất chiếu.
     *
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
     * @param suatChieu     Thông tin suất chiếu để cập nhật.
     * @param bindingResult Kết quả xác thực.
     * @param id            ID suất chiếu để cập nhật.
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
        Phim phim = phimRepository.findById(suatChieu.getPhim().getId()).orElse(null);
        if (phim == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Phim không tồn tại dựa");
        }
        PhongChieu phongChieu = phongChieuRepository.findById(suatChieu.getId_PhongChieu()).orElse(null);
        if (phongChieu == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phòng chiếu");
        }

        ResponseEntity response = RepoUtility.findById(id, suatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            SuatChieu toBeUpdated = (SuatChieu) response.getBody();
            toBeUpdated.setMaSuatChieu(idPrefix + suatChieuRepository.getMaxTableId());
            toBeUpdated.setThoiGianChieu(suatChieu.getThoiGianChieu());
            toBeUpdated.setTrangThaiSuatChieu(suatChieu.getTrangThaiSuatChieu());
            toBeUpdated.setPhim(suatChieu.getPhim());
            toBeUpdated.setId_PhongChieu(suatChieu.getId_PhongChieu());
            return ResponseEntity.status(HttpStatus.OK).body(suatChieuRepository.save(toBeUpdated));
        }
        return response;
    }

    /**
     * Thêm suất chiếu mới.
     *
     * @param addSuatChieuDTO Thông tin suất chiếu mới.
     * @param bindingResult   Kết quả xác thực.
     * @return SuatChieu đã thêm hoặc phản hồi lỗi.
     */
    @Operation(summary = "Thêm suất chiếu mới", description = "Thêm một suất chiếu mới vào hệ thống")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thêm suất chiếu thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ, có lỗi xác thực")
    })
    @Transactional
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody AddSuatChieuDTO addSuatChieuDTO, BindingResult bindingResult) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Phim phim = phimRepository.findById(addSuatChieuDTO.getId_Phim()).orElse(null);
        if (phim == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phim");
        }
        PhongChieu phongChieu = phongChieuRepository.findById(addSuatChieuDTO.getId_PhongChieu()).orElse(null);
        if (phongChieu == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phòng chiếu");
        }
        Integer exists = gheRepository.checkIfGheOfPhongChieuExists(addSuatChieuDTO.getId_PhongChieu().toString());
        if (exists == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Phòng chiếu " + phongChieu.getMaPhong() + " hiện tại chưa có ghế");
        }

        LocalDateTime thoiGianChieu = addSuatChieuDTO.getThoiGianChieu();

        SuatChieu suatChieu = new SuatChieu();
        suatChieu.setMaSuatChieu(idPrefix + (suatChieuRepository.getMaxTableId() + 1));
        suatChieu.setThoiGianChieu(thoiGianChieu);
        suatChieu.setPhim(phim);
        suatChieu.setId_PhongChieu(addSuatChieuDTO.getId_PhongChieu());
        suatChieu.setTrangThaiSuatChieu(0);
        //SHOULD PROBABLY IMPLEMENT A CHECK HERE
        //IF THOIGIANCHIEU<= (LocalDateTime.now() + phim.getThoiLuong())
        //Then allow ADDING new SuatChieu
        //Basically, only when Phim finished its airing
        //Will we allow another airing of the same Phim, in the same PhongChieu
        //For now, not implemented
        //just to be sure create new suatchieu


        List<SuatChieu> suatChieuList = suatChieuRepository.getAllAfterOrOnTodayAndById_PhongChieu(suatChieu.getId_PhongChieu().toString());
        boolean allowNewSuatChieu = false;
        for (int i = 0; i < suatChieuList.size(); i++) {
            if (i == 0) { //Trước suất chiếu cũ nhất
                if (thoiGianChieu.plusMinutes(suatChieu.getPhim().getThoiLuong())
                        .isBefore(suatChieuList.get(0).getThoiGianChieu())) {
                    allowNewSuatChieu = true;
                    break;
                }
            } else if (i == suatChieuList.size() - 1) { // Sau suất chiếu mới nhất
                SuatChieu lastSuatChieu = suatChieuList.get(i);
                if (thoiGianChieu
                        .isAfter(lastSuatChieu.getThoiGianChieu()
                                .plusMinutes(lastSuatChieu.getPhim().getThoiLuong()))
                ) {
                    allowNewSuatChieu = true;
                    break;
                }
            } else { // Nằm giữa các suất chiếu
                SuatChieu suatChieuBefore = suatChieuList.get(i - 1);
                SuatChieu suatChieuAfter = suatChieuList.get(i);
                if (thoiGianChieu.isAfter(suatChieuBefore.getThoiGianChieu().plusMinutes(suatChieuBefore.getPhim().getThoiLuong()))
                        && thoiGianChieu.isBefore(suatChieuAfter.getThoiGianChieu())) {
                    allowNewSuatChieu = true;
                    break;
                }
            }
        }
        if (allowNewSuatChieu) {
            try {
                SuatChieu newlyAddedSuatChieu = suatChieuRepository.save(suatChieu);
                // Đã có suất chiếu sẽ phải có ghế
                //Bắt đầu thêm Ghế vào Suất chiếu
                AddGheAndSuatChieuDTO addGheAndSuatChieuDTO = new AddGheAndSuatChieuDTO();
                addGheAndSuatChieuDTO.setId_SuatChieu(newlyAddedSuatChieu.getId());
                addGheAndSuatChieuDTO.setId_PhongChieu(addSuatChieuDTO.getId_PhongChieu());
                //Should directly cal GheAndSuatChieuController.add()
                //Someone pls do it, im a bit busy
                BindingResult bindingResultGheAndSuatChieu = new BindException(new Object(), "GheAndSuatChieu");
                gheAndSuatChieuController.add(addGheAndSuatChieuDTO, bindingResultGheAndSuatChieu);

                return ResponseEntity.status(HttpStatus.OK).body(suatChieu);
            } catch (DuplicateKeyException duplicateKeyException) {
                //Just in case users somehow are able to insert duplicate unique keys
                duplicateKeyException.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Đã tồn tại phim trong khung giờ này");
            }

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có khung giờ trống cho suatchieu mới");
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
        ResponseEntity response = RepoUtility.findById(id, suatChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(suatChieuRepository.save((SuatChieu) response.getBody()));
        }
        return response;
    }

    /**
     * Tìm kiếm suất chiếu theo tên cột và giá trị của cột.
     *
     * @param columnName Tên cột để tìm kiếm.
     * @param value      Giá trị để tìm kiếm.
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

    @Operation(summary = "Đặt lịch chiếu tự động",
            description = "SuatChieu tự động bắt đầu dựa trên thoiGianChieu\n\n" +
                    "SuatChieu tự động vô hiệu hoá suất chiếu sau khi chiếu xong\n\n" +
                    "Mở hết các ghế thuộc suất chiếu khi chiếu xong")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm kiếm thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ, cột không tồn tại"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bản ghi"),
            @ApiResponse(responseCode = "500", description = "Lỗi máy chủ nội bộ")
    })
    @GetMapping("schedule/{idSuatChieu}")
    public ResponseEntity schedule(@PathVariable Long idSuatChieu) {
        ResponseEntity response = RepoUtility.findById(idSuatChieu, suatChieuRepository);
        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                setSchedulerStartSuatChieu(idSuatChieu);
                setSchedulerEndSuatchieu(idSuatChieu);
                setSchedulerFreeAllGhesFromSuatChieu(idSuatChieu);
                return ResponseEntity.status(HttpStatus.OK).body("Lên lịch Suất chiếu thành công");
            }
            return response;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Suất chiếu đã được lên lịch(trùng lặp)");
        }
    }

    private void setSchedulerEndSuatchieu(Long idSuatChieu) throws SchedulerException {
        SuatChieu suatChieu = suatChieuRepository.findById(idSuatChieu).orElse(null);

        if (suatChieu != null) {

            LocalDateTime thoiGianChieu = suatChieu.getThoiGianChieu();
            int thoiLuong = suatChieu.getPhim().getThoiLuong();

            Date startAtDate = Date.from(thoiGianChieu
                    .atZone(ZoneId.systemDefault())
                    .plusMinutes(Long.valueOf(thoiLuong))
                    .toInstant());


            JobDetail endSuatChieuJob = JobBuilder.newJob(EndSuatChieuJob.class)
                    .withIdentity("EndSuatChieuJob" + idSuatChieu, "suatchieuGroup")
                    .build();


            SimpleTrigger endSuatChieuTrigger = newTrigger()
                    .withIdentity("EndSuatChieuTrigger" + idSuatChieu, "suatchieuGroup")
                    .forJob(endSuatChieuJob)
                    .usingJobData("id", idSuatChieu)
                    .startAt(startAtDate)
                    .withSchedule(simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();
            scheduler.scheduleJob(endSuatChieuJob, endSuatChieuTrigger);//Kết thúc suất chiếu
            System.out.println("SuatChieu will end itself on " + startAtDate);
            // Keep the app running to observe the scheduler

        }
    }

    private void setSchedulerFreeAllGhesFromSuatChieu(Long idSuatChieu) throws SchedulerException {
        SuatChieu suatChieu = suatChieuRepository.findById(idSuatChieu).orElse(null);

        if (suatChieu != null) {
            //Tránh trường hợp kết thúc suất chiếu chưa bắt đầu

            LocalDateTime thoiGianChieu = suatChieu.getThoiGianChieu();
            int thoiLuong = suatChieu.getPhim().getThoiLuong();

            Date startAtDate = Date.from(thoiGianChieu
                    .atZone(ZoneId.systemDefault())
                    .plusMinutes(Long.valueOf(thoiLuong))
                    .plusMinutes(5)//To be safe, disable Ghes 5 minutes after SuatChieu is done
                    .toInstant());

            JobDetail freeAllGheFromSuatChieuJob = JobBuilder.newJob(FreeAllGheFromSuatChieuJob.class)
                    .withIdentity("FreeAllGheFromSuatChieuJob" + idSuatChieu, "suatchieuGroup")
                    .build();

            SimpleTrigger freeAllGheFromSuatChieuTrigger = newTrigger()
                    .withIdentity("FreeAllGheFromSuatChieuTrigger" + idSuatChieu, "suatchieuGroup")
                    .forJob(freeAllGheFromSuatChieuJob)
                    .usingJobData("id", idSuatChieu)
                    .startAt(startAtDate)
                    .withSchedule(simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();
            scheduler.scheduleJob(freeAllGheFromSuatChieuJob, freeAllGheFromSuatChieuTrigger);//setTrangThai tất cả ghế về 0
            System.out.println("All Ghes of SuatChieu " + idSuatChieu + " will be open on " + startAtDate);

        }
    }

    private void setSchedulerStartSuatChieu(Long idSuatChieu) throws SchedulerException {
        SuatChieu suatChieu = suatChieuRepository.findById(idSuatChieu).orElse(null);

        if (suatChieu != null && suatChieu.getTrangThaiSuatChieu() == 0) {
            //Chỉ bắt đầu nếu chưa chiếu hay kết thúc
            LocalDateTime thoiGianChieu = suatChieu.getThoiGianChieu();

            Date startAtDate = Date.from(thoiGianChieu
                    .atZone(ZoneId.systemDefault())
                    .toInstant());

            JobDetail startSuatChieuJob = JobBuilder.newJob(StartSuatChieuJob.class)
                    .withIdentity("StartSuatChieuJob" + idSuatChieu, "suatchieuGroup")
                    .build();

            SimpleTrigger startSuatChieuTrigger = newTrigger()
                    .withIdentity("StartSuatChieuTrigger" + idSuatChieu, "suatchieuGroup")
                    .forJob(startSuatChieuJob)
                    .usingJobData("id", idSuatChieu)
                    .startAt(startAtDate)
                    .withSchedule(simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();

            scheduler.scheduleJob(startSuatChieuJob, startSuatChieuTrigger);//setTrangThai tất cả ghế về 0
            System.out.println("SuatChieu will start on " + startAtDate);
        }
    }


    /**
     * Thêm nhiều suất chiếu mới.
     *
     * @param addMultipleSuatChieuDTO Thông tin suất chiếu mới.
     * @param bindingResult           Kết quả xác thực.
     * @return SuatChieu đã thêm hoặc phản hồi lỗi.
     */
    @Operation(summary = "Thêm nhiều suất chiếu mới", description = "Thêm nhiều suất chiếu mới vào hệ thống")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thêm suất chiếu thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ, có lỗi xác thực")
    })
    @PostMapping("/add/multiple")
    @Transactional
    public ResponseEntity addMultiple(@Valid @RequestBody AddMultipleSuatChieuDTO addMultipleSuatChieuDTO, BindingResult bindingResult) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        List<String> maSuatChieus = new ArrayList<>();
        Phim phim = phimRepository.findById(addMultipleSuatChieuDTO.getId_Phim()).orElse(null);
        if (phim == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phim");
        }
        PhongChieu phongChieu = phongChieuRepository.findById(addMultipleSuatChieuDTO.getId_PhongChieu()).orElse(null);
        if (phongChieu == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phòng chiếu");
        }
        Integer exists = gheRepository.checkIfGheOfPhongChieuExists(addMultipleSuatChieuDTO.getId_PhongChieu().toString());
        if (exists == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Phòng chiếu " + phongChieu.getMaPhong() + " hiện tại chưa có ghế");
        }
        for (int i = 0; i < addMultipleSuatChieuDTO.getThoiGianChieuList().size(); i++) {
            LocalDateTime thoiGianChieu = addMultipleSuatChieuDTO.getThoiGianChieuList().get(i);

            SuatChieu suatChieu = new SuatChieu();
            //Could totally use Virtual property in database to automatically setMaSuatChieu
            //Hopefully this will be fixed by someone in the future
            //For now, we use ancient caveman technique
            suatChieu.setMaSuatChieu(idPrefix + (suatChieuRepository.getMaxTableId() + (i + 1)));
            suatChieu.setThoiGianChieu(thoiGianChieu);
            suatChieu.setPhim(phim);
            suatChieu.setId_PhongChieu(addMultipleSuatChieuDTO.getId_PhongChieu());
            suatChieu.setTrangThaiSuatChieu(0);

            List<SuatChieu> suatChieuList = suatChieuRepository.getAllAfterOrOnTodayAndById_PhongChieu(suatChieu.getId_PhongChieu().toString());
            boolean allowNewSuatChieu = false;
            for (int x = 0; x < suatChieuList.size(); x++) {
                if (x == 0) { //Trước suất chiếu cũ nhất
                    if (thoiGianChieu.plusMinutes(suatChieu.getPhim().getThoiLuong())
                            .isBefore(suatChieuList.get(0).getThoiGianChieu())) {
                        allowNewSuatChieu = true;
                        break;
                    }
                    if (suatChieuList.size() == 1) {
                        SuatChieu lastSuatChieu = suatChieuList.get(x);
                        if (thoiGianChieu
                                .isAfter(lastSuatChieu.getThoiGianChieu()
                                        .plusMinutes(lastSuatChieu.getPhim().getThoiLuong()))
                        ) {
                            allowNewSuatChieu = true;
                            break;
                        }
                    }
                } else if (x == suatChieuList.size() - 1) { // Sau suất chiếu mới nhất
                    SuatChieu lastSuatChieu = suatChieuList.get(x);
                    if (thoiGianChieu
                            .isAfter(lastSuatChieu.getThoiGianChieu()
                                    .plusMinutes(lastSuatChieu.getPhim().getThoiLuong()))
                    ) {
                        allowNewSuatChieu = true;
                        break;
                    }
                    SuatChieu suatChieuBefore = suatChieuList.get(x - 1);
                    SuatChieu suatChieuAfter = suatChieuList.get(x);
                    if (thoiGianChieu.isAfter(suatChieuBefore.getThoiGianChieu().plusMinutes(suatChieuBefore.getPhim().getThoiLuong()))
                            && thoiGianChieu.plusMinutes(suatChieu.getPhim().getThoiLuong()).isBefore(suatChieuAfter.getThoiGianChieu())) {
                        allowNewSuatChieu = true;
                        break;
                    }
                } else { // Nằm giữa các suất chiếu
                    SuatChieu suatChieuBefore = suatChieuList.get(x - 1);
                    SuatChieu suatChieuAfter = suatChieuList.get(x);
                    if (thoiGianChieu.isAfter(suatChieuBefore.getThoiGianChieu().plusMinutes(suatChieuBefore.getPhim().getThoiLuong()))
                            && thoiGianChieu.plusMinutes(suatChieu.getPhim().getThoiLuong()).isBefore(suatChieuAfter.getThoiGianChieu())) {
                        allowNewSuatChieu = true;
                        break;
                    }
                }
            }
            if (allowNewSuatChieu) {
                try {
                    SuatChieu newlyAddedSuatChieu = suatChieuRepository.save(suatChieu);
                    // Đã có suất chiếu sẽ phải có ghế
                    //Bắt đầu thêm Ghế vào Suất chiếu
                    AddGheAndSuatChieuDTO addGheAndSuatChieuDTO = new AddGheAndSuatChieuDTO();
                    addGheAndSuatChieuDTO.setId_SuatChieu(newlyAddedSuatChieu.getId());
                    addGheAndSuatChieuDTO.setId_PhongChieu(addMultipleSuatChieuDTO.getId_PhongChieu());
                    BindingResult bindingResultGheAndSuatChieu = new BindException(new Object(), "GheAndSuatChieu");
                    gheAndSuatChieuController.add(addGheAndSuatChieuDTO, bindingResultGheAndSuatChieu);
                    maSuatChieus.add(suatChieu.getMaSuatChieu());
                } catch (DuplicateKeyException duplicateKeyException) {
                    //Just in case users somehow are able to insert duplicate unique keys
                    duplicateKeyException.printStackTrace();
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Đã tồn tại phim trong khung giờ này");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Thời gian chiếu không hợp lệ(" + thoiGianChieu + "). Đã có suất chiếu tồn tại trong khung giờ này");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Thêm thành công các suất chiếu: " + Arrays.toString(maSuatChieus.toArray()));
    }
}