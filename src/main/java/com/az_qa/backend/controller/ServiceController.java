/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.controller;

import com.az_qa.backend.service.ServicesService;

// Ensure the correct package path for ServicesService or create the class if it
// doesn't exist

import com.az_qa.backend.vo.ServicesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@Validated
public class ServiceController {

    @Autowired
    private ServicesService servicesService;

    @GetMapping
    public ResponseEntity<List<ServicesVO>> getAllServices() {
        List<ServicesVO> services = servicesService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicesVO> getServiceById(@PathVariable Long id) {
        ServicesVO service = servicesService.getServiceById(id);
        if (service != null) {
            return new ResponseEntity<>(service, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
