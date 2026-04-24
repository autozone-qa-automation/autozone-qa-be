package com.az_qa.backend.repository;

import com.az_qa.backend.entity.ReleasedFeaturesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleasedFeaturesRepository extends JpaRepository<ReleasedFeaturesEntity, Long> {
}
