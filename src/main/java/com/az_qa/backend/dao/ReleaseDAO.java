/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.dao;

import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.mapper.ReleaseMapper;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.vo.ReleaseVO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object (DAO) for Release persistence operations.
 * Handles all database interactions for Release entities, including retrieval,
 * creation, and deletion.
 */
@Repository
public class ReleaseDAO {
  /**
   * Repository dependency used for movie persistence operations.
   */
  @Autowired private ReleaseRepository releaseRepository;

  /**
   * Retrieves a release by its ID.
   *
   * @param id the ID of the release to retrieve
   * @return the ReleaseVO with the specified ID
   * @throws ItemNotFoundException if no release exists with the given ID
   */
  public ReleaseVO findById(Long id) {
    Optional<ReleaseVO> releaseVO = releaseRepository.findById(id).map(ReleaseMapper::toVO);

    if (releaseVO.isEmpty()) {
      throw new ItemNotFoundException("Release with id " + id + " not found");
    }

    return releaseVO.get();
  }

  /**
   * Retrieves all releases from the database.
   *
   * @return a list of all ReleaseVO objects
   */
  public List<ReleaseVO> findAll() {
    return releaseRepository.findAll().stream().map(ReleaseMapper::toVO).toList();
  }

  /**
   * Saves a new release or updates an existing one.
   *
   * @param releaseVO the ReleaseVO object to save
   * @return the saved ReleaseVO with its ID set
   */
  public ReleaseVO save(ReleaseVO releaseVO) {
    ReleaseEntity releaseEntity = ReleaseMapper.toEntity(releaseVO);
    if (releaseEntity == null) {
      return null;
    }
    if (releaseEntity.getReleaseId() == null) {
      releaseEntity.setNew(true);
    }
    return ReleaseMapper.toVO(releaseRepository.save(releaseEntity));
  }

  /**
   * Deletes a release by its ID.
   *
   * @param id the ID of the release to delete
   */
  public void deleteById(Long id) {
    releaseRepository.deleteById(id);
  }
}
