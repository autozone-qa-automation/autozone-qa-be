/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.controller;

import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.service.ReleaseService;
import com.az_qa.backend.vo.ReleaseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/releases")
@Tag(name = "Releases", description = "Endpoints for managing releases")
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
  @Operation(
      summary = "Get all releases",
      description = "Retrieves all releases, optionally filtered by tags and status")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Releases retrieved",
            content =
                @Content(array = @ArraySchema(schema = @Schema(implementation = ReleaseVO.class))))
      })
  public ResponseEntity<List<ReleaseVO>> getAllReleases(
      @RequestParam(required = false) String releaseTags,
      @RequestParam(required = false) String releaseStatus) {
    List<ReleaseVO> releases = releaseService.getReleasesFiltered(releaseStatus, releaseTags);
    return new ResponseEntity<>(releases, HttpStatus.OK);
  }

  /**
   * Retrieves the last 5 releases ordered by creation date descending.
   *
   * @return a ResponseEntity containing a list of up to 5 ReleaseVO objects
   */
  @GetMapping("/last")
  @Operation(
      summary = "Get last 5 releases",
      description = "Retrieves the 5 most recent releases ordered by creation date descending")
  @ApiResponse(
      responseCode = "200",
      description = "Last 5 releases retrieved",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReleaseVO.class))))
  public ResponseEntity<List<ReleaseVO>> getLastReleases() {
    List<ReleaseVO> releases = releaseService.getLastReleases();
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
  @Operation(summary = "Get release by id", description = "Retrieves a release by its identifier")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Release found",
            content = @Content(schema = @Schema(implementation = ReleaseVO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Release not found",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value =
                                "{\"timestamp\":\"2026-05-25T10:00:00\",\"message\":\"Release with"
                                    + " id 99 not found\"}")))
      })
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
  @Operation(summary = "Create release", description = "Creates a new release")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Release created",
            content = @Content(schema = @Schema(implementation = ReleaseVO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid payload")
      })
  public ResponseEntity<ReleaseVO> createRelease(@Valid @RequestBody ReleaseVO releaseVO) {
    ReleaseVO createdRelease = releaseService.createRelease(releaseVO);
    return new ResponseEntity<>(createdRelease, HttpStatus.CREATED);
  }

  /**
   * Updates the status of a specific release.
   *
   * @param id     the ID of the release
   * @param status the new status (Draft, Progress, Active)
   * @return a ResponseEntity with the updated ReleaseVO or error details
   */
  @PutMapping("/{id}/status/{status}")
  @Operation(
      summary = "Update release status",
      description = "Updates the status of a release following state machine rules")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Release status updated",
            content = @Content(schema = @Schema(implementation = ReleaseVO.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid status transition",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(value = "Invalid status transition from Active to Draft"))),
        @ApiResponse(
            responseCode = "404",
            description = "Release not found",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value =
                                "{\"timestamp\":\"2026-05-25T10:00:00\",\"message\":\"Release with"
                                    + " id 99 not found\"}")))
      })
  public ResponseEntity<?> updateReleaseStatus(
      @PathVariable Long id, @PathVariable ReleaseStatus status) {
    try {
      ReleaseVO updatedRelease = releaseService.updateReleaseStatus(id, status);
      return new ResponseEntity<>(updatedRelease, HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
