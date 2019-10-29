package de.ravenguard.ausbildungsnachweis.utils;

import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtils {

  /**
   * Checks if the {@link LocalDate} is a working day or calculates the next
   * working day after the LocalDate.
   *
   * @param date {@link LocalDate} to check and start calculation
   * @return {@link LocalDate} representing the working day equal or after date
   */
  public static LocalDate calculateWorkdayAfter(LocalDate date) {
    if (date == null) {
      throw new NullPointerException("date cannot be null");
    }
    while (!checkWorkday(date)) {
      date = date.plusDays(1);
    }
    return date;
  }

  /**
   * Checks if the {@link LocalDate} is a working day or calculates the last
   * working day before the LocalDate.
   *
   * @param date {@link LocalDate} to check and start calculation
   * @return {@link LocalDate} representing the working day equal or before date
   */
  public static LocalDate calculateWorkdayBefore(LocalDate date) {
    if (date == null) {
      throw new NullPointerException("date cannot be null");
    }
    while (!checkWorkday(date)) {
      date = date.minusDays(1);
    }
    return date;
  }

  /**
   * Validates a date as work day.
   *
   * @param date date to validate
   * @return true if the date is a workday
   * @throws NullPointerException if date is null
   */
  public static boolean checkWorkday(LocalDate date) {
    if (date == null) {
      throw new NullPointerException("date may not be null");
    }
    final Configuration configuration = Configuration.getInstance();
    return !(date.getDayOfWeek() == DayOfWeek.SATURDAY && !configuration.isSaturdayWorkday()
            || date.getDayOfWeek() == DayOfWeek.SUNDAY && !configuration.isSundayWorkday());
  }

  private DateUtils() {
    // prevent instances
  }
}
