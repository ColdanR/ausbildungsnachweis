package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SchoolSubject {
  private String label;
  private LocalDate exemptSince = null;

  /**
   * empty argument constructor.
   */
  public SchoolSubject() {
    ;
  }

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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SchoolSubject other = (SchoolSubject) obj;
    if (exemptSince == null) {
      if (other.exemptSince != null) {
        return false;
      }
    } else if (!exemptSince.equals(other.exemptSince)) {
      return false;
    }
    if (label == null) {
      if (other.label != null) {
        return false;
      }
    } else if (!label.equals(other.label)) {
      return false;
    }
    return true;
  }

  public LocalDate getExemptSince() {
    return exemptSince;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (exemptSince == null ? 0 : exemptSince.hashCode());
    result = prime * result + (label == null ? 0 : label.hashCode());
    return result;
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

  @Override
  public String toString() {
    return "SchoolSubject [label=" + label + ", exemptSince=" + exemptSince + "]";
  }
}
