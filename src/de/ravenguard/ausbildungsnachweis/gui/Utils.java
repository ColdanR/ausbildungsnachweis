package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.gui.elements.AlertException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {
  private static final Logger LOGGER = LogManager.getLogger(DialogTraineeController.class);
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault());

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
    final Alert alert = new Alert(AlertType.ERROR, error, ButtonType.OK);
    alert.showAndWait();
  }

  public static void createExceptionAlert(Throwable t) {
    LOGGER.trace("Called createExceptionAlert(t: {})", t);
    new AlertException(t).showAndWait();
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
}
