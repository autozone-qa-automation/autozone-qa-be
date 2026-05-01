/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.repository;

import com.az_qa.backend.entity.ReleasedFeaturesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing released-feature relationship records.
 * Provides standard JPA operations for {@link ReleasedFeaturesEntity}.
 */
@Repository
public interface ReleasedFeaturesRepository extends JpaRepository<ReleasedFeaturesEntity, Long> {}
