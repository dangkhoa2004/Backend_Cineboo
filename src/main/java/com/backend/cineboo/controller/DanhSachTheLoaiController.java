package com.backend.cineboo.controller;

import com.backend.cineboo.entity.DanhSachTLPhim;
import com.backend.cineboo.repository.DanhSachTLPhimReposiory;
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
@RequestMapping("/dstl")
public class DanhSachTheLoaiController {

    @Autowired
    DanhSachTLPhimReposiory danhSachTLPhimReposiory;

    @GetMapping("/get")
    public ResponseEntity<List<DanhSachTLPhim>> get(){
        return ResponseEntity.ok((List<DanhSachTLPhim>)danhSachTLPhimReposiory.findAll());
    }
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, danhSachTLPhimReposiory);
        if (response.getStatusCode().is2xxSuccessful()) {
            DanhSachTLPhim danhSachTLPhim = (DanhSachTLPhim) response.getBody();
            danhSachTLPhim.setTrangThai(0);
            danhSachTLPhimReposiory.save(danhSachTLPhim);
            return ResponseEntity.status(HttpStatus.OK).body("Disable danhsachtheloai thành công");
        }
        return response;
    }
    @PutMapping("/add")
    public ResponseEntity add(@Valid @RequestBody DanhSachTLPhim danhSachTLPhim, BindingResult bindingResult){
        Map<String,String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        return ResponseEntity.ok(danhSachTLPhimReposiory.save(danhSachTLPhim));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody DanhSachTLPhim danhSachTLPhim, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, danhSachTLPhimReposiory);
        if (response.getStatusCode().is2xxSuccessful()) {
            DanhSachTLPhim toBeUpdated = (DanhSachTLPhim) response.getBody();
            toBeUpdated.setTheLoaiPhim(danhSachTLPhim.getTheLoaiPhim());
            toBeUpdated.setPhim(danhSachTLPhim.getPhim());
            toBeUpdated.setTrangThai(danhSachTLPhim.getTrangThai());
            //Lưu vào Database
            return ResponseEntity.status(HttpStatus.OK).body(danhSachTLPhimReposiory.save(toBeUpdated));
        }
        return response;
    }
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, danhSachTLPhimReposiory);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(danhSachTLPhimReposiory.save((DanhSachTLPhim) response.getBody()));
        }
        return response;
    }
}
