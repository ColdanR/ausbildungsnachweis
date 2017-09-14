package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.logic.Install;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class DataMonth {
  private LocalDate begin;
  private List<DataWeek> weeks;
  private String notes;

  /**
   * Fields Constructor.
   *
   * @param begin begin of the month, may not be null
   * @param weeks list of weeks, may not be null
   * @param notes notes for the month, may not be null
   */
  public DataMonth(LocalDate begin, List<DataWeek> weeks, String notes) {
    super();
    if (begin == null) {
      throw new IllegalArgumentException("begin may not be null");
    }
    if (weeks == null) {
      throw new IllegalArgumentException("weeks may not be null");
    }
    if (notes == null) {
      throw new IllegalArgumentException("notes may not be null");
    }
    this.begin = begin;
    this.weeks = weeks;
    this.notes = notes.trim();
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

    final LocalDate begin = week.getBegin();
    if (begin.getMonth() != this.begin.getMonth()) {
      throw new IllegalArgumentException("Week is not in month");
    }

    if (begin.getDayOfWeek() == DayOfWeek.SUNDAY
        || begin.getDayOfWeek() == DayOfWeek.SATURDAY && !Install.getInstance().isSaturdayWorkday()
        || Install.getInstance().isHoliday(begin)) {
      throw new IllegalArgumentException("Begin of the week is not a work day");
    }
  }

  public LocalDate getBegin() {
    return begin;
  }

  public String getNotes() {
    return notes;
  }

  public List<DataWeek> getWeeks() {
    return weeks;
  }

  /**
   * Sets the begin of the month.
   *
   * @param begin begin to set, may not be null
   */
  public void setBegin(LocalDate begin) {
    if (begin == null) {
      throw new IllegalArgumentException("begin may not be null");
    }

    this.begin = begin;
  }

  /**
   * Sets the begin of the month.
   *
   * @param notes notes to set, may not be null
   */
  public void setNotes(String notes) {
    if (notes == null) {
      throw new IllegalArgumentException("notes may not be null");
    }

    this.notes = notes.trim();
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

    this.weeks = weeks;
  }
}
