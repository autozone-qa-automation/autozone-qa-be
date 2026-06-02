/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.enumeration;

/**
 * Enumeration representing the possible states of a software release.
 * Includes logic to validate allowed state transitions.
 */
public enum ReleaseStatus {
  Draft,
  Progress,
  Active;

  /**
   * Determines if a transition to a target status is valid.
   * * @param nextStatus the desired status to transition to
   * @return true if the transition is allowed, false otherwise
   */
  public boolean canTransitionTo(ReleaseStatus nextStatus) {
    if (this == nextStatus) return true;

    return switch (this) {
      case Draft -> (nextStatus == Progress || nextStatus == Active);
      case Progress -> (nextStatus == Active);
      case Active -> false;
    };
  }
}
