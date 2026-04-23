/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.az_qa.backend.entity.ReleaseEntity;

@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {


}
