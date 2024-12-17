package com.backend.cineboo.scheduledJobs;

import com.backend.cineboo.entity.TaiKhoan;
import com.backend.cineboo.repository.TaiKhoanRepository;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class InvalidateOTPJob implements Job {

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String email = context.getMergedJobDataMap().getString("email");

        invalidateOTP(email);

    }
    private void invalidateOTP(String  email) {
        if(!StringUtils.isBlank(email)){
            TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);
            if(taiKhoan!=null){
                taiKhoan.setOtp("");
                taiKhoanRepository.save(taiKhoan);
            }
        }
    }
}


