package com.backend.cineboo.controller;

import com.backend.cineboo.entity.Ghe;
import com.backend.cineboo.entity.PhongChieu;
import com.backend.cineboo.repository.GheRepository;
import com.backend.cineboo.repository.PhongChieuRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import jakarta.validation.Valid;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/phongchieu")
public class PhongChieuController {

    @Autowired
    PhongChieuRepository phongChieuRepository;





    @GetMapping("/get")
    public ResponseEntity<List<PhongChieu>> get(){
        return ResponseEntity.ok((List<PhongChieu>)phongChieuRepository.findAll());
    }
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, phongChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            PhongChieu phongChieu = (PhongChieu) response.getBody();
            phongChieuRepository.save(phongChieu);
            return ResponseEntity.status(HttpStatus.OK).body("Disable danhsachtheloai thành công");
        }
        return response;
    }
    @PutMapping("/add")
    public ResponseEntity add(@Valid @RequestBody PhongChieu phongChieu, BindingResult bindingResult){
        Map<String,String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        return ResponseEntity.ok(phongChieuRepository.save(phongChieu));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody PhongChieu phongChieu, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, phongChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            PhongChieu toBeUpdated = (PhongChieu) response.getBody();
            toBeUpdated.setTongSoGhe(phongChieu.getTongSoGhe());
            toBeUpdated.setMaPhong(phongChieu.getMaPhong());
            toBeUpdated.setTrangThaiPhongChieu(phongChieu.getTrangThaiPhongChieu());
            //Lưu vào Database
            return ResponseEntity.status(HttpStatus.OK).body(phongChieuRepository.save(toBeUpdated));
        }
        return response;
    }
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, phongChieuRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(phongChieuRepository.save((PhongChieu) response.getBody()));
        }
        return response;
    }
}
