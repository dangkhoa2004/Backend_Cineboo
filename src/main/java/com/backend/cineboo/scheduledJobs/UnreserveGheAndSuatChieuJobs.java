package com.backend.cineboo.scheduledJobs;

import com.backend.cineboo.entity.ChiTietHoaDon;
import com.backend.cineboo.entity.Ghe;
import com.backend.cineboo.entity.GheAndSuatChieu;
import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.repository.ChiTietHoaDonRepository;
import com.backend.cineboo.repository.GheAndSuatChieuRepository;
import com.backend.cineboo.repository.GheRepository;
import com.backend.cineboo.repository.HoaDonRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UnreserveGheAndSuatChieuJobs implements Job {
    @Autowired
    GheRepository gheRepository;

    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    GheAndSuatChieuRepository gheAndSuatChieuRepository;

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
            GheAndSuatChieu revertGheAndSuatChieu =chiTietHoaDon.getId_GheAndSuatChieu();
            if (revertGheAndSuatChieu != null) {
                revertGheAndSuatChieu.setTrangThaiGheAndSuatChieu(0);//0 là ghế chưa book
                gheAndSuatChieuRepository.save(revertGheAndSuatChieu);
                System.out.println("Reverting Ghe" + revertGheAndSuatChieu.getId_Ghe()+" Of SuatChieu "+revertGheAndSuatChieu.getId_SuatChieu());
            }
        }
    }
}
