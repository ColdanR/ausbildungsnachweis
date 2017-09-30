package de.ravenguard.ausbildungsnachweis.logic;

import de.ravenguard.ausbildungsnachweis.model.ContentSchoolSubject;
import de.ravenguard.ausbildungsnachweis.model.DataMonth;
import de.ravenguard.ausbildungsnachweis.model.DataWeek;
import de.ravenguard.ausbildungsnachweis.model.IllegalDateException;
import de.ravenguard.ausbildungsnachweis.model.SchoolSubject;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import de.ravenguard.ausbildungsnachweis.model.WeekType;
import de.ravenguard.ausbildungsnachweis.utils.DateUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrainingPeriodLogic {
  private static final Logger LOGGER = LogManager.getLogger(TrainingPeriodLogic.class);

  /**
   * Add a subject to the training period.
   *
   * @param period period to edit
   * @param subject subject to add
   */
  public void addSchoolSubject(TrainingPeriod period, SchoolSubject subject) {
    if (period == null) {
      throw new NullPointerException("period cannot be null");
    }
    if (subject == null) {
      throw new NullPointerException("subject cannot be null");
    }
    for (final DataMonth month : period.getMonths()) {
      for (final DataWeek week : month.getWeeks()) {
        if (week.getType() == WeekType.SCHOOL) {
          final ContentSchoolSubject content = new ContentSchoolSubject();
          content.setSubject(subject);
          week.getContentSchool().add(content);
        }
      }
    }
  }

  private List<DataMonth> calculateMonths(LocalDate begin, LocalDate end)
      throws IllegalDateException {
    LOGGER.trace("Called calculateMonths(begin: {}, end: {})", begin, end);
    if (!DateUtils.checkWorkday(begin)) {
      throw new IllegalDateException("begin must be a working day");
    }
    if (!DateUtils.checkWorkday(end)) {
      throw new IllegalDateException("end must be a working day");
    }
    if (end.isBefore(begin)) {
      throw new IllegalDateException("begin may not be after end");
    }

    final List<DataMonth> retValue = new ArrayList<>();
    LocalDate tempBegin = begin.plusDays(0);
    while (tempBegin.getMonth() != end.getMonth()) {
      // Calculate last working day of month
      LocalDate tempEnd = tempBegin.withDayOfMonth(tempBegin.lengthOfMonth());
      tempEnd = DateUtils.calculateWorkdayBefore(tempEnd);
      if (tempEnd != null) {
        // Add to list
        retValue.add(new DataMonth(tempBegin, tempEnd, calculateWeeks(tempBegin, tempEnd)));
        // Set begin to next month
        if (tempBegin.getMonthValue() != 12) {
          tempBegin = LocalDate.of(tempBegin.getYear(), tempBegin.getMonthValue() + 1, 1);
        } else {
          tempBegin = LocalDate.of(tempBegin.getYear() + 1, 1, 1);
        }
        tempBegin = DateUtils.calculateWorkdayAfter(tempBegin);
      }
      if (tempEnd == null || tempBegin == null) {
        break;
      }
    }
    if (tempBegin != null) {
      final LocalDate tempEnd = DateUtils.calculateWorkdayBefore(end);
      if (tempEnd != null && !tempBegin.isAfter(tempEnd)) {
        // Add last month
        retValue.add(new DataMonth(tempBegin, tempEnd, calculateWeeks(tempBegin, tempEnd)));
      }
    }

    return retValue;
  }

  private LocalDate calculateWeekAfter(LocalDate date, Month currentMonth) {
    LOGGER.trace("Called calculateWeekForward(date: {}, currentMonth: {})", date, currentMonth);
    final LocalDate startDate = date.plusDays(0);
    final int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
    date = DateUtils.calculateWorkdayAfter(date);
    if (date.get(WeekFields.ISO.weekOfWeekBasedYear()) != weekNumber) {
      return null;
    } else {
      LOGGER.trace("Return calculateWeekForward(date: {}, currentMonth: {}): {}", startDate,
          currentMonth, date);
      return date;
    }
  }

  private LocalDate calculateWeekBefore(LocalDate date, Month currentMonth) {
    LOGGER.trace("Called calculateWeekBack(date: {}, currentMonth: {})", date, currentMonth);
    final LocalDate startDate = date.plusDays(0);
    final int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
    date = DateUtils.calculateWorkdayBefore(date);
    if (date.get(WeekFields.ISO.weekOfWeekBasedYear()) != weekNumber) {
      return null;
    } else {
      LOGGER.trace("Return calculateWeekBack(date: {}, currentMonth: {}): {}", startDate,
          currentMonth, date);
      return date;
    }
  }

  private List<DataWeek> calculateWeeks(LocalDate begin, LocalDate end)
      throws IllegalDateException {
    LOGGER.trace("Called calculateWeeks(begin: {}, end: {})", begin, end);
    DateUtils.checkWorkday(begin);
    DateUtils.checkWorkday(end);
    if (end.isBefore(begin)) {
      throw new IllegalArgumentException("begin may not be after end");
    }
    if (begin.getMonth() != end.getMonth()) {
      throw new IllegalArgumentException("begin and end are not in the same month");
    }

    final Month currentMonth = begin.getMonth();
    final List<DataWeek> retValue = new ArrayList<>();
    LocalDate tempBegin = begin.plusDays(0);
    while (tempBegin.get(WeekFields.ISO.weekOfWeekBasedYear()) != end
        .get(WeekFields.ISO.weekOfWeekBasedYear())) {
      // Calculate last working day of month
      final LocalDate tempEnd = calculateWeekBefore(tempBegin.with(DayOfWeek.SUNDAY), currentMonth);
      if (tempEnd != null) {
        // Add to list
        retValue.add(new DataWeek(tempBegin, tempEnd));
      }
      // Set begin to next week
      tempBegin = tempBegin.plus(1, ChronoUnit.WEEKS).with(WeekFields.ISO.dayOfWeek(), 1);
      tempBegin = calculateWeekAfter(tempBegin, currentMonth);
      if (tempEnd == null || tempBegin == null) {
        break;
      }
    }
    if (tempBegin != null) {
      final LocalDate tempEnd = calculateWeekBefore(end, currentMonth);
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
   * @throws IllegalDateException if either begin or end is not a working day or end is before begin
   */
  public TrainingPeriod create(String label, LocalDate begin, LocalDate end, String schoolClass,
      String classTeacher) throws IllegalDateException {
    LOGGER.trace("Called create(label: {}, begin: {}, end: {}, schoolClass: {}, classTeacher: {})",
        label, begin, end, schoolClass, classTeacher);
    return new TrainingPeriod(label, begin, end, schoolClass, classTeacher,
        calculateMonths(begin, end), new ArrayList<>());
  }

  /**
   * Perform the switch of the {@link WeekType}.
   *
   * @param period {@link TrainingPeriod} of the week
   * @param week {@link DataWeek} to switch {@link WeekType}
   * @param newType new {@link WeekType}
   */
  public void switchWeekType(TrainingPeriod period, DataWeek week, WeekType newType) {
    if (period == null) {
      throw new NullPointerException("period cannot be null");
    }
    if (week == null) {
      throw new NullPointerException("week cannot be null");
    }
    if (newType == null) {
      throw new NullPointerException("newType cannot be null");
    }
    boolean found = false;
    for (final DataMonth month : period.getMonths()) {
      if (month.getWeeks().contains(week)) {
        found = true;
        break;
      }
    }
    if (!found) {
      throw new IllegalArgumentException("Week not found.");
    }
    week.setType(newType);
    if (newType == WeekType.SCHOOL) {
      for (final SchoolSubject subject : period.getSchoolSubjects()) {
        final ContentSchoolSubject content = new ContentSchoolSubject();
        content.setSubject(subject);
        week.getContentSchool().add(content);
      }
      if (!Configuration.getInstance().isCompanyAndSchool()) {
        week.setContentCompany("");
      }
    } else {
      week.getContentSchool().clear();
    }
  }
}
