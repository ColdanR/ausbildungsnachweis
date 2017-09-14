package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;

public class SchoolSubject {
  private String label;
  private LocalDate exemptSince = null;

  /**
   * Label Field Constructor.
   * 
   * @param label label of the SchoolSubject, may not be null or empty.
   */
  public SchoolSubject(String label) {
    if (label == null || label.trim().length() == 0) {
      throw new IllegalArgumentException("label may not be null or empty.");
    }

    this.label = label.trim();
  }

  /**
   * Field Constructor.
   * 
   * @param label label of the SchoolSubject, may not be null or empty.
   * @param exemptSince LocalDate of exempt for this SchoolSubject
   */
  public SchoolSubject(String label, LocalDate exemptSince) {
    super();
    if (label == null || label.trim().length() == 0) {
      throw new IllegalArgumentException("label may not be null or empty.");
    }

    this.label = label.trim();
    this.exemptSince = exemptSince;
  }

  public LocalDate getExemptSince() {
    return exemptSince;
  }

  public String getLabel() {
    return label;
  }

  public void setExemptSince(LocalDate exemptSince) {
    this.exemptSince = exemptSince;
  }

  /**
   * Sets the label of the school subject.
   *
   * @param label label to set, may not be null or empty
   */
  public void setLabel(String label) {
    if (label == null || label.trim().length() == 0) {
      throw new IllegalArgumentException("label may not be null or empty.");
    }
    this.label = label.trim();
  }
}
