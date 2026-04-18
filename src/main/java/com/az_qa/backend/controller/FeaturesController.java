/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.controller;

import com.az_qa.backend.service.FeaturesService;
import com.az_qa.backend.vo.FeatureVO;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<FeatureVO> getUserById(@PathVariable @Positive long id) {
    return ResponseEntity.ok(featuresService.getFeatureById(id));
  }

  @GetMapping
  public ResponseEntity<List<FeatureVO>> getAll() {
    return ResponseEntity.ok(featuresService.getAllFeatures());
  }

  /**
   * @PostMapping
   * public ResponseEntity<FeaturesResponse> createFeature(
   * @Valid @RequestBody FeaturesRequest request) {
   * FeaturesResponse createdFeature = featuresService.create(request);
   * return ResponseEntity.status(HttpStatus.CREATED).body(createdFeature);
   * }*/
}
