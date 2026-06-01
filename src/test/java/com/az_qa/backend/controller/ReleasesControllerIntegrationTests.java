/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.repository.ReleasedFeaturesRepository;
import com.az_qa.backend.repository.ServicesRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReleasesControllerIntegrationTests {

  @Autowired private WebApplicationContext context;

  @Autowired private ReleaseRepository releaseRepository;

  @Autowired private ReleasedFeaturesRepository releasedFeaturesRepository;

  @Autowired private FeaturesRepository featuresRepository;

  @Autowired private ServicesRepository servicesRepository;

  private MockMvc mockMvc;

  private Long releaseId;
  private Long featureId;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    releasedFeaturesRepository.deleteAll();
    featuresRepository.deleteAll();
    servicesRepository.deleteAll();
    releaseRepository.deleteAll();

    ServicesEntity service = createService("Orders API", "Order management service.");
    FeatureEntity feature = createFeature("Order validation", "Validates order payloads.", service);
    featureId = feature.getId();
    releaseId = createReleaseOne().getReleaseId();
  }

  @Test
  @WithMockUser
  void getReleaseById_returnsExpectedRelease() throws Exception {
    mockMvc
        .perform(get("/api/v1/releases/{id}", releaseId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.releaseId").value(releaseId))
        .andExpect(jsonPath("$.releaseName").value("Checkout QA Release"))
        .andExpect(jsonPath("$.releaseDescription").value("Release for checkout QA automation."))
        .andExpect(jsonPath("$.releaseCreationDate").value("2026-05-01"))
        .andExpect(jsonPath("$.releaseLaunchDate").value("2026-05-15"))
        .andExpect(jsonPath("$.releaseVersion").value("1.0.0"))
        .andExpect(jsonPath("$.releaseTags[0]").value("checkout"))
        .andExpect(jsonPath("$.releaseTags[1]").value("qa"))
        .andExpect(jsonPath("$.releaseStatus").value("Active"))
        .andExpect(jsonPath("$.releaseServices").isArray())
        .andExpect(jsonPath("$.releaseFeatures").isArray());
  }

  @Test
  @WithMockUser
  void getReleaseById_notExistingRelease() throws Exception {
    mockMvc.perform(get("/api/v1/releases/999999")).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void getAllReleases_withStatusFilter_returnsMatchingReleases() throws Exception {
    createRelease(
        "Draft Search Release",
        "Release that should not match the Active status filter.",
        LocalDate.of(2026, 5, 2),
        LocalDate.of(2026, 5, 20),
        "1.1.0",
        "search,qa",
        ReleaseStatus.Draft);

    mockMvc
        .perform(get("/api/v1/releases").param("releaseStatus", "Active"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].releaseId").value(releaseId))
        .andExpect(jsonPath("$[0].releaseStatus").value("Active"));
  }

  @Test
  @WithMockUser
  void getAllReleases_withTagsFilter_returnsMatchingReleases() throws Exception {
    createRelease(
        "Search QA Release",
        "Release that should match the search tag.",
        LocalDate.of(2026, 5, 3),
        LocalDate.of(2026, 5, 21),
        "1.2.0",
        "search,qa",
        ReleaseStatus.Progress);

    mockMvc
        .perform(get("/api/v1/releases").param("releaseTags", "search"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].releaseName").value("Search QA Release"))
        .andExpect(jsonPath("$[0].releaseTags[0]").value("search"))
        .andExpect(jsonPath("$[0].releaseTags[1]").value("qa"));
  }

  @Test
  @WithMockUser
  void addRelease_withoutFeatureIds_returnsCreatedAndPersistsRelease() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/releases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                          "releaseName": "Inventory QA Release",
                          "releaseDescription": "Release for inventory QA automation.",
                          "releaseCreationDate": "2026-05-04",
                          "releaseLaunchDate": "2026-05-30",
                          "releaseVersion": "2.0.0",
                          "releaseTags": ["inventory", "qa"],
                          "releaseStatus": "Draft",
                          "releaseServiceId": 1
                        }
                        """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.releaseId").isNumber())
        .andExpect(jsonPath("$.releaseName").value("Inventory QA Release"))
        .andExpect(jsonPath("$.releaseCreationDate").value("2026-05-04"))
        .andExpect(jsonPath("$.releaseVersion").value("2.0.0"))
        .andExpect(jsonPath("$.releaseStatus").value("Draft"));
  }

  @Test
  @WithMockUser
  void addRelease_withFeatureIds_returnsCreatedAndPersistsAssociation() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/releases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    String.format(
                        """
                            {
                              "releaseName": "Orders QA Release",
                              "releaseDescription": "Release with a linked feature.",
                              "releaseCreationDate": "2026-05-05",
                              "releaseLaunchDate": "2026-06-01",
                              "releaseVersion": "3.0.0",
                              "releaseTags": ["orders", "qa"],
                              "releaseStatus": "Progress",
                              "releaseServiceId": 1,
                              "releaseFeatureIds": [%d]
                            }
                            """,
                        featureId)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.releaseId").isNumber())
        .andExpect(jsonPath("$.releaseName").value("Orders QA Release"))
        .andExpect(jsonPath("$.releaseStatus").value("Progress"))
        .andExpect(jsonPath("$.releaseServices[0]").value("Orders API"));

    org.junit.jupiter.api.Assertions.assertEquals(1L, releasedFeaturesRepository.count());
  }

  private ReleaseEntity createReleaseOne() {
    return createRelease(
        "Checkout QA Release",
        "Release for checkout QA automation.",
        LocalDate.of(2026, 5, 1),
        LocalDate.of(2026, 5, 15),
        "1.0.0",
        "checkout,qa",
        ReleaseStatus.Active);
  }

  private ReleaseEntity createRelease(
      String releaseName,
      String releaseDescription,
      LocalDate releaseCreationDate,
      LocalDate releaseLaunchDate,
      String releaseVersion,
      String releaseTags,
      ReleaseStatus releaseStatus) {
    ReleaseEntity release =
        new ReleaseEntity(
            releaseName,
            releaseDescription,
            releaseCreationDate,
            releaseLaunchDate,
            releaseVersion,
            releaseTags,
            releaseStatus,
            true);
    release.setNew(true);
    return releaseRepository.save(release);
  }

  private ServicesEntity createService(String name, String description) {
    ServicesEntity service = new ServicesEntity(null, name, description);
    service.setNew(true);
    return servicesRepository.save(service);
  }

  private FeatureEntity createFeature(String name, String description, ServicesEntity service) {
    FeatureEntity feature = new FeatureEntity(name, description);
    feature.setService(service);
    return featuresRepository.save(feature);
  }
}
