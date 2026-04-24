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

  List<TestCasesEntity> findByActive(boolean active);

  Optional<TestCasesEntity> findByIdAndActive(long id, boolean active);

  List<TestCasesEntity> findByFeature_IdAndActiveTrue(Long featureId);
}
