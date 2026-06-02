/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.vo.ReleaseVO;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReleasesDAOTests {

  @Mock private ReleaseRepository releaseRepository;

  @InjectMocks private ReleaseDAO releaseDAO;

  @Test
  void save_withoutId_marksEntityAsNewAndReturnsSavedRelease() {
    when(releaseRepository.save(any(ReleaseEntity.class)))
        .thenAnswer(
            invocation -> {
              ReleaseEntity release = invocation.getArgument(0);
              release.setReleaseId(3L);
              return release;
            });

    ReleaseVO savedRelease = releaseDAO.save(createReleaseVO(null));

    ArgumentCaptor<ReleaseEntity> captor = ArgumentCaptor.forClass(ReleaseEntity.class);
    verify(releaseRepository).save(captor.capture());

    ReleaseEntity entityToSave = captor.getValue();
    assertTrue(entityToSave.isNew());
    assertEquals("Inventory QA Release", entityToSave.getReleaseName());
    assertEquals(LocalDate.of(2026, 5, 4), entityToSave.getReleaseCreationDate());
    assertEquals(ReleaseStatus.Draft, entityToSave.getReleaseStatus());
    assertEquals(3L, savedRelease.getReleaseId());
    assertEquals("Inventory QA Release", savedRelease.getReleaseName());
  }

  @Test
  void save_withId_preservesIdAndDoesNotMarkEntityAsNew() {
    when(releaseRepository.save(any(ReleaseEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ReleaseVO savedRelease = releaseDAO.save(createReleaseVO(9L));

    ArgumentCaptor<ReleaseEntity> captor = ArgumentCaptor.forClass(ReleaseEntity.class);
    verify(releaseRepository).save(captor.capture());

    ReleaseEntity entityToSave = captor.getValue();
    assertFalse(entityToSave.isNew());
    assertEquals(9L, entityToSave.getReleaseId());
    assertEquals(9L, savedRelease.getReleaseId());
  }

  @Test
  void save_whenReleaseIsNull_returnsNullAndDoesNotUseRepository() {
    ReleaseVO savedRelease = releaseDAO.save(null);

    assertNull(savedRelease);
    verifyNoInteractions(releaseRepository);
  }

  private ReleaseVO createReleaseVO(Long releaseId) {
    return new ReleaseVO(
        releaseId,
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
        List.of());
  }
}
