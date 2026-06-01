/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.repository;

import com.az_qa.backend.entity.ServicesEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<ServicesEntity, Long> {

  @Query("SELECT DISTINCT s FROM ServicesEntity s LEFT JOIN FETCH s.urls WHERE s.isActive = true")
  List<ServicesEntity> findAllWithUrls();

  @Query(
      "SELECT s FROM ServicesEntity s LEFT JOIN FETCH s.urls WHERE s.id = :id AND s.isActive ="
          + " true")
  Optional<ServicesEntity> findByIdWithUrls(@Param("id") Long id);

  Optional<ServicesEntity> findByIdAndIsActive(Long id, Boolean isActive);

  @Query(
      "SELECT COUNT(s) > 0 FROM ServicesEntity s WHERE LOWER(s.name) = LOWER(:name) AND s.id <>"
          + " :id")
  boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("id") Long id);
}
