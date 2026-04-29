package com.az_qa.backend.repository;

import com.az_qa.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {}

// placeholder for UserRepository, not implemented yet
