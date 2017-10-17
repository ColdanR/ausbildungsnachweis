package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.model.ContentSchoolSubject;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContentSchoolController {
  private static final Logger LOGGER = LogManager.getLogger(ContentSchoolController.class);
  @FXML
  private TextArea textArea;
  @FXML
  private Label subject;
  @FXML
  private Label exempt;

  /**
   * Sets the data for the controller.
   *
   * @param schoolSubject school subject
   * @param weekBegin weekBegin
   * @param weekEnd weekEnd
   */
  public void setData(ContentSchoolSubject schoolSubject, LocalDate weekBegin, LocalDate weekEnd) {
    LOGGER.trace("Called setData(schoolSubject: {}, weekBegin: {}, weekEnd: {})", schoolSubject,
        weekBegin, weekEnd);
    final LocalDate exemptSince = schoolSubject.getSubject().getExemptSince();

    // Init textArea
    textArea.textProperty().addListener((observable, oldValue, newValue) -> {
      schoolSubject.setContent(newValue);
    });
    textArea.setText(schoolSubject.getContent());

    // Set subject
    subject.setText(schoolSubject.getSubject().getLabel());

    if (exemptSince != null) {
      if (exemptSince.isBefore(weekBegin)) {
        // completely exempted
        textArea.visibleProperty().set(false);
        textArea.managedProperty().set(false);
        exempt.setText("Befreit seit " + Utils.formatDate(exemptSince));
      } else {
        if (exemptSince.isBefore(weekEnd)) {
          // Just in this week
          exempt.setText("Befreit seit " + Utils.formatDate(exemptSince));
        } else {
          exempt.visibleProperty().set(false);
          exempt.managedProperty().set(false);
        }
      }
    } else {
      // No exempt
      exempt.visibleProperty().set(false);
      exempt.managedProperty().set(false);
    }
  }
}
