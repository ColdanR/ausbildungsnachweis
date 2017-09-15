package de.ravenguard.ausbildungsnachweis.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ContentSchoolSubject {
  private SchoolSubject subject;
  private String content;

  public String getContent() {
    return content;
  }

  public SchoolSubject getSubject() {
    return subject;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setSubject(SchoolSubject subject) {
    this.subject = subject;
  }

  @Override
  public String toString() {
    return "ContentSchoolSubject [subject=" + subject + ", content=" + content + "]";
  }
}
