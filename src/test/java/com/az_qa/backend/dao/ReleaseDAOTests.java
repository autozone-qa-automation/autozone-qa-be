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

  private ReleaseEntity releaseEntity;

  @BeforeEach
  void setUp() {
    releaseEntity = new ReleaseEntity();
    releaseEntity.setReleaseId(1L);
    releaseEntity.setReleaseName("DAO Test Release");
    releaseEntity.setReleaseDescription("DAO test description");
    releaseEntity.setReleaseCreationDate(LocalDate.of(2026, 5, 25));
    releaseEntity.setReleaseVersion("v1.0.0");
    releaseEntity.setReleaseStatus(ReleaseStatus.Active);
  }

  @Test
  @DisplayName("GET /last: Debe retornar los últimos 5 releases mapeados correctamente")
  void findLast_Success() {
    when(releaseRepository.findTop5ByOrderByReleaseCreationDateDesc())
        .thenReturn(List.of(releaseEntity));

    List<ReleaseVO> result = releaseDAO.findLast(null);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("DAO Test Release", result.get(0).getReleaseName());
    assertEquals("v1.0.0", result.get(0).getReleaseVersion());
    assertEquals(ReleaseStatus.Active, result.get(0).getReleaseStatus());
  }
}
