/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.repository;

import com.az_qa.backend.entity.ReleaseEntity;
import java.time.LocalDate;
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
  @Query(
      "SELECT s.name FROM ReleaseEntity r "
          + "JOIN r.features rf "
          + "JOIN rf.feature f "
          + "JOIN f.service s "
          + "WHERE r.releaseId = :releaseId")
  List<String> findNombresServiciosByReleaseId(@Param("releaseId") Long releaseId);

  List<ReleaseEntity> findTop5ByOrderByReleaseCreationDateDesc();
  /**
   * Finds releases matching all provided filter criteria. Any {@code null} parameter is ignored,
   * making every filter optional.
   *
   * @param serviceId  only releases that include a feature from this service; {@code null} skips
   * @param startDate  lower bound (inclusive) for {@code releaseLaunchDate}; {@code null} skips
   * @param endDate    upper bound (inclusive) for {@code releaseLaunchDate}; {@code null} skips
   * @param tagName    substring match (case-insensitive) against {@code releaseTags}; {@code null} skips
   * @return distinct list of matching release entities
   */
  @Query(
      "SELECT DISTINCT r FROM ReleaseEntity r "
          + "LEFT JOIN r.features rf "
          + "LEFT JOIN rf.feature f "
          + "LEFT JOIN f.service s "
          + "WHERE (:serviceId IS NULL OR s.id = :serviceId) "
          + "AND (:startDate IS NULL OR r.releaseLaunchDate >= :startDate) "
          + "AND (:endDate IS NULL OR r.releaseLaunchDate <= :endDate) "
          + "AND (:tagName IS NULL OR LOWER(r.releaseTags) LIKE LOWER(CONCAT('%', :tagName, '%')))")
  List<ReleaseEntity> findByFilters(
      @Param("serviceId") Long serviceId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("tagName") String tagName);
}
