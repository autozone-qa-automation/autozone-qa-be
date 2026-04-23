/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.az_qa.backend.dao.ReleaseDAO;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.vo.ReleaseVO;

@Service
public class ReleaseService {
  private final ReleaseDAO releaseDAO;

  public ReleaseService(ReleaseDAO releaseDAO) {
    this.releaseDAO = releaseDAO;
  }

  /**
   * Retrieves a release by its ID.
   *
   * @param id the release's ID
   * @return the matching release
   */
  public ReleaseVO getReleaseById(Long id) {
    try {
      return releaseDAO.findById(id);
    } catch (ItemNotFoundException e) {
      throw new ResourceNotFoundException("Release with id {" + id + "} not found.");
    }
  }

  /**
   * Retrieves releases based on filter criteria.
   *
   * @param releaseStatus the status to filter by
   * @param releaseTags the tags to filter by
   * @return a list of filtered releases
   */
  public List<ReleaseVO> getReleasesFiltered(String releaseStatus, String releaseTags) {
    return releaseDAO.findFiltered(releaseStatus, releaseTags);
  }

  /**
   * Retrieves all registered releases.
   *
   * @return a list of all releases
   */
  public List<ReleaseVO> getAllReleases() {
    return releaseDAO.findAll();
  }

  /**
   * Creates a new release.
   *
   * @param releaseVO the release payload
   * @return the created release
   */
  @Transactional
  public ReleaseVO createRelease(ReleaseVO releaseVO) {
    releaseVO.setReleaseId(null);
    return releaseDAO.save(releaseVO);
  }
}
