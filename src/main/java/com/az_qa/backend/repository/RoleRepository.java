package com.az_qa.backend.repository;

import com.az_qa.backend.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {}

// placeholder for RoleRepository, not implemented yet
