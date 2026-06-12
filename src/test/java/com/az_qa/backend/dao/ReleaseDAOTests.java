/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.vo.ReleaseVO;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReleaseDAOTests {

  @Mock private ReleaseRepository releaseRepository;

  @InjectMocks private ReleaseDAO releaseDAO;

  private ReleaseEntity activeRelease;
  private ReleaseEntity inactiveRelease;

  @BeforeEach
  void setUp() {
    activeRelease = new ReleaseEntity();
    activeRelease.setReleaseId(1L);
    activeRelease.setReleaseName("DAO Test Active Release");
    activeRelease.setReleaseDescription("DAO test description");
    activeRelease.setReleaseCreationDate(LocalDate.of(2026, 5, 25));
    activeRelease.setReleaseVersion("v1.0.0");
    activeRelease.setReleaseStatus(ReleaseStatus.Active);
    activeRelease.setReleaseIsActive(true);

    inactiveRelease = new ReleaseEntity();
    inactiveRelease.setReleaseId(2L);
    inactiveRelease.setReleaseName("DAO Test Inactive Release");
    inactiveRelease.setReleaseDescription("DAO test description 2");
    inactiveRelease.setReleaseCreationDate(LocalDate.of(2026, 5, 26));
    inactiveRelease.setReleaseVersion("v1.0.0");
    inactiveRelease.setReleaseStatus(ReleaseStatus.Active);
    inactiveRelease.setReleaseIsActive(false);
  }

  @Test
  @DisplayName("GET /last: Debe retornar los últimos 5 releases mapeados correctamente")
  void findLast_Success() {
    when(releaseRepository.findTop5ByOrderByReleaseCreationDateDesc())
        .thenReturn(List.of(activeRelease));

    List<ReleaseVO> result = releaseDAO.findLast(null);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("DAO Test Active Release", result.get(0).getReleaseName());
    assertEquals("v1.0.0", result.get(0).getReleaseVersion());
    assertEquals(ReleaseStatus.Active, result.get(0).getReleaseStatus());
  }

  @Test
  @DisplayName("findFiltered: Debe retornar sólo los releases activos (isActive=true)")
  void findFiltered_OnlyActiveReleases() {
    when(releaseRepository.findAll()).thenReturn(List.of(activeRelease, inactiveRelease));

    List<ReleaseVO> result = releaseDAO.findFiltered(null, null);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getReleaseId());
    assertEquals("DAO Test Active Release", result.get(0).getReleaseName());
  }
}
