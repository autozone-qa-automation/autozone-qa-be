/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/
package com.az_qa.backend.repository;

import com.az_qa.backend.entity.FeatureEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturesRepository extends JpaRepository<FeatureEntity, Long> {

  /**
   * Interface used to find the feature that contains the service id.
   * @param id service id.
   * @return FeatureEntity.
   */
  List<FeatureEntity> findByServiceId(Long id);

  Optional<FeatureEntity> findByName(String name);
}
