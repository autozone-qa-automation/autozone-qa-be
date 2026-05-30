/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.az_qa.backend.entity.TestCasesEntity;

@Repository
public interface TestCasesRepository extends JpaRepository<TestCasesEntity, Long> {

  List<TestCasesEntity> findByIsActive(boolean isActive);

  Optional<TestCasesEntity> findByIdAndIsActive(long id, boolean isActive);

  Optional<TestCasesEntity> findByTitleAndIsActive(String title, boolean isActive);

  List<TestCasesEntity> findByFeature_IdAndIsActive(Long featureId, boolean isActive);

  /**
   * Finds all test cases associated with a given release.
   * Used to desassociate test cases when a release is soft-deleted.
   *
   * @param releaseId the release identifier
   * @return list of test case entities linked to the release
   */
  List<TestCasesEntity> findByRelease_ReleaseId(Long releaseId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE TestCasesEntity t SET t.isActive = false WHERE t.id = :id AND t.isActive = true")
  int deactivateById(@Param("id") Long id);
}
