package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;
import java.util.List;

public class TrainingPeriode {
  private String label;
  private LocalDate begin;
  private LocalDate end;
  private String schoolClass;
  private List<DataMonth> months;
  private List<SchoolSubject> schoolSubjects;

  public LocalDate getBegin() {
    return begin;
  }

  public LocalDate getEnd() {
    return end;
  }

  public String getLabel() {
    return label;
  }

  public List<DataMonth> getMonths() {
    return months;
  }

  public String getSchoolClass() {
    return schoolClass;
  }

  public List<SchoolSubject> getSchoolSubjects() {
    return schoolSubjects;
  }

  public void setBegin(LocalDate begin) {
    this.begin = begin;
  }

  public void setEnd(LocalDate end) {
    this.end = end;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setMonths(List<DataMonth> months) {
    this.months = months;
  }

  public void setSchoolClass(String schoolClass) {
    this.schoolClass = schoolClass;
  }

  public void setSchoolSubjects(List<SchoolSubject> schoolSubjects) {
    this.schoolSubjects = schoolSubjects;
  }
}
