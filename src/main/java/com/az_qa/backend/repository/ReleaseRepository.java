/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.az_qa.backend.entity.ReleaseEntity;

@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {
    @Query("SELECT s.name FROM ReleaseEntity r " +
       "JOIN r.features rf " +
       "JOIN rf.feature f " +   
       "JOIN f.service s " +       
       "WHERE r.releaseId = :releaseId")
    List<String> findNombresServiciosByReleaseId(@Param("releaseId") Long releaseId);
}
