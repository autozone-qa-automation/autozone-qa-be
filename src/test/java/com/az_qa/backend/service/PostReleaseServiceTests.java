/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.az_qa.backend.dao.ReleaseDAO;
import com.az_qa.backend.entity.FeatureEntity;
import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.entity.ReleasedFeaturesEntity;
import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.repository.FeaturesRepository;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.repository.ReleasedFeaturesRepository;
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
class PostReleaseServiceTests {

    @Mock
    private ReleaseDAO releaseDAO;

    @Mock
    private ReleaseRepository releaseRepository;

    @Mock
    private FeaturesRepository featuresRepository;

    @Mock
    private ReleasedFeaturesRepository releasedFeaturesRepository;

    @InjectMocks
    private ReleaseService releaseService;

    @Test
    void createRelease_withoutFeatureIds_savesReleaseAndDoesNotCreateAssociations() {
        ReleaseVO request = createReleaseVO(null, null, List.of());
        ReleaseVO savedRelease = createReleaseVO(3L, LocalDate.of(2026, 5, 12), List.of());
        when(releaseDAO.save(any(ReleaseVO.class))).thenReturn(savedRelease);

        ReleaseVO result = releaseService.createRelease(request);

        ArgumentCaptor<ReleaseVO> captor = ArgumentCaptor.forClass(ReleaseVO.class);
        verify(releaseDAO).save(captor.capture());

        ReleaseVO releaseToSave = captor.getValue();
        assertNull(releaseToSave.getReleaseId());
        assertEquals(LocalDate.now(), releaseToSave.getReleaseCreationDate());
        assertEquals(savedRelease, result);
        verify(featuresRepository, never()).findAllById(any());
        verify(releasedFeaturesRepository, never()).saveAll(any());
    }

    @Test
    void createRelease_withRequestId_clearsIdBeforeSaving() {
        ReleaseVO request = createReleaseVO(99L, LocalDate.of(2026, 5, 4), null);
        ReleaseVO savedRelease = createReleaseVO(4L, LocalDate.of(2026, 5, 4), null);
        when(releaseDAO.save(any(ReleaseVO.class))).thenReturn(savedRelease);

        releaseService.createRelease(request);

        ArgumentCaptor<ReleaseVO> captor = ArgumentCaptor.forClass(ReleaseVO.class);
        verify(releaseDAO).save(captor.capture());
        assertNull(captor.getValue().getReleaseId());
    }

    @Test
    @SuppressWarnings("unchecked")
    void createRelease_withFeatureIds_savesAssociationsAndReturnsReleaseWithServices() {
        ReleaseVO request = createReleaseVO(null, LocalDate.of(2026, 5, 5), List.of(10L));
        ReleaseVO savedRelease = createReleaseVO(5L, LocalDate.of(2026, 5, 5), null);
        ReleaseVO foundRelease = createReleaseVO(5L, LocalDate.of(2026, 5, 5), List.of(10L));
        foundRelease.setReleaseServices(List.of("Orders API"));

        ServicesEntity service = new ServicesEntity(20L, "Orders API", "Order management service.");
        FeatureEntity feature = new FeatureEntity("Order validation", "Validates order payloads.");
        feature.setId(10L);
        feature.setService(service);

        ReleaseEntity releaseReference = new ReleaseEntity();
        releaseReference.setReleaseId(5L);

        when(releaseDAO.save(any(ReleaseVO.class))).thenReturn(savedRelease);
        when(featuresRepository.findAllById(List.of(10L))).thenReturn(List.of(feature));
        when(releaseRepository.getReferenceById(5L)).thenReturn(releaseReference);
        when(releasedFeaturesRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(releaseDAO.findById(5L)).thenReturn(foundRelease);
        when(releaseRepository.findNombresServiciosByReleaseId(5L)).thenReturn(List.of("Orders API"));

        ReleaseVO result = releaseService.createRelease(request);

        ArgumentCaptor<List<ReleasedFeaturesEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(releasedFeaturesRepository).saveAll(captor.capture());

        List<ReleasedFeaturesEntity> associations = captor.getValue();
        assertEquals(1, associations.size());
        assertEquals(releaseReference, associations.get(0).getRelease());
        assertEquals(feature, associations.get(0).getFeature());
        assertEquals(5L, result.getReleaseId());
        assertEquals(List.of("Orders API"), result.getReleaseServices());
    }

    @Test
    void createRelease_whenOneFeatureDoesNotExist_throwsResourceNotFound() {
        ReleaseVO request = createReleaseVO(null, LocalDate.of(2026, 5, 5), List.of(10L, 999L));
        ReleaseVO savedRelease = createReleaseVO(5L, LocalDate.of(2026, 5, 5), null);

        FeatureEntity feature = new FeatureEntity("Order validation", "Validates order payloads.");
        feature.setId(10L);

        when(releaseDAO.save(any(ReleaseVO.class))).thenReturn(savedRelease);
        when(featuresRepository.findAllById(List.of(10L, 999L))).thenReturn(List.of(feature));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> releaseService.createRelease(request));

        assertEquals("One or more release features were not found.", exception.getMessage());
        verify(releasedFeaturesRepository, never()).saveAll(any());
    }

    private ReleaseVO createReleaseVO(
            Long releaseId, LocalDate releaseCreationDate, List<Long> releaseFeatureIds) {
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
