package com.backend.cineboo.controller;

import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.entity.PTTT;
import com.backend.cineboo.repository.PTTTRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pttt")
@OpenAPIDefinition(
        info = @Info(
                title = "PTTTAPI",
                version = "0.1",
                description = "CRUD operations for PTTT entity"
        ))
public class PTTTController {

    @Autowired
    private PTTTRepository ptttRepository;

    @Operation(summary = "Get all PTTT records",
            description = "Returns a list of all PTTT records in the system.")
    @GetMapping("/get")
    public ResponseEntity<List<PTTT>> get(){
        List<PTTT> pttts = new ArrayList<>(ptttRepository.findAll());
        return ResponseEntity.ok(pttts);
    }

    @Operation(summary = "Disable a PTTT record",
            description = "Sets the PTTT status to disabled (trangThaiPTTT = 0) by ID.")
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, ptttRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            PTTT pttt = (PTTT) response.getBody();
            pttt.setTrangThaiPTTT(0);
            ptttRepository.save(pttt);
            return ResponseEntity.status(HttpStatus.OK).body("PTTT disabled successfully");
        }
        return response;
    }

    @Operation(summary = "Add a new PTTT record",
            description = "Adds a new PTTT record to the system.")
    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody PTTT pttt, BindingResult bindingResult){
        Map<String,String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        PTTT addedPTTT = ptttRepository.save(pttt);
        return ResponseEntity.ok(addedPTTT);
    }

    @Operation(summary = "Update PTTT details",
            description = "Updates the PTTT record based on ID.")
    @PutMapping("/update/{id}")
    public ResponseEntity update(@Valid @RequestBody PTTT pttt, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity response = RepoUtility.findById(id, ptttRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            PTTT toBeUpdated = (PTTT) response.getBody();
            toBeUpdated.setMaPTTT(pttt.getMaPTTT());
            toBeUpdated.setTenPTTT(pttt.getTenPTTT());
            toBeUpdated.setTrangThaiPTTT(pttt.getTrangThaiPTTT());
            return ResponseEntity.status(HttpStatus.OK).body(ptttRepository.save(toBeUpdated));
        }
        return response;
    }

    @Operation(summary = "Find PTTT by ID",
            description = "Finds a PTTT record by its ID.")
    @GetMapping("/find/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        ResponseEntity response = RepoUtility.findById(id, ptttRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        }
        return response;
    }

    @Operation(summary = "Find PTTT by column and value",
            description = "Supports searching by column name and value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal error")})
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity findBy(@PathVariable String columnName, @PathVariable String value) {
        return RepoUtility.findByCustomColumn(ptttRepository, columnName, value);
    }

}
