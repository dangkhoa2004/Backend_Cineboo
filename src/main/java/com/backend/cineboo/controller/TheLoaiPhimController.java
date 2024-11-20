package com.backend.cineboo.controller;

import com.backend.cineboo.dto.AddTheLoaiPhimDTO;
import com.backend.cineboo.entity.TheLoaiPhim;
import com.backend.cineboo.repository.TheLoaiPhimRepository;
import com.backend.cineboo.utility.EntityValidator;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/theloaiphim")
@OpenAPIDefinition(
        info = @Info(
                title = "TheLoaiPhimAPI",
                version = "1.0",
                description = "CRUD operations for TheLoaiPhim entity"
        ))
public class TheLoaiPhimController {

    @Autowired
    private TheLoaiPhimRepository theLoaiPhimRepository;

    @Operation(summary = "Get all TheLoaiPhim records", description = "Returns a list of all TheLoaiPhim records in the system.")
    @GetMapping("/get")
    public ResponseEntity<List<TheLoaiPhim>> getAll() {
        List<TheLoaiPhim> theLoaiPhims = theLoaiPhimRepository.findAll();
        return ResponseEntity.ok(theLoaiPhims);
    }

    @Operation(summary = "Disable a TheLoaiPhim record", description = "Sets the TheLoaiPhim status to disabled (trangThai = 0) by ID.")
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, theLoaiPhimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            TheLoaiPhim theLoaiPhim = (TheLoaiPhim) response.getBody();
            theLoaiPhim.setTrangThai(0);
            theLoaiPhimRepository.save(theLoaiPhim);
            return ResponseEntity.status(HttpStatus.OK).body("Disable TheLoaiPhim thành công");
        }
        return response;
    }

    @Operation(summary = "Enable a TheLoaiPhim record", description = "Sets the TheLoaiPhim status to enabled (trangThai = 1) by ID.")
    @PutMapping("/enable/{id}")
    public ResponseEntity enable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, theLoaiPhimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            TheLoaiPhim theLoaiPhim = (TheLoaiPhim) response.getBody();
            theLoaiPhim.setTrangThai(1);
            theLoaiPhimRepository.save(theLoaiPhim);
            return ResponseEntity.status(HttpStatus.OK).body("Enable TheLoaiPhim thành công");
        }
        return response;
    }

    @Operation(summary = "Add a new TheLoaiPhim record", description = "Adds a new TheLoaiPhim record to the system.")
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody AddTheLoaiPhimDTO theLoaiPhimDTO, BindingResult bindingResult) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        String name = theLoaiPhimDTO.getTenTheLoai();
        TheLoaiPhim duplicate = theLoaiPhimRepository.checkDuplicate(name).orElse(null);
        if(duplicate!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Đã tồn tại thể loại phim "+name);
        }
        TheLoaiPhim theLoaiPhim = new TheLoaiPhim();
        String prefix = "TL00";
        theLoaiPhim.setMaTLPhim(prefix + theLoaiPhimRepository.getMaxTableId());
        theLoaiPhim.setTenTheLoai(name);
        theLoaiPhim.setTrangThai(0);
        theLoaiPhim = theLoaiPhimRepository.save(theLoaiPhim);
        return ResponseEntity.ok(theLoaiPhim);
    }

    @Operation(summary = "Update TheLoaiPhim details", description = "Updates the TheLoaiPhim record based on ID.")
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody TheLoaiPhim theLoaiPhim, BindingResult bindingResult, @PathVariable Long id) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, theLoaiPhimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            TheLoaiPhim toBeUpdated = (TheLoaiPhim) response.getBody();
            toBeUpdated.setMaTLPhim(theLoaiPhim.getMaTLPhim());
            toBeUpdated.setTenTheLoai(theLoaiPhim.getTenTheLoai());
            toBeUpdated.setTrangThai(theLoaiPhim.getTrangThai());
            return ResponseEntity.status(HttpStatus.OK).body(theLoaiPhimRepository.save(toBeUpdated));
        }
        return response;
    }

    @Operation(summary = "Find TheLoaiPhim by ID", description = "Finds a TheLoaiPhim record by its ID.")
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, theLoaiPhimRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        }
        return response;
    }

    @Operation(summary = "Find TheLoaiPhim by column and value", description = "Supports searching by column name and value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity found"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity findBy(@PathVariable String columnName, @PathVariable String value) {
        return RepoUtility.findByCustomColumn(theLoaiPhimRepository, columnName, value);
    }
}
