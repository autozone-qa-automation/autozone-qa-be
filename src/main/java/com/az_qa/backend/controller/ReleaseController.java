package com.az_qa.backend.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.az_qa.backend.service.ReleaseService;
import com.az_qa.backend.vo.ReleaseVO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RequestMapping("/api/v1/releases")
public class ReleaseController {
    @Autowired
    ReleaseService releaseService;

    @GetMapping("/{id}")
    ResponseEntity<ReleaseVO> getReleaseById(@PathVariable Long id) {
        ReleaseVO release = releaseService.findById(id);
        return ResponseEntity.ok(release);
    }

    @GetMapping
    ResponseEntity<List<ReleaseVO>> getAll() {
        return ResponseEntity.ok(releaseService.findAll());
    }

    @PostMapping
    ResponseEntity<ReleaseVO> addNew(@Valid @RequestBody ReleaseVO releaseVO) {
        ReleaseVO savedRelease = releaseService.add(releaseVO);
        if (savedRelease == null) {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }
}
