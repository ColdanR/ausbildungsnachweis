package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.utils.DateUtils;
import de.ravenguard.ausbildungsnachweis.utils.Utils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataMonth implements TreeElement {
  private static final Logger LOGGER = LogManager.getLogger(DataMonth.class);
  private LocalDate begin;
  private LocalDate end;
  @XmlElementWrapper(name = "weeks")
  @XmlElement(name = "week")
  private final List<DataWeek> weeks = new ArrayList<>();

  /**
   * empty argument constructor.
   */
  public DataMonth() {
    LOGGER.trace("Called DataMonth()");
  }

  /**
   * Fields Constructor.
   *
   * @param begin begin of the month, may not be null
   * @param end end of the month, may not be null
   * @param weeks list of weeks, may not be null
   * @throws IllegalDateException If begin or end is not a working day or begin is after end.
   */
  public DataMonth(LocalDate begin, LocalDate end, List<DataWeek> weeks)
      throws IllegalDateException {
    LOGGER.trace("Called DataMonth(begin: {}, end: {}, weeks: {})", begin, end, weeks);
    if (weeks == null) {
      throw new NullPointerException("weeks may not be null");
    }

    setBegin(begin);
    setEnd(end);
    for (final DataWeek week : weeks) {
      addWeek(week);
    }
  }

  /**
   * Adds a week to the month.
   *
   * @param week week to add, may not be null. Will be validated.
   * @throws IllegalDateException if week is not within month or already in month.
   */
  public void addWeek(DataWeek week) throws IllegalDateException {
    LOGGER.trace("Called addWeek(week: {})", week);
    if (week == null) {
      throw new IllegalArgumentException("week cannot be null.");
    }

    if (week.getBegin().getMonth() != begin.getMonth()) {
      throw new IllegalDateException("Begin of week is not in month");
    }
    if (week.getEnd().getMonth() != end.getMonth()) {
      throw new IllegalDateException("End of week is not in month");
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
      throw new IllegalDateException("week is within another week.");
    }

    weeks.add(week);
    weeks.sort(null);
  }

  public LocalDate getBegin() {
    LOGGER.trace("Called getBegin()");
    return begin;
  }

  @Override
  public List<? extends TreeElement> getChildren() {
    return Collections.emptyList();
  }

  public LocalDate getEnd() {
    LOGGER.trace("Called getEnd()");
    return end;
  }

  @Override
  public String getTreeLabel() {
    return "Monat vom " + Utils.formatDate(begin) + " bis " + Utils.formatDate(end);
  }

  public List<DataWeek> getWeeks() {
    LOGGER.trace("Called getWeeks()");
    return weeks;
  }

  /**
   * Removes a week to the month.
   *
   * @param week week to remove, may not be null
   */
  public void removeWeek(DataWeek week) {
    LOGGER.trace("Called removeWeek(week: {})", week);
    if (week == null) {
      throw new NullPointerException("week cannot be null.");
    }

    weeks.remove(week);
  }

  /**
   * Sets the begin of the month.
   *
   * @param begin begin to set, may not be null
   * @throws IllegalDateException if begin is not a working day or is after end of month
   */
  public void setBegin(LocalDate begin) throws IllegalDateException {
    LOGGER.trace("Called setBegin(begin: {})", begin);
    if (begin == null) {
      throw new NullPointerException("begin cannot be null.");
    }
    if (!DateUtils.checkWorkday(begin)) {
      throw new IllegalDateException("begin must be a workday");
    }
    if (end != null && begin.isAfter(end)) {
      throw new IllegalDateException("begin may not be after end of month.");
    }

    this.begin = begin;
  }

  /**
   * Sets the end of the month.
   *
   * @param end end to set, may not be null
   * @throws IllegalDateException if end is not a working day or is before begin of month
   */
  public void setEnd(LocalDate end) throws IllegalDateException {
    LOGGER.trace("Called setEnd(end: {})", end);
    if (end == null) {
      throw new NullPointerException("end cannot be null.");
    }
    if (!DateUtils.checkWorkday(end)) {
      throw new IllegalDateException("end must be a workday");
    }
    if (begin != null && begin.isAfter(end)) {
      throw new IllegalDateException("end may not be before begin of month.");
    }

    this.end = end;
  }

  /**
   * Sets the begin of the month.
   *
   * @param weeks weeks to set, may not be null
   * @throws IllegalDateException if a week is within another or begins or ends outside the month
   */
  public void setWeeks(List<DataWeek> weeks) throws IllegalDateException {
    LOGGER.trace("Called setWeeks(weeks: {})", weeks);
    if (weeks == null) {
      throw new NullPointerException("weeks may not be null");
    }

    for (final DataWeek week : weeks) {
      addWeek(week);
    }
  }

  @Override
  public String toString() {
    return getTreeLabel();
  }
}
