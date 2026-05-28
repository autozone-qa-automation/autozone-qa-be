/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.repository;

import com.az_qa.backend.entity.TestCasesEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
