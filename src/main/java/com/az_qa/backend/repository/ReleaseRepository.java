package com.az_qa.backend.repository;

public class ReleaseRepository {
    
import com.az_qa.backend.entity.ReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {


}
