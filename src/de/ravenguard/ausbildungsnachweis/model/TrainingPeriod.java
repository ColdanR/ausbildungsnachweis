package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.gui.Utils;
import de.ravenguard.ausbildungsnachweis.utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingPeriod implements TreeElement {
  private String label;
  private LocalDate begin;
  private LocalDate end;
  private String schoolClass;
  private String classTeacher;
  @XmlElementWrapper(name = "months")
  @XmlElement(name = "month")
  private final List<DataMonth> months = new ArrayList<>();
  @XmlElementWrapper(name = "schoolSubjects")
  @XmlElement(name = "schoolSubject")
  private final List<SchoolSubject> schoolSubjects = new ArrayList<>();

  /**
   * empty argument constructor.
   */
  public TrainingPeriod() {}

  /**
   * Field Constructor.
   *
   * @param label label, may not be null or empty
   * @param begin begin of period, may not be null
   * @param end end of period, may not be null
   * @param schoolClass schoolClass, may not be null or empty
   * @param classTeacher class teacher, may not be null or empty
   * @param months months of period, may not be null
   * @param schoolSubjects schoolSubjects, may not be null
   * @throws IllegalDateException if begin or end is not a working day or end is before begin
   */
  public TrainingPeriod(String label, LocalDate begin, LocalDate end, String schoolClass,
      String classTeacher, List<DataMonth> months, List<SchoolSubject> schoolSubjects)
      throws IllegalDateException {
    super();
    setLabel(label);
    setBegin(begin);
    setEnd(end);
    setSchoolClass(schoolClass);
    setClassTeacher(classTeacher);
    setMonths(months);
    setSchoolSubjects(schoolSubjects);
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
    final TrainingPeriod other = (TrainingPeriod) obj;
    if (begin == null) {
      if (other.begin != null) {
        return false;
      }
    } else if (!begin.equals(other.begin)) {
      return false;
    }
    if (classTeacher == null) {
      if (other.classTeacher != null) {
        return false;
      }
    } else if (!classTeacher.equals(other.classTeacher)) {
      return false;
    }
    if (end == null) {
      if (other.end != null) {
        return false;
      }
    } else if (!end.equals(other.end)) {
      return false;
    }
    if (label == null) {
      if (other.label != null) {
        return false;
      }
    } else if (!label.equals(other.label)) {
      return false;
    }
    if (months == null) {
      if (other.months != null) {
        return false;
      }
    } else if (!months.equals(other.months)) {
      return false;
    }
    if (schoolClass == null) {
      if (other.schoolClass != null) {
        return false;
      }
    } else if (!schoolClass.equals(other.schoolClass)) {
      return false;
    }
    if (schoolSubjects == null) {
      if (other.schoolSubjects != null) {
        return false;
      }
    } else if (!schoolSubjects.equals(other.schoolSubjects)) {
      return false;
    }
    return true;
  }

  public LocalDate getBegin() {
    return begin;
  }

  @Override
  public List<DataMonth> getChildren() {
    return getMonths();
  }

  public String getClassTeacher() {
    return classTeacher;
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

  @Override
  public String getTreeLabel() {
    return "Ausbildungszeitraum vom " + Utils.formatDate(begin) + " bis " + Utils.formatDate(end);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (begin == null ? 0 : begin.hashCode());
    result = prime * result + (classTeacher == null ? 0 : classTeacher.hashCode());
    result = prime * result + (end == null ? 0 : end.hashCode());
    result = prime * result + (label == null ? 0 : label.hashCode());
    result = prime * result + (months == null ? 0 : months.hashCode());
    result = prime * result + (schoolClass == null ? 0 : schoolClass.hashCode());
    result = prime * result + (schoolSubjects == null ? 0 : schoolSubjects.hashCode());
    return result;
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
   * Sets the begin date.
   *
   * @param begin LocalDate of the begin, cannot be null.
   * @throws IllegalDateException if begin is not a working day or is after end
   */
  public void setBegin(LocalDate begin) throws IllegalDateException {
    if (begin == null) {
      throw new NullPointerException("begin may not be null.");
    }
    if (!DateUtils.checkWorkday(begin)) {
      throw new IllegalDateException("begin must be a working day.");
    }
    if (end != null && begin.isAfter(end)) {
      throw new IllegalDateException("begin may not be after end.");
    }

    this.begin = begin;
  }

  /**
   * Sets the class teacher of the period.
   *
   * @param classTeacher class teacher to set, may not be null or empty
   */
  public void setClassTeacher(String classTeacher) {
    if (classTeacher == null || classTeacher.trim().length() == 0) {
      throw new IllegalArgumentException("classTeacher cannot be null or empty.");
    }

    this.classTeacher = classTeacher.trim();
  }

  /**
   * Sets the end date.
   *
   * @param end LocalDate of the end, cannot be null.
   * @throws IllegalDateException if end is not a working day or end is before begin
   */
  public void setEnd(LocalDate end) throws IllegalDateException {
    if (end == null) {
      throw new NullPointerException("end may not be null.");
    }
    if (!DateUtils.checkWorkday(end)) {
      throw new IllegalDateException("end must be a working day.");
    }
    if (end != null && end.isBefore(begin)) {
      throw new IllegalDateException("end may not be before begin.");
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
    return getTreeLabel();
  }
}
