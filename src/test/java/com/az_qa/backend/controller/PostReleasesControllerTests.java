package com.az_qa.backend.controller;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.exception.GlobalExceptionHandler;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.service.ReleaseService;
import com.az_qa.backend.vo.FeatureVO;
import com.az_qa.backend.vo.ReleaseVO;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = { ReleaseController.class, GlobalExceptionHandler.class })
class PostReleasesControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ReleaseService releaseService;

        @Test
        void addNewRelease_withoutId_returnsCreated() throws Exception {
                when(releaseService.createRelease(
                                argThat(release -> release != null && release.getReleaseId() == null)))
                                .thenReturn(
                                                new ReleaseVO(
                                                                3L,
                                                                "Inventory QA Release",
                                                                "Release for inventory QA automation.",
                                                                LocalDate.of(2026, 5, 4),
                                                                LocalDate.of(2026, 5, 30),
                                                                "2.0.0",
                                                                List.of("inventory", "qa"),
                                                                ReleaseStatus.Draft,
                                                                List.of(),
                                                                20L,
                                                                List.of(),
                                                                List.of()));

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
                                                                                                  "releaseServiceId": 20
                                                                                                }
                                                                                                """))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.releaseId").value(3))
                                .andExpect(jsonPath("$.releaseName").value("Inventory QA Release"))
                                .andExpect(jsonPath("$.releaseStatus").value("Draft"))
                                .andExpect(jsonPath("$.releaseServiceId").value(20));
        }

        @Test
        void addNewRelease_withFeatureIds_returnsCreated() throws Exception {
                when(releaseService.createRelease(
                                argThat(
                                                release -> release != null
                                                                && release.getReleaseId() == null
                                                                && release.getReleaseFeatureIds() != null
                                                                && release.getReleaseFeatureIds()
                                                                                .equals(List.of(10L)))))
                                .thenReturn(
                                                new ReleaseVO(
                                                                4L,
                                                                "Orders QA Release",
                                                                "Release with linked features.",
                                                                LocalDate.of(2026, 5, 5),
                                                                LocalDate.of(2026, 6, 1),
                                                                "3.0.0",
                                                                List.of("orders", "qa"),
                                                                ReleaseStatus.Progress,
                                                                List.of("Orders API"),
                                                                20L,
                                                                List.of(10L),
                                                                List.of(new FeatureVO(10L, "Order validation",
                                                                                "Validates order payloads.", 20L))));

                mockMvc
                                .perform(
                                                post("/api/v1/releases")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(
                                                                                """
                                                                                                {
                                                                                                  "releaseName": "Orders QA Release",
                                                                                                  "releaseDescription": "Release with linked features.",
                                                                                                  "releaseCreationDate": "2026-05-05",
                                                                                                  "releaseLaunchDate": "2026-06-01",
                                                                                                  "releaseVersion": "3.0.0",
                                                                                                  "releaseTags": ["orders", "qa"],
                                                                                                  "releaseStatus": "Progress",
                                                                                                  "releaseServiceId": 20,
                                                                                                  "releaseFeatureIds": [10]
                                                                                                }
                                                                                                """))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.releaseId").value(4))
                                .andExpect(jsonPath("$.releaseName").value("Orders QA Release"))
                                .andExpect(jsonPath("$.releaseStatus").value("Progress"))
                                .andExpect(jsonPath("$.releaseServices[0]").value("Orders API"))
                                .andExpect(jsonPath("$.releaseFeatures[0].id").value(10));
        }

        @Test
        void addNewRelease_whenFeatureDoesNotExist_returnsNotFound() throws Exception {
                when(releaseService.createRelease(
                                argThat(
                                                release -> release != null
                                                                && release.getReleaseFeatureIds() != null
                                                                && release.getReleaseFeatureIds()
                                                                                .equals(List.of(999L)))))
                                .thenThrow(new ResourceNotFoundException(
                                                "One or more release features were not found."));

                mockMvc
                                .perform(
                                                post("/api/v1/releases")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(
                                                                                """
                                                                                                {
                                                                                                  "releaseName": "Orders QA Release",
                                                                                                  "releaseDescription": "Release with missing features.",
                                                                                                  "releaseCreationDate": "2026-05-05",
                                                                                                  "releaseLaunchDate": "2026-06-01",
                                                                                                  "releaseVersion": "3.0.0",
                                                                                                  "releaseTags": ["orders", "qa"],
                                                                                                  "releaseStatus": "Progress",
                                                                                                  "releaseServiceId": 20,
                                                                                                  "releaseFeatureIds": [999]
                                                                                                }
                                                                                                """))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("One or more release features were not found."));
        }

        @Test
        void addNewRelease_withMalformedBody_returnsBadRequest() throws Exception {
                mockMvc
                                .perform(
                                                post("/api/v1/releases")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(
                                                                                """
                                                                                                {
                                                                                                  "releaseName": "Orders QA Release",
                                                                                                  "releaseStatus": 42
                                                                                                }
                                                                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Invalid or malformed request body"));
        }
}
