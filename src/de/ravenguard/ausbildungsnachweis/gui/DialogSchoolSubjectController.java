package de.ravenguard.ausbildungsnachweis.gui;

import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogSchoolSubjectController {
  private static final Logger LOGGER = LogManager.getLogger(DialogSchoolSubjectController.class);
  @FXML
  private TextField label;
  @FXML
  private DatePicker exemptSince;

  public LocalDate getExemptSince() {
    LOGGER.trace("Called getExemptSince()");
    return exemptSince.getValue();
  }

  public String getLabel() {
    LOGGER.trace("Called getLabel()");
    return label.getText();
  }

  @FXML
  public void onDeleteExemptSince(ActionEvent event) {
    LOGGER.trace("Called onDeleteExemptSince(event: {})", event);
    exemptSince.setValue(null);
  }

  public void setExemptSince(LocalDate exemptSince) {
    LOGGER.trace("Called setExemptSince(exemptSince: {})", exemptSince);
    this.exemptSince.setValue(exemptSince);
  }

  public void setLabel(String label) {
    LOGGER.trace("Called setLabel(label: {})", label);
    this.label.setText(label);
  }
}
