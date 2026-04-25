/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.entity;

import com.az_qa.backend.enumeration.TestCaseType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_cases")
public class TestCasesEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  @Column(nullable = false)
  private String title;

  // Relationships

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idFeature", nullable = false)
  private FeatureEntity feature;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "release_id")
  private ReleaseEntity release;

  // End of relationships

  @Column(columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TestCaseType type;

  @Column(columnDefinition = "TEXT")
  private String preconditions;

  @Column(columnDefinition = "TEXT")
  private String postconditions;

  @Column(columnDefinition = "TEXT")
  private String inputs;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String steps;

  @Column(name = "expected_output", nullable = false, columnDefinition = "TEXT")
  private String expectedOutput;

  @Column(nullable = false)
  private Boolean active = true;

  public TestCasesEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public FeatureEntity getFeature() {
    return feature;
  }

  public void setFeature(FeatureEntity feature) {
    this.feature = feature;
  }

  public ReleaseEntity getRelease() {
    return release;
  }

  public void setRelease(ReleaseEntity release) {
    this.release = release;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TestCaseType getType() {
    return type;
  }

  public void setType(TestCaseType type) {
    this.type = type;
  }

  public String getPreconditions() {
    return preconditions;
  }

  public void setPreconditions(String preconditions) {
    this.preconditions = preconditions;
  }

  public String getPostconditions() {
    return postconditions;
  }

  public void setPostconditions(String postconditions) {
    this.postconditions = postconditions;
  }

  public String getInputs() {
    return inputs;
  }

  public void setInputs(String inputs) {
    this.inputs = inputs;
  }

  public String getSteps() {
    return steps;
  }

  public void setSteps(String steps) {
    this.steps = steps;
  }

  public String getExpectedOutput() {
    return expectedOutput;
  }

  public void setExpectedOutput(String expectedOutput) {
    this.expectedOutput = expectedOutput;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
