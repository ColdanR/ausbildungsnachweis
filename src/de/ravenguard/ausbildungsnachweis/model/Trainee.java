package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Trainee {
  private String familiyName;
  private String givenNames;
  private LocalDate begin;
  private LocalDate end;
  private String trainer;
  private String school;
  private String training;
  @XmlElementWrapper(name = "trainingPeriods")
  @XmlElement(name = "trainingPeriod")
  private final List<TrainingPeriod> trainingPeriods = new ArrayList<>();

  /**
   * empty argument constructor.
   */
  public Trainee() {}

  /**
   * Fields Constructor.
   *
   * @param familiyName family name
   * @param givenNames given name(s)
   * @param begin begin of training
   * @param end end of training
   * @param trainer name of trainer
   * @param school name of school
   * @param training training
   * @param trainingPeriods list of training periods
   * @throws IllegalDateException if either begin or end is not a working day or end is before begin
   */
  public Trainee(String familiyName, String givenNames, LocalDate begin, LocalDate end,
      String trainer, String school, String training, List<TrainingPeriod> trainingPeriods)
      throws IllegalDateException {
    super();
    setFamiliyName(familiyName);
    setGivenNames(givenNames);
    setBegin(begin);
    setEnd(end);
    setTrainer(trainer);
    setSchool(school);
    setTraining(training);
    setTrainingPeriodes(trainingPeriods);
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
      throw new IllegalArgumentException("Time conflict with existing periodes.");
    }

    trainingPeriods.add(trainingPeriod);
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
    final Trainee other = (Trainee) obj;
    if (begin == null) {
      if (other.begin != null) {
        return false;
      }
    } else if (!begin.equals(other.begin)) {
      return false;
    }
    if (end == null) {
      if (other.end != null) {
        return false;
      }
    } else if (!end.equals(other.end)) {
      return false;
    }
    if (familiyName == null) {
      if (other.familiyName != null) {
        return false;
      }
    } else if (!familiyName.equals(other.familiyName)) {
      return false;
    }
    if (givenNames == null) {
      if (other.givenNames != null) {
        return false;
      }
    } else if (!givenNames.equals(other.givenNames)) {
      return false;
    }
    if (school == null) {
      if (other.school != null) {
        return false;
      }
    } else if (!school.equals(other.school)) {
      return false;
    }
    if (trainer == null) {
      if (other.trainer != null) {
        return false;
      }
    } else if (!trainer.equals(other.trainer)) {
      return false;
    }
    if (training == null) {
      if (other.training != null) {
        return false;
      }
    } else if (!training.equals(other.training)) {
      return false;
    }
    if (trainingPeriods == null) {
      if (other.trainingPeriods != null) {
        return false;
      }
    } else if (!trainingPeriods.equals(other.trainingPeriods)) {
      return false;
    }
    return true;
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

  public String getTraining() {
    return training;
  }

  public List<TrainingPeriod> getTrainingPeriods() {
    return trainingPeriods;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (begin == null ? 0 : begin.hashCode());
    result = prime * result + (end == null ? 0 : end.hashCode());
    result = prime * result + (familiyName == null ? 0 : familiyName.hashCode());
    result = prime * result + (givenNames == null ? 0 : givenNames.hashCode());
    result = prime * result + (school == null ? 0 : school.hashCode());
    result = prime * result + (trainer == null ? 0 : trainer.hashCode());
    result = prime * result + (training == null ? 0 : training.hashCode());
    result = prime * result + (trainingPeriods == null ? 0 : trainingPeriods.hashCode());
    return result;
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
   * Sets the family name.
   *
   * @param familiyName family name, cannot be null or empty
   */
  public void setFamiliyName(String familiyName) {
    if (familiyName == null || familiyName.trim().length() == 0) {
      throw new IllegalArgumentException("familyName cannot be null or empty");
    }
    this.familiyName = familiyName.trim();
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
    this.givenNames = givenNames.trim();
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
    this.school = school.trim();
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
    this.trainer = trainer.trim();
  }

  /**
   * Sets the training.
   *
   * @param training training label, may not be null or empty
   */
  public void setTraining(String training) {
    if (training == null || training.trim().length() == 0) {
      throw new IllegalArgumentException("training cannot be null or empty");
    }
    this.training = training.trim();
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
        + ", end=" + end + ", trainer=" + trainer + ", school=" + school + ", training=" + training
        + ", trainingPeriods=" + trainingPeriods + "]";
  }
}
