package com.az_qa.backend.controller;

import com.az_qa.backend.dto.request.FeaturesRequest;
import com.az_qa.backend.dto.response.FeaturesResponse;
import com.az_qa.backend.service.FeaturesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for features management endpoints.
 * Base path: /api/v1/features
 */
@RestController
@RequestMapping("/api/v1/features")
@Validated
public class FeaturesController {
  private final FeaturesService featuresService;

  public FeaturesController(FeaturesService featuresService) {
    this.featuresService = featuresService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<FeaturesResponse> getUserById(@PathVariable @Positive long id) {
    return ResponseEntity.ok(featuresService.getFeatureById(id));
  }

  @GetMapping
  public ResponseEntity<List<FeaturesResponse>> getAll() {
    return ResponseEntity.ok(featuresService.getAllFeatures());
  }

  @PostMapping
  public ResponseEntity<FeaturesResponse> createUser(@Valid @RequestBody FeaturesRequest request) {
    FeaturesResponse createdUser = featuresService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }
}
