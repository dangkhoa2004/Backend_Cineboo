package com.backend.cineboo.controller;

import com.backend.cineboo.entity.DanhSachHoanVe;
import com.backend.cineboo.repository.DanhSachHoanVeRepository;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/hoanve")
public class HoanVeController {
    @Autowired
    DanhSachHoanVeRepository danhSachHoanVeRepository;


    @Operation(summary = "Lấy danh sách các yêu cầu hoàn vé",
            description = "Lấy danh sách các yêu cầu hoàn vé. Nếu không có bản ghi thì sẽ trả về danh sách trống")
    @GetMapping("/get")
    public ResponseEntity get(){
        return ResponseEntity.ok(danhSachHoanVeRepository.findAll());
    }

    @Operation(summary = "Tìm yêu cầu",
            description = "Tìm yêu cầu hoàn vé dựa trên ID Yêu cầu")
    @GetMapping("/find/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        ResponseEntity response = RepoUtility.findById(id,danhSachHoanVeRepository);
        if(response.getStatusCode().is2xxSuccessful()){
            DanhSachHoanVe danhSachHoanVe = (DanhSachHoanVe) response.getBody();
            return ResponseEntity.status(HttpStatus.OK).body(danhSachHoanVe);
        }
        return response;
    }
}
