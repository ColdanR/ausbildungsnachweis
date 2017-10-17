package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.model.DataMonth;
import de.ravenguard.ausbildungsnachweis.model.DataWeek;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import de.ravenguard.ausbildungsnachweis.utils.GuiLoader;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContentDataMonthController {
  private static final Logger LOGGER = LogManager.getLogger(ContentDataMonthController.class);
  private DataMonth dataMonth;

  @FXML
  private Label headerMonth;
  @FXML
  private Label headerBeginEnd;
  @FXML
  private Label headerSchool;
  @FXML
  private VBox company;
  @FXML
  private VBox school;
  @FXML
  private Button closeSchool;

  /**
   * Returns the dataMonth instance.
   *
   * @return the dataMonth
   */
  public DataMonth getDataMonth() {
    LOGGER.trace("Called getDataMonth()");
    return dataMonth;
  }

  @FXML
  public void onPdfExport(ActionEvent event) {
    LOGGER.trace("Called onPdfExport(event: {})", event);
  }

  /**
   * Sets the dataMonth instance.
   *
   * @param dataMonth the dataMonth to set
   */
  public void setData(DataMonth dataMonth, TrainingPeriod period, Stage stage) {
    LOGGER.trace("Called setData(dataMonth: {}, period: {}, stage: {})", dataMonth, period, stage);
    this.dataMonth = dataMonth;

    // Set headers
    headerMonth.setText("Monat " + Utils.formatFullNameDate(dataMonth.getBegin()));
    headerBeginEnd.setText("Anfang: " + Utils.formatDate(dataMonth.getBegin()) + " - Ende: "
        + Utils.formatDate(dataMonth.getEnd()));

    // Set Weeks
    for (final DataWeek week : dataMonth.getWeeks()) {
      try {
        final GuiLoader<ContentDataWeekController, VBox> loader =
            new GuiLoader<>("ContentDataWeek.fxml");
        final VBox content = loader.getRoot();
        company.getChildren().add(content);
        final ContentDataWeekController controller = loader.getController();
        controller.setData(week, period, stage, headerSchool, school);
      } catch (final IOException e) {
        Utils.createExceptionAlert(e);
      }
    }
    closeSchool.setOnAction((event) -> {
      school.getChildren().clear();
    });
  }
}
