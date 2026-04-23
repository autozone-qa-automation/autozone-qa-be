/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.service.ReleaseService;
import com.az_qa.backend.vo.ReleaseVO;

@RestController
@RequestMapping("/api/v1/releases")
/**
 * REST controller for managing releases.
 * Provides endpoints for retrieving, creating, and managing release
 * information.
 */
public class ReleaseController {

  private final ReleaseService releaseService;

  public ReleaseController(ReleaseService releaseService) {
    this.releaseService = releaseService;
  }

  /**
   * Retrieves all releases.
   *
   * @return a ResponseEntity containing a list of all ReleaseVO objects and HTTP
   *         status OK
   */
  @GetMapping
  public ResponseEntity<List<ReleaseVO>> getAllReleases() {
    List<ReleaseVO> releases = releaseService.getAllReleases();
    return new ResponseEntity<>(releases, HttpStatus.OK);
  }

  /**
   * Retrieves a release by its ID.
   *
   * @param id the ID of the release to retrieve
   * @return a ResponseEntity containing the ReleaseVO if found (HTTP 200) or
   *         NOT_FOUND if not found
   */
  @GetMapping("/{id}")
  public ResponseEntity<ReleaseVO> getReleaseById(@PathVariable Long id) {
    try {
      ReleaseVO release = releaseService.getReleaseById(id);
      return new ResponseEntity<>(release, HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Creates a new release.
   *
   * @param releaseVO the ReleaseVO object containing the release data to create
   * @return a ResponseEntity containing the created ReleaseVO and HTTP status
   *         CREATED
   */
  @PostMapping
  public ResponseEntity<ReleaseVO> createRelease(@RequestBody ReleaseVO releaseVO) {
    ReleaseVO createdRelease = releaseService.createRelease(releaseVO);
    return new ResponseEntity<>(createdRelease, HttpStatus.CREATED);
  }
}