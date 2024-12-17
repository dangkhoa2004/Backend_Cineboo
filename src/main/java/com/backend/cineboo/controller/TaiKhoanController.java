package com.backend.cineboo.controller;

import com.backend.cineboo.dto.ResetPasswordDTO;
import com.backend.cineboo.entity.TaiKhoan;
import com.backend.cineboo.repository.TaiKhoanRepository;
import com.backend.cineboo.scheduledJobs.InvalidateOTPJob;
import com.backend.cineboo.utility.EmailHelper;
import com.backend.cineboo.utility.EntityValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Controller
@RequestMapping("/taikhoan")
public class TaiKhoanController {

    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    SecureRandom secureRandom = new SecureRandom();

    @Autowired
    Scheduler scheduler;

    @Operation(summary = "Gửi email đặt lại mật khẩu",
            description = "Mỗi lần gửi yêu cầu cách nhau 5 phút\n\n" +
                    "Backend sẽ không kiểm tra định dạng email")
    @PutMapping("/recovery/send")
    public ResponseEntity sendRecoveryEmail(@RequestParam("email") String emailAddress) {
        //OTP will either be sent if email is correct
        //Or it will not be sent if email is in wrong format
        //So no regex validation for email
        //Also because there are weird ass emails out there that regex will fail to catch anyway
        emailAddress = emailAddress.trim();
        if(StringUtils.isBlank(emailAddress)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Khong xac dinh duoc email truyen vao");
        }
        Integer otpExist = taiKhoanRepository.checkIfOTPExistsByEmail(emailAddress, "");
        if (otpExist != null && otpExist == 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vui long doi 5 phut truoc khi gui ma OTP moi");
        }
        String originalNumber = String.format("%06d", secureRandom.nextInt(999999));
        String hashedNumber = BCrypt.hashpw(originalNumber, BCrypt.gensalt());
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(emailAddress).orElse(null);
        taiKhoan.setOtp(hashedNumber);
       taiKhoan= taiKhoanRepository.save(taiKhoan);
       if(taiKhoan.getOtp()!=hashedNumber){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Loi luu OTP vao database");
       }
       EmailHelper emailHelper = new EmailHelper();
        try {
            emailHelper.sendEmail("huyvhpp02961@fpt.edu.vn", emailAddress, "Ma OTP dat lai mat khau CINEBOO", originalNumber);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Loi gui email dat lai mat khau. Vui long thu lai sau");
        }
        try {
                setSchedulerInvalidateOTP(emailAddress);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Loi tao su kien huy ma OTP");
        }
        return ResponseEntity.ok("Da gui ma OTP toi dia chi email: "+emailAddress);
    }

    @Operation(summary = "Đặt lại mật khẩu của tài khoaản đi liền với email",
            description = "\n\n" +
                    "Backend sẽ không kiểm tra định dạng email")
    @PutMapping("/recovery/reset-password")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, BindingResult bindingResult) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        String email = resetPasswordDTO.getEmail();
        String newPassword = resetPasswordDTO.getNewPassword();
        String retypePassword = resetPasswordDTO.getRetypePassword();
        if(StringUtils.isBlank(email)|| StringUtils.isBlank(retypePassword)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chua nhap vao mat khau");
        }
        if (StringUtils.compare(newPassword, retypePassword) != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mat khau khong trung khop");
        }
        String hashedOTP = taiKhoanRepository.getOTPByEmail(email).orElse(null);
        if (hashedOTP == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email nay khong co OTP");
        }
        boolean compareOTP = BCrypt.checkpw(resetPasswordDTO.getOtp(), hashedOTP);
        if (compareOTP) {
            TaiKhoan taiKhoanWithNewPassword = taiKhoanRepository.findByEmail(email).orElse(null);
            if (taiKhoanWithNewPassword == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Khong tim thay tai khoan ung voi email " + email);
            }
            String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            taiKhoanWithNewPassword.setMatKhau(newHashedPassword);
            taiKhoanWithNewPassword.setOtp("");
            taiKhoanRepository.save(taiKhoanWithNewPassword);
            return ResponseEntity.status(HttpStatus.OK).body("Doi mat khau thanh cong");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ma OTP khong chinh xac");
    }

    private void setSchedulerInvalidateOTP(String email) throws SchedulerException {
        Integer otpExist = taiKhoanRepository.checkIfOTPExistsByEmail(email, "");
        if (otpExist == null || otpExist != 1) {
            return;
        }
        Date startAtDate = Date.from(LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .plusMinutes(5)
                .toInstant());

        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomNumber = String.format("%06d", secureRandom.nextInt(999999));
        JobDetail invalidateOTPJob = JobBuilder.newJob(InvalidateOTPJob.class)
                .withIdentity("InvalidateOTPJob" + randomNumber+timestamp, "taiKhoanGroup")
                .build();


        SimpleTrigger invalidateOTPTrigger = newTrigger()
                .withIdentity("InvalidateOTPTrigger" + randomNumber+timestamp, "taiKhoanGroup")
                .forJob(invalidateOTPJob)
                .usingJobData("email", email)
                .startAt(startAtDate)
                .withSchedule(simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
        scheduler.scheduleJob(invalidateOTPJob, invalidateOTPTrigger);
        System.out.println("OTP will be invalid on " + startAtDate);
    }
}
