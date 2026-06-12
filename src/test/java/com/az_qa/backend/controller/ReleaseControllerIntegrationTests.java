/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.repository.ReleaseRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReleaseControllerIntegrationTests {

  @Autowired private WebApplicationContext context;

  @Autowired private ReleaseRepository releaseRepository;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    releaseRepository.deleteAll();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName(
      "GET /api/v1/releases/last - Integración completa (Controller -> Service -> DAO -> DB)")
  public void getLastReleases_IntegrationSuccess() throws Exception {
    ReleaseEntity release = new ReleaseEntity();
    release.setReleaseName("Integration Test Release");
    release.setReleaseDescription("Descripción de integración");
    release.setReleaseCreationDate(LocalDate.of(2026, 5, 25));
    release.setReleaseVersion("v2.0.0");
    release.setReleaseStatus(ReleaseStatus.Active);
    releaseRepository.save(release);

    mockMvc
        .perform(get("/api/v1/releases/last"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].releaseName").value("Integration Test Release"))
        .andExpect(jsonPath("$[0].releaseVersion").value("v2.0.0"))
        .andExpect(jsonPath("$[0].releaseStatus").value("Active"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("GET /api/v1/releases/last - Debe retornar lista vacía cuando no hay releases")
  public void getLastReleases_Empty() throws Exception {
    mockMvc
        .perform(get("/api/v1/releases/last"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }
}
