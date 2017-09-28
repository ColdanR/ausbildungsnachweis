package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataMonth {
  private LocalDate begin;
  private LocalDate end;
  @XmlElementWrapper(name = "weeks")
  @XmlElement(name = "week")
  private final List<DataWeek> weeks = new ArrayList<>();

  /**
   * empty argument constructor.
   */
  public DataMonth() {
    ;
  }

  /**
   * Fields Constructor.
   *
   * @param begin begin of the month, may not be null
   * @param end end of the month, may not be null
   * @param weeks list of weeks, may not be null
   */
  public DataMonth(LocalDate begin, LocalDate end, List<DataWeek> weeks) {
    super();
    DateUtils.checkDate(begin);
    DateUtils.checkDate(end);
    DateUtils.checkDate(begin, end);
    if (weeks == null) {
      throw new IllegalArgumentException("weeks may not be null");
    }

    this.begin = begin;
    this.end = end;
    for (final DataWeek week : weeks) {
      addWeek(week);
    }
  }

  /**
   * Adds a week to the month.
   *
   * @param week week to add, may not be null. Will be validated.
   */
  public void addWeek(DataWeek week) {
    if (week == null) {
      throw new IllegalArgumentException("week cannot be null.");
    }

    if (week.getBegin().getMonth() != begin.getMonth()) {
      throw new IllegalArgumentException("Begin of week is not in month");
    }
    if (week.getEnd().getMonth() != end.getMonth()) {
      throw new IllegalArgumentException("End of week is not in month");
    }

    boolean conflict = false;
    for (final DataWeek temp : weeks) {
      if (week.getBegin().isAfter(temp.getBegin()) && week.getBegin().isBefore(temp.getEnd())
          || week.getEnd().isAfter(temp.getBegin()) && week.getEnd().isBefore(temp.getEnd())
          || week.getBegin().isBefore(temp.getBegin()) && week.getEnd().isAfter(temp.getEnd())) {
        conflict = true;
        break;
      }
    }
    if (conflict) {
      throw new IllegalArgumentException("week is within another week.");
    }

    weeks.add(week);
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
    final DataMonth other = (DataMonth) obj;
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
    if (weeks == null) {
      if (other.weeks != null) {
        return false;
      }
    } else if (!weeks.equals(other.weeks)) {
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

  public List<DataWeek> getWeeks() {
    return weeks;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (begin == null ? 0 : begin.hashCode());
    result = prime * result + (end == null ? 0 : end.hashCode());
    result = prime * result + (weeks == null ? 0 : weeks.hashCode());
    return result;
  }

  /**
   * Removes a week to the month.
   *
   * @param week week to remove, may not be null
   */
  public void removeWeek(DataWeek week) {
    if (week == null) {
      throw new IllegalArgumentException("week cannot be null.");
    }

    weeks.remove(week);
  }

  /**
   * Sets the begin of the month.
   *
   * @param begin begin to set, may not be null
   */
  public void setBegin(LocalDate begin) {
    DateUtils.checkDate(begin);
    DateUtils.checkDate(begin, end);

    this.begin = begin;
  }

  /**
   * Sets the end of the month.
   *
   * @param end end to set, may not be null
   */
  public void setEnd(LocalDate end) {
    DateUtils.checkDate(end);
    DateUtils.checkDate(begin, end);

    this.end = end;
  }

  /**
   * Sets the begin of the month.
   *
   * @param weeks weeks to set, may not be null
   */
  public void setWeeks(List<DataWeek> weeks) {
    if (weeks == null) {
      throw new IllegalArgumentException("weeks may not be null");
    }

    for (final DataWeek week : weeks) {
      addWeek(week);
    }
  }

  @Override
  public String toString() {
    return "DataMonth [begin=" + begin + ", end=" + end + ", weeks=" + weeks + "]";
  }
}
