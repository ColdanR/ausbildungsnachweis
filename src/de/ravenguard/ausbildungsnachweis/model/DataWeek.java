package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.logic.dao.LocalDateAdapter;
import de.ravenguard.ausbildungsnachweis.logic.dao.WeekTypeAdapter;
import java.time.LocalDate;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataWeek {
  @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
  private LocalDate begin;
  @XmlJavaTypeAdapter(value = WeekTypeAdapter.class)
  private WeekType type;
  private String notes;
  private String contentCompany;
  @XmlElementWrapper(name = "schoolContents")
  @XmlElement(name = "contentSchool")
  private List<ContentSchoolSubject> contentSchool;

  public LocalDate getBegin() {
    return begin;
  }

  public String getContentCompany() {
    return contentCompany;
  }

  public List<ContentSchoolSubject> getContentSchool() {
    return contentSchool;
  }

  public String getNotes() {
    return notes;
  }

  public WeekType getType() {
    return type;
  }

  public void setBegin(LocalDate begin) {
    this.begin = begin;
  }

  public void setContentCompany(String contentCompany) {
    this.contentCompany = contentCompany;
  }

  public void setContentSchool(List<ContentSchoolSubject> contentSchool) {
    this.contentSchool = contentSchool;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setType(WeekType type) {
    this.type = type;
  }
}
