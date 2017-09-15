package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.logic.dao.LocalDateAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Trainee {
  private String familiyName;
  private String givenNames;
  @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
  private LocalDate begin;
  @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
  private LocalDate end;
  private String trainer;
  private String school;
  @XmlElementWrapper(name = "trainingPeriods")
  @XmlElement(name = "trainingPeriod")
  private final List<TrainingPeriod> trainingPeriods = new ArrayList<>();

  /**
   * empty argument constructor.
   */
  public Trainee() {
    ;
  }

  /**
   * Fields Constructor.
   *
   * @param familiyName family name
   * @param givenNames given name(s)
   * @param begin begin of training
   * @param end end of training
   * @param trainer name of trainer
   * @param school name of school
   * @param trainingPeriods list of training periods
   */
  public Trainee(String familiyName, String givenNames, LocalDate begin, LocalDate end,
      String trainer, String school, List<TrainingPeriod> trainingPeriods) {
    super();
    if (familiyName == null || familiyName.trim().length() == 0) {
      throw new IllegalArgumentException("familyName cannot be null or empty");
    }
    if (givenNames == null || givenNames.trim().length() == 0) {
      throw new IllegalArgumentException("givenNames cannot be null or empty");
    }
    if (begin == null) {
      throw new IllegalArgumentException("begin cannot be null");
    }
    if (end == null) {
      throw new IllegalArgumentException("end cannot be null");
    }
    if (trainer == null || trainer.trim().length() == 0) {
      throw new IllegalArgumentException("trainer cannot be null or empty");
    }
    if (school == null || school.trim().length() == 0) {
      throw new IllegalArgumentException("school cannot be null or empty");
    }
    this.familiyName = familiyName.trim();
    this.givenNames = givenNames.trim();
    this.begin = begin;
    this.end = end;
    this.trainer = trainer.trim();
    this.school = school.trim();
    trainingPeriods.forEach(trainingPeriod -> {
      addTrainingPeriode(trainingPeriod);
    });
  }

  /**
   * Adds a training period to the list.
   *
   * @param trainingPeriod training period to add, may not be null.
   */
  public void addTrainingPeriode(TrainingPeriod trainingPeriod) {
    if (trainingPeriod == null) {
      throw new IllegalArgumentException("trainingPeriod cannot be null");
    }

    final boolean conflict = trainingPeriods.stream().anyMatch(periode -> {
      final LocalDate begin = periode.getBegin();
      final LocalDate end = periode.getEnd();
      return begin.isAfter(trainingPeriod.getBegin()) && begin.isBefore(trainingPeriod.getEnd())
          || end.isAfter(trainingPeriod.getBegin()) && end.isBefore(trainingPeriod.getEnd())
          || begin.isBefore(trainingPeriod.getBegin()) && end.isAfter(trainingPeriod.getEnd())
          || begin.equals(trainingPeriod.getBegin()) || begin.equals(trainingPeriod.getEnd())
          || end.equals(trainingPeriod.getBegin()) || end.equals(trainingPeriod.getEnd());
    });

    if (conflict) {
      throw new IllegalArgumentException("Time Conflict with existing periodes.");
    }

    trainingPeriods.add(trainingPeriod);
  }

  public LocalDate getBegin() {
    return begin;
  }

  public LocalDate getEnd() {
    return end;
  }

  public String getFamiliyName() {
    return familiyName;
  }

  public String getGivenNames() {
    return givenNames;
  }

  public String getSchool() {
    return school;
  }

  public String getTrainer() {
    return trainer;
  }

  public List<TrainingPeriod> getTrainingPeriods() {
    return trainingPeriods;
  }

  /**
   * Sets the begin date.
   *
   * @param begin LocalDate of the begin, cannot be null.
   */
  public void setBegin(LocalDate begin) {
    if (begin == null) {
      throw new IllegalArgumentException("begin cannot be null");
    }
    this.begin = begin;
  }

  /**
   * Sets the end date.
   *
   * @param end LocalDate of the end, cannot be null.
   */
  public void setEnd(LocalDate end) {
    if (end == null) {
      throw new IllegalArgumentException("end cannot be null");
    }
    this.end = end;
  }

  /**
   * Sets the family name.
   *
   * @param familiyName family name, cannot be null or empty
   */
  public void setFamiliyName(String familiyName) {
    if (familiyName == null || familiyName.trim().length() == 0) {
      throw new IllegalArgumentException("familyName cannot be null or empty");
    }
    this.familiyName = familiyName;
  }

  /**
   * Sets the given name(s).
   *
   * @param givenNames given name(s), cannot be null or empty.
   */
  public void setGivenNames(String givenNames) {
    if (givenNames == null || givenNames.trim().length() == 0) {
      throw new IllegalArgumentException("givenNames cannot be null or empty");
    }
    this.givenNames = givenNames;
  }

  /**
   * Sets the school name.
   *
   * @param school name of the school, cannot be null or empty.
   */
  public void setSchool(String school) {
    if (school == null || school.trim().length() == 0) {
      throw new IllegalArgumentException("school cannot be null or empty");
    }
    this.school = school;
  }

  /**
   * Sets the trainer name.
   *
   * @param trainer trainer name, cannot be null or empty.
   */
  public void setTrainer(String trainer) {
    if (trainer == null || trainer.trim().length() == 0) {
      throw new IllegalArgumentException("trainer cannot be null or empty");
    }
    this.trainer = trainer;
  }

  /**
   * Sets a new list for training periods.
   *
   * @param trainingPeriods List of training periods
   */
  public void setTrainingPeriodes(List<TrainingPeriod> trainingPeriods) {
    if (trainingPeriods == null) {
      throw new NullPointerException("trainingPeriods cannot be null.");
    }

    this.trainingPeriods.clear();
    trainingPeriods.forEach(period -> {
      addTrainingPeriode(period);
    });
  }

  @Override
  public String toString() {
    return "Trainee [familiyName=" + familiyName + ", givenNames=" + givenNames + ", begin=" + begin
        + ", end=" + end + ", trainer=" + trainer + ", school=" + school + ", trainingPeriods="
        + trainingPeriods + "]";
  }
}
