/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.dao;

import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.mapper.ServicesMapper;
import com.az_qa.backend.repository.ServicesRepository;
import com.az_qa.backend.vo.ServicesVO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Data access component that coordinates service persistence operations.
 */
@Repository
public class ServicesDAO {

  /** Repository dependency used for service persistence operations. */
  @Autowired private ServicesRepository servicesRepository;

  /**
   * Finds a service by id with its URLs.
   *
   * @param id service identifier
   * @return service representation with URLs
   */
  public ServicesVO findById(Long id) {
    Optional<ServicesVO> servicesVO =
        servicesRepository.findByIdWithUrls(id).map(ServicesMapper::toVO);

    if (servicesVO.isEmpty()) {
      throw new ItemNotFoundException("Service with id " + id + " not found");
    }

    return servicesVO.get();
  }

  /**
   * Retrieves all existing services with their URLs.
   *
   * @return list of all services found
   */
  public List<ServicesVO> findAll() {
    return servicesRepository.findAllWithUrls().stream().map(ServicesMapper::toVO).toList();
  }
}
