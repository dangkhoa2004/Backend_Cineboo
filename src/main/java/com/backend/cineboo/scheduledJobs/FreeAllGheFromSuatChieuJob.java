package com.backend.cineboo.scheduledJobs;

import com.backend.cineboo.entity.Ghe;
import com.backend.cineboo.entity.PhongChieu;
import com.backend.cineboo.entity.SuatChieu;
import com.backend.cineboo.repository.GheRepository;
import com.backend.cineboo.repository.SuatChieuRepository;
import org.apache.commons.collections.ListUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FreeAllGheFromSuatChieuJob implements Job {

    @Autowired
    GheRepository gheRepository;

    @Autowired
    SuatChieuRepository suatChieuRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Long suatChieuId = jobExecutionContext.getMergedJobDataMap().getLong("id");
        freeAllGheBookings(suatChieuId);
        //Other logic here

        // Other logic here
    }

    private void freeAllGheBookings(Long idSuatChieu) {
        SuatChieu suatChieu = suatChieuRepository.findById(idSuatChieu).orElse(null);
        if (suatChieu != null&& suatChieu.getTrangThaiSuatChieu()==2) {
        //Only free Ghes when suatChieu has trangThaiSuatChieu(2)-Already ended
            List<Ghe> gheList = gheRepository.findByID_PhongChieu(suatChieu.getPhongChieu().getId().toString());
            if (gheList != null && !gheList.isEmpty()) {
                for (Ghe freeGhe : gheList) {
                    freeGhe.setTrangThaiGhe(0);
                    gheRepository.save(freeGhe);
                }
            }
        }
    }
}
