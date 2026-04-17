package com.az_qa.backend.dto.response;

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
