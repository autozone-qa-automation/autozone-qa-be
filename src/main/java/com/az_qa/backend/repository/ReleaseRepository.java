/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.repository;

import com.az_qa.backend.entity.ReleaseEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing and managing release records.
 * Provides the standard JPA operations for {@link ReleaseEntity} and custom
 * release queries.
 */
@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {

  /**
   * Finds the service names associated with the features included in a release.
   *
   * @param releaseId the release identifier
   * @return a list of service names linked to the release features
   */
  /**
   * Retrieves the most recent 5 releases ordered by creation date descending.
   *
   * @return a list of up to 5 release entities
   */
  List<ReleaseEntity> findTop5ByOrderByReleaseCreationDateDesc();

  @Query(
      "SELECT s.name FROM ReleaseEntity r "
          + "JOIN r.features rf "
          + "JOIN rf.feature f "
          + "JOIN f.service s "
          + "WHERE r.releaseId = :releaseId")
  List<String> findNombresServiciosByReleaseId(@Param("releaseId") Long releaseId);
}
