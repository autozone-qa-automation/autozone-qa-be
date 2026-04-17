package com.az_qa.backend.dto.response;
/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

public class TestCasesResponse {
    private Long id;
    private String name;
    private String description;
    private Long featureId;
    
    public TestCasesResponse(Long id, String name, String description, Long featureId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.featureId = featureId;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Long getFeatureId() {
        return featureId;
    }
    
}
