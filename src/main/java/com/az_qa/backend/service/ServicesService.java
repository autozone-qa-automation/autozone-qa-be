/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.service;

import com.az_qa.backend.dao.ServicesDAO;
import com.az_qa.backend.vo.ServicesVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service component that handles business logic for services.
 */
@Service
public class ServicesService {

  /** DAO dependency used for service data access operations. */
  @Autowired
  private ServicesDAO servicesDAO;

  /**
   * Retrieves all existing services with their URLs.
   *
   * @return list of all services found
   */
  public List<ServicesVO> getAllServices() {
    return servicesDAO.findAll();
  }

  /**
   * Finds a service by id with its URLs.
   *
   * @param id service identifier
   * @return service representation with URLs
   */
  public ServicesVO getServiceById(Long id) {
    return servicesDAO.findById(id);
  }
}