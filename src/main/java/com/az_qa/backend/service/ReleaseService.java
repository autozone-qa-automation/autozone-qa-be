/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.az_qa.backend.enumeration.ReleaseStatus;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.vo.ReleaseVO;

@Service
public class ReleaseService {

  // Mock data storage
  private final List<ReleaseVO> mockReleases = new ArrayList<>();
  private Long nextId = 1L;

  // Initialize with mock data
  {
    mockReleases.add(new ReleaseVO(
        1L,
        "Release 1.0.0",
        "Initial release with core features",
        LocalDate.of(2024, 1, 15),
        LocalDate.of(2024, 2, 1),
        "1.0.0",
        "production,stable",
        ReleaseStatus.Active,
        "UserService"
    ));

    mockReleases.add(new ReleaseVO(
        2L,
        "Release 1.1.0",
        "Feature enhancement release",
        LocalDate.of(2024, 3, 1),
        LocalDate.of(2024, 3, 15),
        "1.1.0",
        "enhancement,minor",
        ReleaseStatus.Active,
        "FeatureService"
    ));

    mockReleases.add(new ReleaseVO(
        3L,
        "Release 2.0.0",
        "Major release with breaking changes",
        LocalDate.of(2024, 6, 1),
        null,
        "2.0.0",
        "major,breaking",
        ReleaseStatus.Draft,
        "TestService"
    ));

    nextId = 4L;
  }

  /**
   * Retrieves a release by its ID.
   *
   * @param id the release's ID
   * @return the matching release
   */
  public ReleaseVO getReleaseById(Long id) {
    return mockReleases.stream()
        .filter(release -> release.getReleaseId().equals(id))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Release with id {" + id + "} not found."));
  }

  /**
   * Retrieves all registered releases.
   *
   * @return a list of all releases
   */
  public List<ReleaseVO> getAllReleases() {
    return new ArrayList<>(mockReleases);
  }

  /**
   * Creates a new release.
   *
   * @param releaseVO the release payload
   * @return the created release
   */
  @Transactional
  public ReleaseVO createRelease(ReleaseVO releaseVO) {
    ReleaseVO newRelease = new ReleaseVO(
        nextId++,
        releaseVO.getReleaseName(),
        releaseVO.getReleaseDescription(),
        releaseVO.getReleaseCreationDate(),
        releaseVO.getReleaseLaunchDate(),
        releaseVO.getReleaseVersion(),
        releaseVO.getReleaseTags(),
        releaseVO.getReleaseStatus(),
        releaseVO.getReleaseService()
    );

    mockReleases.add(newRelease);
    return newRelease;
  }
}