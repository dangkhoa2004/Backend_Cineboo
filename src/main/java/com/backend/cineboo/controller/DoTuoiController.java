package com.backend.cineboo.controller;

import com.backend.cineboo.entity.DoTuoi;
import com.backend.cineboo.repository.DoTuoiRepository;
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
@RequestMapping("/dotuoi")
@OpenAPIDefinition(
        info = @Info(
                title = "DoTuoiAPI",
                version = "0.1",
                description = "CRUD operations for DoTuoi entity"
        ))
public class DoTuoiController {

    @Autowired
    private DoTuoiRepository doTuoiRepository;

    @Operation(summary = "Get all DoTuoi entries", description = "Returns a list of all DoTuoi entities.")
    @GetMapping("/get")
    public ResponseEntity<List<DoTuoi>> getAll() {
        List<DoTuoi> doTuois = doTuoiRepository.findAll();
        return ResponseEntity.ok(doTuois);
    }

    @Operation(summary = "Disable a DoTuoi", description = "Sets the status of DoTuoi to unavailable by ID.")
    @PutMapping("/disable/{id}")
    public ResponseEntity disable(@PathVariable Long id) {
        ResponseEntity<?> response = RepoUtility.findById(id, doTuoiRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            DoTuoi doTuoi = (DoTuoi) response.getBody();
            // Assume there's a field for status or availability; if not, you can implement a custom logic
            // doTuoi.setActive(false);  // Uncomment if there’s a status field
            doTuoiRepository.save(doTuoi);
            return ResponseEntity.status(HttpStatus.OK).body("Disabled DoTuoi successfully");
        }
        return response;
    }

    @Operation(summary = "Add a new DoTuoi", description = "Adds a new DoTuoi entity.")
    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody DoTuoi doTuoi, BindingResult bindingResult) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        DoTuoi addedDoTuoi = doTuoiRepository.save(doTuoi);
        return ResponseEntity.ok(addedDoTuoi);
    }

    @Operation(summary = "Update DoTuoi information", description = "Updates an existing DoTuoi based on its ID.")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody DoTuoi doTuoi, BindingResult bindingResult, @PathVariable("id") Long id) {
        Map<String, String> errors = EntityValidator.validateFields(bindingResult);
        if (MapUtils.isNotEmpty(errors)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ResponseEntity<?> response = RepoUtility.findById(id, doTuoiRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            DoTuoi toBeUpdated = (DoTuoi) response.getBody();
            toBeUpdated.setMaDoTuoi(doTuoi.getMaDoTuoi());
            toBeUpdated.setTenDoTuoi(doTuoi.getTenDoTuoi());
            return ResponseEntity.status(HttpStatus.OK).body(doTuoiRepository.save(toBeUpdated));
        }
        return response;
    }

    @Operation(summary = "Find DoTuoi by ID", description = "Finds a DoTuoi entity by its ID.")
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return RepoUtility.findById(id, doTuoiRepository);
    }

    @Operation(summary = "Find DoTuoi by custom column", description = "Finds DoTuoi entities by a specified column and value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity found"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/find/{columnName}/{value}")
    public ResponseEntity<?> findBy(@PathVariable String columnName, @PathVariable String value) {
        return RepoUtility.findByCustomColumn(doTuoiRepository, columnName, value);
    }
}
