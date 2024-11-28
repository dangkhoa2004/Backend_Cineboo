package com.backend.cineboo.controller;

import com.backend.cineboo.dto.ReviewDTO;
import com.backend.cineboo.entity.Phim;
import com.backend.cineboo.entity.Review;
import com.backend.cineboo.repository.KhachHangRepository;
import com.backend.cineboo.repository.PhimRepository;
import com.backend.cineboo.repository.ReviewRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private PhimRepository phimRepository;

    @Operation(summary = "Trả về danh sách review đầy đủ")
    @GetMapping("/get")
    public ResponseEntity<List<Review>> get() {
        List<Review> reviews = new ArrayList<>(reviewRepository.findAll());
        return ResponseEntity.ok(reviews);
    }


    @Operation(summary = "Trả về danh sách review hợp lệ",
            description = "Trả về danh sách review có trạng thái 1.")
    @GetMapping("/get/valid")
    public ResponseEntity<List<Review>> getFiltered() {
        List<Review> reviews = reviewRepository.findAllByTrangThaiReview(1);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Disable a Review record",
            description = "Sets the Review status to disabled (trangThaiReview = 0) by ID.")
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, reviewRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Review review = (Review) response.getBody();
            review.setTrangThaiReview(0);
            reviewRepository.save(review);
            return ResponseEntity.status(HttpStatus.OK).body("Review disabled successfully");
        }
        return response;
    }

    @Operation(summary = "Add a new Review record",
            description = "Adds a new Review record to the system.")
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody ReviewDTO review, BindingResult bindingResult) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Long idKhachHang = review.getId_KhachHang();
        Long idPhim = review.getId_Phim();
        ResponseEntity khachHangResponse = RepoUtility.findById(idKhachHang,khachHangRepository);
        if(!khachHangResponse.getStatusCode().is2xxSuccessful()){
            return khachHangResponse;
        }
        ResponseEntity phimResponse = RepoUtility.findById(idPhim,phimRepository);
        if(!phimResponse.getStatusCode().is2xxSuccessful()){
            return phimResponse;
        }
        int sawTheMovie = reviewRepository.checkIfKhachHangHadViewedMovie(idKhachHang.toString());
        int duplicate = reviewRepository.checkDuplicate(idKhachHang.toString(),idPhim.toString());
        if(sawTheMovie==0){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn chưa xem phim này");
        }
        if(duplicate>0){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Đã bình luận trước đó");
        }
        Review addedReview = new Review();
        addedReview.setId_KhachHang(idKhachHang);
        addedReview.setId_Phim(idPhim);
        addedReview.setDanhGia(review.getDanhGia());
        addedReview.setBinhLuan(review.getBinhLuan());
        addedReview.setTrangThaiReview(1);//this is for hiding showing comment?
        addedReview = reviewRepository.save(addedReview);
        //Tính lại điểm
       BigDecimal average =  reviewRepository.averageRating(idPhim.toString());
       if(average!= null && average.compareTo(BigDecimal.ZERO)>=0){
           Phim phim = (Phim) phimResponse.getBody();
           phim.setDiem(average);
           phimRepository.save(phim);
       }
        return ResponseEntity.ok(addedReview);
    }

    @Operation(summary = "Update Review details",
            description = "Updates the Review record based on ID.")
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody Review review, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, reviewRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            Review toBeUpdated = (Review) response.getBody();
            toBeUpdated.setMaReview(review.getMaReview());
            toBeUpdated.setId_KhachHang(review.getId_KhachHang());
            toBeUpdated.setId_Phim(review.getId_Phim());
            toBeUpdated.setDanhGia(review.getDanhGia());
            toBeUpdated.setBinhLuan(review.getBinhLuan());
            toBeUpdated.setTrangThaiReview(review.getTrangThaiReview());
            return ResponseEntity.status(HttpStatus.OK).body(reviewRepository.save(toBeUpdated));
        }
        return response;
    }

    @Operation(summary = "Find Review by ID",
            description = "Finds a Review record by its ID.")
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, reviewRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        }
        return response;
    }
}
