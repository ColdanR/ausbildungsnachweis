package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;
import java.util.List;

public class TrainingPeriod {
  private String label;
  private LocalDate begin;
  private LocalDate end;
  private String schoolClass;
  private List<DataMonth> months;
  private List<SchoolSubject> schoolSubjects;

  /**
   * Field Constructor.
   * 
   * @param label label, may not be null or empty
   * @param begin begin of period, may not be null
   * @param end end of period, may not be null
   * @param schoolClass schoolClass, may not be null or empty
   * @param months months of period, may not be null
   * @param schoolSubjects schoolSubjects, may not be null
   */
  public TrainingPeriod(String label, LocalDate begin, LocalDate end, String schoolClass,
      List<DataMonth> months, List<SchoolSubject> schoolSubjects) {
    super();

    if (label == null || label.trim().length() == 0) {
      throw new IllegalArgumentException("label cannot be null or empty.");
    }

    if (begin == null) {
      throw new IllegalArgumentException("begin cannot be null.");
    }

    if (end == null) {
      throw new IllegalArgumentException("end cannot be null.");
    }

    if (schoolClass == null || schoolClass.trim().length() == 0) {
      throw new IllegalArgumentException("schoolClass cannot be null or empty.");
    }

    if (months == null) {
      throw new IllegalArgumentException("months cannot be null.");
    }

    if (schoolSubjects == null) {
      throw new IllegalArgumentException("schoolSubjects cannot be null.");
    }
    this.label = label.trim();
    this.begin = begin;
    this.end = end;
    this.schoolClass = schoolClass.trim();
    months.forEach(month -> {
      addMonth(month);
    });
    schoolSubjects.forEach(schoolSubject -> {
      addSchoolSubject(schoolSubject);
    });
  }

  /**
   * Adds a month to the training period.
   * 
   * @param month month to add, may not be null, not already in the list and must be within the
   *        bonds of the period
   */
  public void addMonth(DataMonth month) {
    if (month == null) {
      throw new IllegalArgumentException("month cannot be null.");
    }

    final boolean conflict = months.stream().anyMatch(currentMonth -> {
      return currentMonth.getBegin().getMonth() == month.getBegin().getMonth()
          || month.getBegin().isBefore(begin) || month.getBegin().isAfter(end);
    });

    if (conflict) {
      throw new IllegalArgumentException(
          "month allready added or before begin or after end of period.");
    }

    months.add(month);
  }

  /**
   * Adds a school subject to the training period.
   * 
   * @param schoolSubject school subject to add, may not be null and not already in the list
   */
  public void addSchoolSubject(SchoolSubject schoolSubject) {
    if (schoolSubject == null) {
      throw new IllegalArgumentException("schoolSubject cannot be null.");
    }

    final boolean conflict = schoolSubjects.stream().anyMatch(currentSchoolSubject -> {
      return currentSchoolSubject.getLabel().equalsIgnoreCase(schoolSubject.getLabel());
    });

    if (conflict) {
      throw new IllegalArgumentException(
          "schoolSubject allready added or before begin or after end of period.");
    }

    schoolSubjects.add(schoolSubject);
  }

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
    if (begin == null) {
      throw new IllegalArgumentException("begin cannot be null.");
    }

    this.begin = begin;
  }

  public void setEnd(LocalDate end) {
    if (end == null) {
      throw new IllegalArgumentException("end cannot be null.");
    }

    this.end = end;
  }

  public void setLabel(String label) {
    if (label == null || label.trim().length() == 0) {
      throw new IllegalArgumentException("label cannot be null or empty.");
    }

    this.label = label;
  }

  public void setMonths(List<DataMonth> months) {
    if (months == null) {
      throw new IllegalArgumentException("months cannot be null.");
    }

    this.months = months;
  }

  public void setSchoolClass(String schoolClass) {
    if (schoolClass == null || schoolClass.trim().length() == 0) {
      throw new IllegalArgumentException("schoolClass cannot be null or empty.");
    }

    this.schoolClass = schoolClass;
  }

  public void setSchoolSubjects(List<SchoolSubject> schoolSubjects) {
    if (schoolSubjects == null) {
      throw new IllegalArgumentException("schoolSubjects cannot be null.");
    }

    this.schoolSubjects = schoolSubjects;
  }
}
