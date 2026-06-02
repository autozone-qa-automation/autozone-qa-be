/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import com.az_qa.backend.service.RolesService;
import com.az_qa.backend.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Endpoints for managing roles")
public class RolesController {

  @Autowired RolesService rolesService;

  /**
   * Retrieves a role by id.
   *
   * @param id role identifier
   * @return role representation
   */
  @GetMapping("/{id}")
  @Operation(summary = "Get role by id", description = "Retrieves a role by its identifier")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Role found",
            content = @Content(schema = @Schema(implementation = RoleVO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value =
                                "{\"timestamp\":\"2026-04-19T10:00:00\",\"message\":\"Role with id"
                                    + " 99 not found\"}")))
      })
  ResponseEntity<RoleVO> getById(@PathVariable Long id) {
    RoleVO role = rolesService.findById(id);
    return ResponseEntity.ok(role);
  }

  /**
   * Retrieves all roles.
   *
   * @return list of roles
   */
  @GetMapping
  @Operation(summary = "Get all roles", description = "Retrieves all roles available in the system")
  @ApiResponse(
      responseCode = "200",
      description = "Roles retrieved",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleVO.class))))
  ResponseEntity<List<RoleVO>> getALL() {
    List<RoleVO> roles = rolesService.findAll();
    return ResponseEntity.ok(roles);
  }
}
