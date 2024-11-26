package com.backend.cineboo.scheduledJobs;

import com.backend.cineboo.entity.PhongChieu;
import com.backend.cineboo.entity.SuatChieu;
import com.backend.cineboo.repository.SuatChieuRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class EndSuatChieuJob implements Job {

    @Autowired
    SuatChieuRepository suatChieuRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long suatChieuId = context.getMergedJobDataMap().getLong("id");
        SuatChieu suatChieu = suatChieuRepository.findById(suatChieuId).orElse(null);
        endSuatChieu(suatChieu);
        //Thêm logic dừng suất chiếu vào đây

        //Thêm logic dừng suất chiếu vào đây
    }
    private void endSuatChieu(SuatChieu suatChieu) {
        if(suatChieu!=null && suatChieu.getTrangThaiSuatChieu()==1){
            suatChieu.setTrangThaiSuatChieu(2);// 2 là suất chiếu kết thúc
            suatChieuRepository.save(suatChieu);
        }
    }
}


