package com.backend.cineboo.scheduledJobs;

import com.backend.cineboo.entity.GheAndSuatChieu;
import com.backend.cineboo.entity.SuatChieu;
import com.backend.cineboo.repository.GheAndSuatChieuRepository;
import com.backend.cineboo.repository.SuatChieuRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FreeAllGheFromSuatChieuJob implements Job {



    @Autowired
    SuatChieuRepository suatChieuRepository;

    @Autowired
    GheAndSuatChieuRepository gheAndSuatChieuRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Long suatChieuId = jobExecutionContext.getMergedJobDataMap().getLong("id");
        freeAllGheBookings(suatChieuId);
        //Other logic here

        // Other logic here
    }

    private void freeAllGheBookings(Long idSuatChieu) {
        SuatChieu suatChieu = suatChieuRepository.findById(idSuatChieu).orElse(null);
        if (suatChieu != null && suatChieu.getTrangThaiSuatChieu()==2) {
        //Only free Ghes when suatChieu has trangThaiSuatChieu(2)-Already ended
            List<GheAndSuatChieu> gheAndSuatChieuList = gheAndSuatChieuRepository.findByID_SuatChieu(suatChieu.getId().toString());
            if (gheAndSuatChieuList != null && !gheAndSuatChieuList.isEmpty()) {
                for (GheAndSuatChieu freeGheAndSuatChieu : gheAndSuatChieuList) {
                    //Tất cả các ghế liên quan đến suất chiếu sẽ bị vô hiệu hoá
                    //chuyển trạng thái tất cả ghế lên 1
                    freeGheAndSuatChieu.setTrangThaiGheAndSuatChieu(1);
                    gheAndSuatChieuRepository.save(freeGheAndSuatChieu);
                }
            }
        }
    }
}
