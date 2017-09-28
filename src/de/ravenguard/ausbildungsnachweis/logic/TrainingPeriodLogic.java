package de.ravenguard.ausbildungsnachweis.logic;

import de.ravenguard.ausbildungsnachweis.model.DataMonth;
import de.ravenguard.ausbildungsnachweis.model.DataWeek;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import de.ravenguard.ausbildungsnachweis.utils.DateUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

public class TrainingPeriodLogic {
  private LocalDate calculateMonthBack(LocalDate date) {
    final Configuration config = Configuration.getInstance();
    final Month month = date.getMonth();
    while (date.getDayOfWeek() == DayOfWeek.SUNDAY && !config.isSundayWorkday()
        || date.getDayOfWeek() == DayOfWeek.SATURDAY && !config.isSaturdayWorkday()
        || config.isHoliday(date) && !config.isSundayWorkday()) {
      date.minusDays(1);
    }
    if (date.getMonth() == month) {
      return date;
    } else {
      return null;
    }
  }

  private LocalDate calculateMonthForward(LocalDate date) {
    final Configuration config = Configuration.getInstance();
    final Month month = date.getMonth();
    while (date.getDayOfWeek() == DayOfWeek.SUNDAY && !config.isSundayWorkday()
        || date.getDayOfWeek() == DayOfWeek.SATURDAY && !config.isSaturdayWorkday()
        || config.isHoliday(date) && !config.isSundayWorkday()) {
      date.plusDays(1);
    }
    if (date.getMonth() == month) {
      return date;
    } else {
      return null;
    }
  }

  private List<DataMonth> calculateMonths(LocalDate begin, LocalDate end) {
    DateUtils.checkDate(begin);
    DateUtils.checkDate(end);
    if (end.isBefore(begin)) {
      throw new IllegalArgumentException("begin may not be after end");
    }

    final List<DataMonth> retValue = new ArrayList<>();
    LocalDate tempBegin = begin.plusDays(0);
    while (tempBegin.getMonth() != end.getMonth()) {
      // Calculate last working day of month
      final LocalDate tempEnd =
          calculateMonthBack(tempBegin.withDayOfMonth(tempBegin.lengthOfMonth()));
      if (tempEnd != null) {
        // Add to list
        retValue.add(new DataMonth(tempBegin, tempEnd, calculateWeeks(tempBegin, tempEnd)));
        // Set begin to next month
        tempBegin = calculateMonthForward(
            LocalDate.of(tempBegin.getYear(), tempBegin.getMonthValue() + 1, 1));
      }
      if (tempEnd == null || tempBegin == null || tempBegin.isAfter(tempEnd)) {
        break;
      }
    }
    if (tempBegin != null) {
      final LocalDate tempEnd = calculateMonthBack(end);
      if (tempEnd != null && !tempBegin.isAfter(tempEnd)) {
        // Add last month
        retValue.add(new DataMonth(tempBegin, tempEnd, calculateWeeks(tempBegin, tempEnd)));
      }
    }

    return retValue;
  }

  private LocalDate calculateWeekBack(LocalDate date) {
    final Configuration config = Configuration.getInstance();
    final int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
    while (date.getDayOfWeek() == DayOfWeek.SUNDAY && !config.isSundayWorkday()
        || date.getDayOfWeek() == DayOfWeek.SATURDAY && !config.isSaturdayWorkday()
        || config.isHoliday(date) && !config.isSundayWorkday()) {
      date.minusDays(1);
    }
    if (date.get(WeekFields.ISO.weekOfWeekBasedYear()) != weekNumber) {
      return null;
    } else {
      return date;
    }
  }

  private LocalDate calculateWeekForward(LocalDate date) {
    final Configuration config = Configuration.getInstance();
    final int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
    while (date.getDayOfWeek() == DayOfWeek.SUNDAY && !config.isSundayWorkday()
        || date.getDayOfWeek() == DayOfWeek.SATURDAY && !config.isSaturdayWorkday()
        || config.isHoliday(date) && !config.isSundayWorkday()) {
      date.plusDays(1);
    }
    if (date.get(WeekFields.ISO.weekOfWeekBasedYear()) != weekNumber) {
      return null;
    } else {
      return date;
    }
  }

  private List<DataWeek> calculateWeeks(LocalDate begin, LocalDate end) {
    DateUtils.checkDate(begin);
    DateUtils.checkDate(end);
    if (end.isBefore(begin)) {
      throw new IllegalArgumentException("begin may not be after end");
    }

    final List<DataWeek> retValue = new ArrayList<>();
    LocalDate tempBegin = begin.plusDays(0);
    while (tempBegin.get(WeekFields.ISO.weekOfWeekBasedYear()) != end
        .get(WeekFields.ISO.weekOfWeekBasedYear())) {
      // Calculate last working day of month
      final LocalDate tempEnd = calculateWeekBack(tempBegin.with(DayOfWeek.SUNDAY));
      if (tempEnd != null) {
        // Add to list
        retValue.add(new DataWeek(tempBegin, tempEnd));
        // Set begin to next month
        tempBegin = calculateWeekForward(tempBegin.plus(1, ChronoUnit.WEEKS));
      }
      if (tempEnd == null || tempBegin == null || tempBegin.isAfter(tempEnd)) {
        break;
      }
    }
    if (tempBegin != null) {
      final LocalDate tempEnd = calculateWeekBack(end);
      if (tempEnd != null && !tempBegin.isAfter(tempEnd)) {
        // Add last month
        retValue.add(new DataWeek(tempBegin, tempEnd));
      }
    }

    return retValue;
  }

  /**
   * Creates a new instance of {@link TrainingPeriod}.
   *
   * @param label label, may not be null or empty
   * @param begin begin, may not be null
   * @param end end, may not be null
   * @param schoolClass schoolClass, may not be null or empty
   * @param classTeacher classTeacher, may not be null or empty
   * @return new {@link TrainingPeriod} with all months and weeks generated
   */
  public TrainingPeriod create(String label, LocalDate begin, LocalDate end, String schoolClass,
      String classTeacher) {

    return new TrainingPeriod(label, begin, end, schoolClass, classTeacher,
        calculateMonths(begin, end), new ArrayList<>());
  }
}
