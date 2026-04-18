/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import com.az_qa.backend.vo.ServicesVO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServicesService {
  // Implement the logic to retrieve all services from the database
  public List<ServicesVO> getAllServices() {
    // This is a placeholder implementation. You should replace it with actual
    // database retrieval logic.
    List<ServicesVO> services = new ArrayList<>();
    services.add(new ServicesVO(1L, "Service 1", "Description of Service 1"));
    services.add(new ServicesVO(2L, "Service 2", "Description of Service 2"));
    return services;
  }

  // Implement the logic to retrieve a service by its ID from the database
  public ServicesVO getServiceById(Long id) {
    // This is a placeholder implementation. You should replace it with actual
    // database retrieval logic.
    if (id == 1L) {
      return new ServicesVO(1L, "Service 1", "Description of Service 1");
    } else if (id == 2L) {
      return new ServicesVO(2L, "Service 2", "Description of Service 2");
    } else {
      return null; // Return null if the service is not found
    }
  }
}
