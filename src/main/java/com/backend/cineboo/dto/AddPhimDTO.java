package com.backend.cineboo.dto;

import com.backend.cineboo.entity.DanhSachTLPhim;
import com.backend.cineboo.entity.DoTuoi;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
public class AddPhimDTO {

    @NotBlank(message = "Thiếu tên phim")
    private String tenPhim;

    @NotBlank(message = "Thiếu ảnh phim")
    private String anhPhim;

    @NotBlank(message = "Thiếu diễn viên")
    private String dienVien;

    private List<Long> id_TheLoaiPhims;


    @Range(min = 1888, max = 2999,message = "Năm sản xuất phải từ 1888-2999")
    private Integer nam;

    private String noiDungMoTa;

    private String trailer;

    @NotNull(message = "Thiếu Ngày ra mắt")
    private Date ngayRaMat;

    @Range(min =1,message = "Thời lượng phải lớn hơn 1")
    private Integer thoiLuong;

    @NotBlank(message = "Thiếu Quốc gia")
    private String quocGia;

    @Column(name = "NOIDUNG")
    private String noiDung;

    @NotNull(message = "Thiếu giới hạn độ tuổi")
    private Long id_GioiHanDoTuoi;

    private Integer trangThai;

    @Column(name="DIEM")
    @DecimalMin(value = "0.0")
    private BigDecimal diem;
}
