package com.backend.cineboo.scheduledJobs;

import com.backend.cineboo.entity.SuatChieu;
import com.backend.cineboo.repository.SuatChieuRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class StartSuatChieuJob implements Job {

    @Autowired
    SuatChieuRepository suatChieuRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long suatChieuId = context.getMergedJobDataMap().getLong("id");
        SuatChieu suatChieu = suatChieuRepository.findById(suatChieuId).orElse(null);
        startSuatChieu(suatChieu);
        //Thêm logic bắt đầu suất chiếu vào đây

        //Thêm logic bắt đầu suất chiếu vào đây
    }
    private void startSuatChieu(SuatChieu suatChieu) {
        if(suatChieu!=null || suatChieu.getTrangThaiSuatChieu()==0){
            suatChieu.setTrangThaiSuatChieu(1);// 1 là suất chiếu bắt đầu/đang chiếu
            suatChieuRepository.save(suatChieu);
        }
    }
}

