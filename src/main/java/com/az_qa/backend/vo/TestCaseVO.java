/*

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.vo;

import com.az_qa.backend.enumeration.TestCaseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TestCaseVO {

  private Long id;
  private String code;

  @NotBlank private String title;

  @NotNull private Long relatedFeature;

  private String description;

  @NotNull private TestCaseType type;

  private String preconditions;
  private String postconditions;
  private String inputs;

  @NotBlank private String steps;

  @NotBlank private String expectedOutput;

  private Boolean active;

  public TestCaseVO() {}

  public TestCaseVO(
      Long id,
      String code,
      String title,
      Long relatedFeature,
      String description,
      TestCaseType type,
      String preconditions,
      String postconditions,
      String inputs,
      String steps,
      String expectedOutput,
      Boolean active) {

    this.id = id;
    this.code = code;
    this.title = title;
    this.relatedFeature = relatedFeature;
    this.description = description;
    this.type = type;
    this.preconditions = preconditions;
    this.postconditions = postconditions;
    this.inputs = inputs;
    this.steps = steps;
    this.expectedOutput = expectedOutput;
    this.active = active;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getCode() {
    return code;
  }

  public Long getRelatedFeature() {
    return relatedFeature;
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

  public void setRelatedFeature(Long relatedFeature) {
    this.relatedFeature = relatedFeature;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TestCaseVO that = (TestCaseVO) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (title != null ? !title.equals(that.title) : that.title != null) return false;
    if (relatedFeature != null
        ? !relatedFeature.equals(that.relatedFeature)
        : that.relatedFeature != null) return false;
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
    result = 31 * result + (relatedFeature != null ? relatedFeature.hashCode() : 0);
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
        + ", relatedFeature="
        + relatedFeature
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
