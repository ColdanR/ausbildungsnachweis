package de.ravenguard.ausbildungsnachweis.model;

import java.util.List;

public class ContentSchoolSubject {
  private SchoolSubject subject;
  private List<String> content;

  public List<String> getContent() {
    return content;
  }

  public SchoolSubject getSubject() {
    return subject;
  }

  public void setContent(List<String> content) {
    this.content = content;
  }

  public void setSubject(SchoolSubject subject) {
    this.subject = subject;
  }
}
