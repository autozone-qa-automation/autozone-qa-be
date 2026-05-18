/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.vo;

import com.az_qa.backend.enumeration.TestCaseType;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TestCaseVO {

  private Long id;

  @Schema(hidden = true)
  private String code;

  @NotBlank private String title;

  @JsonProperty("relatedFeature")
  @JsonAlias("featureId")
  @NotNull
  private Long featureId;

  private String description;

  @NotNull private TestCaseType type;

  private String preconditions;
  private String postconditions;
  private String inputs;

  @NotBlank private String steps;

  @NotBlank private String expectedOutput;

  @Schema(hidden = true)
  private Boolean active;

  private Long releaseId;
  private String featureName;

  @Schema(hidden = true)
  private FeatureVO feature;

  @Schema(hidden = true)
  private ReleaseVO release;

  public TestCaseVO() {}

  // public TestCaseVO(
  //     Long id,
  //     String code,
  //     String title,
  //     Long featureId,
  //     String description,
  //     TestCaseType type,
  //     String preconditions,
  //     String postconditions,
  //     String inputs,
  //     String steps,
  //     String expectedOutput,
  //     Boolean active) {

  //   this.id = id;
  //   this.code = code;
  //   this.title = title;
  //   this.featureId = featureId;
  //   this.description = description;
  //   this.type = type;
  //   this.preconditions = preconditions;
  //   this.postconditions = postconditions;
  //   this.inputs = inputs;
  //   this.steps = steps;
  //   this.expectedOutput = expectedOutput;
  //   this.active = active;
  // }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getCode() {
    return code;
  }

  public Long getFeatureId() {
    return featureId;
  }

  public String getDescription() {
    return description;
  }

  public TestCaseType getType() {
    return type;
  }

  public String getPreconditions() {
    return preconditions;
  }

  public String getPostconditions() {
    return postconditions;
  }

  public String getInputs() {
    return inputs;
  }

  public String getSteps() {
    return steps;
  }

  public String getExpectedOutput() {
    return expectedOutput;
  }

  public Boolean getActive() {
    return active;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setFeatureId(Long featureId) {
    this.featureId = featureId;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setType(TestCaseType type) {
    this.type = type;
  }

  public void setPreconditions(String preconditions) {
    this.preconditions = preconditions;
  }

  public void setPostconditions(String postconditions) {
    this.postconditions = postconditions;
  }

  public void setInputs(String inputs) {
    this.inputs = inputs;
  }

  public void setSteps(String steps) {
    this.steps = steps;
  }

  public void setExpectedOutput(String expectedOutput) {
    this.expectedOutput = expectedOutput;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Long getReleaseId() {
    return releaseId;
  }

  public void setReleaseId(Long releaseId) {
    this.releaseId = releaseId;
  }

  public String getFeatureName() {
    return featureName;
  }

  public void setFeatureName(String featureName) {
    this.featureName = featureName;
  }

  public FeatureVO getFeature() {
    return feature;
  }

  public void setFeature(FeatureVO feature) {
    this.feature = feature;
  }

  public ReleaseVO getRelease() {
    return release;
  }

  public void setRelease(ReleaseVO release) {
    this.release = release;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TestCaseVO that = (TestCaseVO) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (title != null ? !title.equals(that.title) : that.title != null) return false;
    // cambiar por featureId en futuro
    if (featureId != null ? !featureId.equals(that.featureId) : that.featureId != null)
      return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;
    if (type != null ? !type.equals(that.type) : that.type != null) return false;
    if (preconditions != null
        ? !preconditions.equals(that.preconditions)
        : that.preconditions != null) return false;
    if (postconditions != null
        ? !postconditions.equals(that.postconditions)
        : that.postconditions != null) return false;
    if (inputs != null ? !inputs.equals(that.inputs) : that.inputs != null) return false;
    if (steps != null ? !steps.equals(that.steps) : that.steps != null) return false;
    return expectedOutput != null
        ? expectedOutput.equals(that.expectedOutput)
        : that.expectedOutput == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (featureId != null ? featureId.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (preconditions != null ? preconditions.hashCode() : 0);
    result = 31 * result + (postconditions != null ? postconditions.hashCode() : 0);
    result = 31 * result + (inputs != null ? inputs.hashCode() : 0);
    result = 31 * result + (steps != null ? steps.hashCode() : 0);
    result = 31 * result + (expectedOutput != null ? expectedOutput.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TestCaseVO{"
        + "id="
        + id
        + ", code='"
        + code
        + '\''
        + ", title='"
        + title
        + '\''
        + ", featureId="
        + featureId
        + ", description='"
        + description
        + '\''
        + ", type="
        + type
        + ", preconditions='"
        + preconditions
        + '\''
        + ", postconditions='"
        + postconditions
        + '\''
        + ", inputs='"
        + inputs
        + '\''
        + ", steps='"
        + steps
        + '\''
        + ", expectedOutput='"
        + expectedOutput
        + '\''
        + '}';
  }
}
