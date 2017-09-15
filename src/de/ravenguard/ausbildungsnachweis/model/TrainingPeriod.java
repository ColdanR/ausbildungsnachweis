package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.logic.dao.LocalDateAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingPeriod {
  private String label;
  @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
  private LocalDate begin;
  @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
  private LocalDate end;
  private String schoolClass;
  @XmlElementWrapper(name = "months")
  @XmlElement(name = "month")
  private final List<DataMonth> months = new ArrayList<>();
  @XmlElementWrapper(name = "schoolSubjects")
  @XmlElement(name = "schoolSubject")
  private final List<SchoolSubject> schoolSubjects = new ArrayList<>();

  /**
   * empty argument constructor.
   */
  public TrainingPeriod() {
    ;
  }

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

  /**
   * Removes a month from the training period.
   *
   * @param month the month to remove, may not be null.
   */
  public void removeMonth(DataMonth month) {
    if (month == null) {
      throw new IllegalArgumentException("month cannot be null.");
    }

    months.remove(month);
  }

  /**
   * Removes a SchoolSubject from the training period.
   *
   * @param subject the subject to remove, may not be null.
   */
  public void removeSchoolSubject(SchoolSubject subject) {
    if (subject == null) {
      throw new IllegalArgumentException("subject cannot be null.");
    }

    schoolSubjects.remove(subject);
  }

  /**
   * Sets the begin of the period.
   *
   * @param begin LocalDate of the begin, may not be null
   */
  public void setBegin(LocalDate begin) {
    if (begin == null) {
      throw new IllegalArgumentException("begin cannot be null.");
    }

    this.begin = begin;
  }

  /**
   * Sets the end of the period.
   *
   * @param end LocalDate of the end, may not be null
   */
  public void setEnd(LocalDate end) {
    if (end == null) {
      throw new IllegalArgumentException("end cannot be null.");
    }

    this.end = end;
  }

  /**
   * Sets the label of the period.
   *
   * @param label label of the period, may not be empty or null
   */
  public void setLabel(String label) {
    if (label == null || label.trim().length() == 0) {
      throw new IllegalArgumentException("label cannot be null or empty.");
    }

    this.label = label.trim();
  }

  /**
   * Sets the months.
   *
   * @param months List of months to add, may not be null.
   */
  public void setMonths(List<DataMonth> months) {
    if (months == null) {
      throw new IllegalArgumentException("months cannot be null.");
    }

    this.months.clear();
    months.forEach(month -> {
      addMonth(month);
    });
  }

  /**
   * Sets the schoolClass of the period.
   *
   * @param schoolClass schoolClass of the period, may not be empty or null
   */
  public void setSchoolClass(String schoolClass) {
    if (schoolClass == null || schoolClass.trim().length() == 0) {
      throw new IllegalArgumentException("schoolClass cannot be null or empty.");
    }

    this.schoolClass = schoolClass.trim();
  }

  /**
   * Sets the schoolSubjects.
   *
   * @param schoolSubjects List of schoolSubjects to add, may not be null.
   */
  public void setSchoolSubjects(List<SchoolSubject> schoolSubjects) {
    if (schoolSubjects == null) {
      throw new IllegalArgumentException("schoolSubjects cannot be null.");
    }

    this.schoolSubjects.clear();
    schoolSubjects.forEach(subject -> {
      addSchoolSubject(subject);
    });
  }

  @Override
  public String toString() {
    return "TrainingPeriod [label=" + label + ", begin=" + begin + ", end=" + end + ", schoolClass="
        + schoolClass + ", months=" + months + ", schoolSubjects=" + schoolSubjects + "]";
  }
}
