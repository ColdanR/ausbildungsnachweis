package de.ravenguard.ausbildungsnachweis.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ContentSchoolSubject {
  private SchoolSubject subject;
  private String content;

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
    final ContentSchoolSubject other = (ContentSchoolSubject) obj;
    if (content == null) {
      if (other.content != null) {
        return false;
      }
    } else if (!content.equals(other.content)) {
      return false;
    }
    if (subject == null) {
      if (other.subject != null) {
        return false;
      }
    } else if (!subject.equals(other.subject)) {
      return false;
    }
    return true;
  }

  public String getContent() {
    return content;
  }

  public SchoolSubject getSubject() {
    return subject;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (content == null ? 0 : content.hashCode());
    result = prime * result + (subject == null ? 0 : subject.hashCode());
    return result;
  }

  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Sets the {@link SchoolSubject}.
   *
   * @param subject {@link SchoolSubject} to set, may not be null
   */
  public void setSubject(SchoolSubject subject) {
    if (subject == null) {
      throw new IllegalArgumentException("Subject may not be null.");
    }
    this.subject = subject;
  }

  @Override
  public String toString() {
    return "ContentSchoolSubject [subject=" + subject + ", content=" + content + "]";
  }
}
