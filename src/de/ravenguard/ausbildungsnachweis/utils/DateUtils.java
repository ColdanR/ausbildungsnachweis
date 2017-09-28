package de.ravenguard.ausbildungsnachweis.utils;

import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtils {
  /**
   * Validates a date as work day.
   *
   * @param date date to validate
   */
  public static void checkDate(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("Begin or end may not be null.");
    }
    final Configuration configuration = Configuration.getInstance();
    if (date.getDayOfWeek() == DayOfWeek.SATURDAY && !configuration.isSaturdayWorkday()) {
      throw new IllegalArgumentException(
          "Begin or end is a saturday and you are not working at saturdays.");
    }
    if (date.getDayOfWeek() == DayOfWeek.SUNDAY && !configuration.isSundayWorkday()) {
      throw new IllegalArgumentException(
          "Begin or end is a sunday and you are not working at sundays.");
    }
    if (configuration.isHoliday(date) && !configuration.isSundayWorkday()) {
      throw new IllegalArgumentException(
          "Begin or end is a legal holiday and you are not working at holidays.");
    }
  }

  /**
   * Checks both dates for work day and for chronology.
   *
   * @param begin begin, must be a work day, may not be after end
   * @param end end, must be a work day, may not be before begin
   */
  public static void checkDate(LocalDate begin, LocalDate end) {
    if (begin != null) {
      checkDate(begin);
    }
    if (end != null) {
      checkDate(end);
    }
    if (begin != null && end != null && begin.isAfter(end)) {
      throw new IllegalArgumentException("begin may not be after end");
    }
  }
}
