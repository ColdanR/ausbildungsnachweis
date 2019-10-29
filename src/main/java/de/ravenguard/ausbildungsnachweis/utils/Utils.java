package de.ravenguard.ausbildungsnachweis.utils;

import de.ravenguard.ausbildungsnachweis.gui.elements.AlertException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {

  private static final Logger LOGGER = LogManager.getLogger(Utils.class);
  private static final DateTimeFormatter FORMATTER
          = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault());
  private static final DateTimeFormatter MONTH_FORMATTER
          = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());

  /**
   * Creates an Alert for errors and displays it.
   *
   * @param errors List of errors.
   */
  public static void createErrorMessage(List<String> errors) {
    LOGGER.trace("Called createErrorMessage(errors: {})", errors);
    final String error = errors.stream().collect(Collectors.joining(System.lineSeparator()));
    createErrorMessage(error);
  }

  /**
   * Creates an Alert for error message and displays it.
   *
   * @param error error message
   */
  public static void createErrorMessage(String error) {
    LOGGER.trace("Called createErrorMessage(error: {})", error);
    new Alert(AlertType.ERROR, error, ButtonType.OK).showAndWait();
  }

  public static void createExceptionAlert(Throwable t) {
    LOGGER.error("Called createExceptionAlert(t: {})", t);
    new AlertException(t).showAndWait();
  }

  /**
   * Creates an Alert for info message and displays it.
   *
   * @param message info message
   */
  public static void createInfoMessage(String message) {
    LOGGER.info("Called createInfoMessage(message: {})", message);
    new Alert(AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
  }

  /**
   * Formats the date to local display.
   *
   * @param date date to format
   * @return formated date as String
   */
  public static String formatDate(LocalDate date) {
    LOGGER.trace("Called formatDate(date: {})", date);
    if (date == null) {
      return "";
    }
    return FORMATTER.format(date);
  }

  /**
   * Formats the date to display name of month and the year.
   *
   * @param date date to format
   * @return formated date as string
   */
  public static String formatFullNameDate(LocalDate date) {
    if (date == null) {
      return "";
    }
    return MONTH_FORMATTER.format(date);
  }

  public static int getWeekNumberFromDate(LocalDate date) {
    return date.get(WeekFields.ISO.weekOfWeekBasedYear());
  }

  private Utils() {
    // prevent instances
  }
}
