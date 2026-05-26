/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.controller;

import com.az_qa.backend.service.FeaturesService;
import com.az_qa.backend.vo.FeatureVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
   * 
   * @param id Feature identifier.
   * @return Feature.
   */
  @GetMapping("/{id}")
  public ResponseEntity<FeatureVO> getFeatureById(@PathVariable @Positive long id) {
    return ResponseEntity.ok(featuresService.getFeatureById(id));
  }

  /**
   * Endpoint used to get the features linked to a service by id.
   * 
   * @param id Service identifier.
   * @return Features.
   */
  @GetMapping("/filtered/{id}")
  public ResponseEntity<List<FeatureVO>> getFeaturesByServiceId(@PathVariable @Positive long id) {
    return ResponseEntity.ok(featuresService.getFeaturesByServiceId(id));
  }

  /**
   * Endpoint used to get all features found in db.
   * 
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

  /**
   * Endpoint used to update an existing feature.
   * 
   * @param id        Feature identifier.
   * @param featureVO Data to update.
   * @return Updated feature.
   */
  @PutMapping("/{id}/deactivate")
  @Operation(summary = "Deactivate a feature and its related test cases", description = "Sets the specified feature as inactive and also deactivates all test cases associated"
      + " with it. Returns 200 if the operation was successful, or 404 if the feature does"
      + " not exist.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Feature and related test cases successfully deactivated."),
      @ApiResponse(responseCode = "404", description = "Feature not found.")
  })
  public ResponseEntity<Void> deactivate(@PathVariable @Positive long id) {
    featuresService.deactivateFeature(id);
    return ResponseEntity.ok().build();
  }
}
