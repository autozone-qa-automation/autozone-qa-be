/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.controller;

import com.az_qa.backend.service.FeaturesService;
import com.az_qa.backend.vo.FeatureVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  /**
   * Endpoint used to get a feature by id.
   * @param id Feature identifier.
   * @return Feature.
   */
  @GetMapping("/{id}")
  public ResponseEntity<FeatureVO> getFeatureById(@PathVariable @Positive long id) {
    return ResponseEntity.ok(featuresService.getFeatureById(id));
  }

  /**
   * Endpoint used to get a feature linked to a service by id.
   * @param id Service identifier.
   * @return Feature.
   */
  @GetMapping("/service/{id}")
  public ResponseEntity<FeatureVO> getFeatureByServiceId(@PathVariable @Positive long id) {
    return ResponseEntity.ok(featuresService.getFeatureByServiceId(id));
  }

  /**
   * Endpoint used to get all features found in db.
   * @return List of features.
   */
  @GetMapping
  public ResponseEntity<List<FeatureVO>> getAll() {
    return ResponseEntity.ok(featuresService.getAllFeatures());
  }

  @PostMapping
  public ResponseEntity<FeatureVO> create(@Valid @RequestBody FeatureVO featureVO) {
    FeatureVO createdFeature = featuresService.createFeature(featureVO);
    if (createdFeature == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createdFeature);
  }
}
