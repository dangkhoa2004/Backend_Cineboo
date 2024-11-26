package com.backend.cineboo.scheduledJobs;

import com.backend.cineboo.entity.ChiTietHoaDon;
import com.backend.cineboo.entity.Ghe;
import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.repository.ChiTietHoaDonRepository;
import com.backend.cineboo.repository.GheRepository;
import com.backend.cineboo.repository.HoaDonRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class UnreserveGheJobs implements Job {
    @Autowired
    GheRepository gheRepository;

    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //Calling one job for each Ghe is inefficient
        // So imma do one for a whole list
        //nvm custom parameter is a pain
        //Passing id only
        Long hoaDonId = jobExecutionContext.getMergedJobDataMap().getLong("id");
        //Check if HoaDon is paid
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElse(null);
        if (hoaDon != null && hoaDon.getTrangThaiHoaDon() == 1) {
            //Only check against database since we are still working with db layer(or something?)
            //If HoaDon exists and is already paid
            //Then do nothing
            return;
        }
        // Get hoaDon again, just to be sure
        List<ChiTietHoaDon> revertList = chiTietHoaDonRepository.getChiTietHoaDonsByID_HoaDon(hoaDonId.toString());
        for (ChiTietHoaDon chiTietHoaDon : revertList) {
            Ghe revertGhe = chiTietHoaDon.getGhe();
            if (revertGhe != null) {
                revertGhe.setTrangThaiGhe(0);//0 là ghế chưa book
                gheRepository.save(revertGhe);
                System.out.println("Reverting Ghe" + revertGhe.getMaGhe());
            }
        }
    }
}
