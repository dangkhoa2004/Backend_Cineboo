package com.backend.cineboo.controller;

import com.backend.cineboo.entity.RevenuePerMonth;
import com.backend.cineboo.entity.RevenuePerMovie;
import com.backend.cineboo.entity.UserSpentAmount;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/thongke")
public class ThongKeController {
    @Autowired
    private EntityManager entityManager;

    @Operation(summary = "Thống kê doanh thu trên Phim",
            description = "Dùng cho Pie Chart")
    @GetMapping("/pie")
    public ResponseEntity pie(){
        String sql = "SELECT\n" +
                "    p.TenPhim,                      -- Movie name\n" +
                "    COUNT(hd.ID_Phim) AS 'Lượt mua', -- Purchase count\n" +
                "    SUM(hd.TongSoTien) AS 'Doanh thu', -- Total revenue\n" +
                "    GROUP_CONCAT(t.TenTheLoai) AS 'TheLoai',\n" +
                "    p.Nam as 'Nam'-- Concatenate all genres into a single string\n" +
                "FROM hoadon hd\n" +
                "         JOIN phim p ON p.ID = hd.ID_Phim\n" +
                "         JOIN danhsachtlphim dstl ON p.ID = dstl.ID_Phim\n" +
                "         JOIN theloaiphim t ON dstl.ID_TLPhim = t.ID\n" +
                "WHERE hd.TrangThaiHoaDon = :paid\n" +
                "GROUP BY (hd.ID_Phim)\n" +
                "ORDER BY COUNT(hd.ID_Phim) DESC;\n";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("paid",1);
        List<Object[]> rows = query.getResultList();
        List<RevenuePerMovie> result = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            RevenuePerMovie revenuePerMovie = new RevenuePerMovie();
            revenuePerMovie.setTenPhim((String) row[0]);
            revenuePerMovie.setLuotMua((Long) row[1]);
            revenuePerMovie.setDoanhThu((BigDecimal) row[2]);
            String theLoais = (String)row[3];
            String[] arr = theLoais.split(",");
            List<String> theLoaiList = new ArrayList<>();
            for(int i=0;i<arr.length;i++){
                if (!theLoaiList.contains(arr[i])) {
                    theLoaiList.add(arr[i]);
                }
            }
            revenuePerMovie.setTheLoai(theLoaiList);
            revenuePerMovie.setNam((Integer) row[4]);
            result.add(revenuePerMovie);
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Thống kê doanh thu trên người dùng",
            description = "Dùng cho Scatter Chart")
    @GetMapping("/scatter")
    public ResponseEntity scatter(){
        String sql = "SELECT kh.ID,\n" +
                "    CONCAT(kh.ho,' ',kh.TenDem,' ',kh.Ten) as Ten,\n" +
                "       tk.MaTaiKhoan,\n" +
                "       kh.gioiTinh,\n" +
                "       kh.Diem,\n" +
                "       SUM(h.TongSoTien) AS TongSoTien\n" +
                "FROM khachhang kh\n" +
                "         JOIN taikhoan tk ON tk.ID = kh.ID_TaiKhoan\n" +
                "         JOIN hoadon h ON kh.ID = h.ID_KhachHang\n" +
                "GROUP BY kh.ID, tk.MaTaiKhoan\n" +
                "ORDER BY TongSoTien DESC;";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();
        List<UserSpentAmount> result = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            UserSpentAmount userSpentAmount = new UserSpentAmount();
            userSpentAmount.setId((Integer) row[0]);
            userSpentAmount.setTen((String) row[1]);
            userSpentAmount.setMaTaiKhoan((String) row[2]);
            userSpentAmount.setGioiTinh((Integer) row[3]);
            userSpentAmount.setDiem((Integer) row[4]);
            userSpentAmount.setTongSoTien((BigDecimal) row[5]);
            result.add(userSpentAmount);
        }
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Thống kê doanh thu theo từng tháng theo năm tuỳ chọn",
            description = "Dùng cho Bar Chart")
    @GetMapping("/bar/{year}")
    public ResponseEntity bar(@PathVariable String year){
        if(!year.matches("^[12][0-9]{3}$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sai định dạng năm");
        }
        String sql = "WITH Months AS (\n" +
                "    SELECT 'Tháng 1' AS Month, 1 AS MonthOrder\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 2', 2\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 3', 3\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 4', 4\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 5', 5\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 6', 6\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 7', 7\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 8', 8\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 9', 9\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 10', 10\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 11', 11\n" +
                "    UNION ALL\n" +
                "    SELECT 'Tháng 12', 12\n" +
                "),\n" +
                "     SeasonCategories AS (\n" +
                "         SELECT\n" +
                "             hd.ID_PHIM,\n" +
                "             hd.ID_KhachHang,\n" +
                "             hd.TongSoTien,\n" +
                "             MONTH(hd.thoiGianThanhToan) AS Thang\n" +
                "         FROM hoadon hd\n" +
                "         WHERE hd.ID_PHIM IS NOT NULL\n" +
                "           AND hd.ID_KhachHang IS NOT NULL\n" +
                "           AND YEAR(hd.thoiGianThanhToan) = :year \n" +
                "     )\n" +
                "SELECT\n" +
                "    m.Month,\n" +
                "    COALESCE(t.TotalInvoices, 0) AS TotalInvoices,\n" +
                "    COALESCE(t.TotalAmount, 0) AS TotalAmount\n" +
                "FROM Months m\n" +
                "         LEFT JOIN (\n" +
                "    SELECT\n" +
                "        Thang,\n" +
                "        COUNT(*) AS TotalInvoices,\n" +
                "        SUM(TongSoTien) AS TotalAmount\n" +
                "    FROM SeasonCategories\n" +
                "    GROUP BY Thang\n" +
                ") t ON m.MonthOrder = t.Thang\n" +
                "ORDER BY m.MonthOrder;\n;";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("year",year);
        List<Object[]> rows = query.getResultList();
        List<RevenuePerMonth> result = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            RevenuePerMonth revenuePerMonth = new RevenuePerMonth();
            revenuePerMonth.setMonth((String) row[0]);
            revenuePerMonth.setTotalInvoices((Long) row[1]);
            revenuePerMonth.setTotalAmount((BigDecimal) row[2]);
            result.add(revenuePerMonth);
        }
        return ResponseEntity.ok(result);
    }
}
