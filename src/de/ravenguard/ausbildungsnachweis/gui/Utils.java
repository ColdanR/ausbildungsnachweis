package de.ravenguard.ausbildungsnachweis.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Utils {
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault());

  public static void createErrorMessage(String error) {
    final Alert alert = new Alert(AlertType.ERROR, error, ButtonType.OK);
    alert.showAndWait();
  }

  /**
   * Formats the date to local display.
   * 
   * @param date date to format
   * @return formated date as String
   */
  public static String formatDate(LocalDate date) {
    if (date == null) {
      return "";
    }
    return FORMATTER.format(date);
  }
}
