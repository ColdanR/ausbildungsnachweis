package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import de.ravenguard.ausbildungsnachweis.logic.TrainingPeriodLogic;
import de.ravenguard.ausbildungsnachweis.model.DataWeek;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import de.ravenguard.ausbildungsnachweis.model.WeekType;
import de.ravenguard.ausbildungsnachweis.utils.GuiLoader;
import de.ravenguard.ausbildungsnachweis.utils.Utils;
import java.io.IOException;
import java.time.temporal.WeekFields;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContentDataWeekController {

  private static final Logger LOGGER = LogManager.getLogger(ContentDataWeekController.class);
  @FXML
  private Label header;
  @FXML
  private RadioButton button;
  @FXML
  private TextArea text;
  @FXML
  private Button setSchoolContent;

  /**
   * Sets and initialize the dataWeek.
   *
   * @param dataWeek dataWeek to set
   * @param period parent period
   * @param stage parent stage
   * @param headerSchool header for school
   * @param schoolContent content for school
   */
  public void setData(DataWeek dataWeek, TrainingPeriod period, Stage stage, Label headerSchool,
          VBox schoolContent) {
    LOGGER.trace("Called setData(dataWeek: {})", dataWeek);

    header.setText("KW " + dataWeek.getBegin().get(WeekFields.ISO.weekOfWeekBasedYear())
            + " - Beginn: " + Utils.formatDate(dataWeek.getBegin()) + " - Ende: "
            + Utils.formatDate(dataWeek.getEnd()));

    button.selectedProperty().set(dataWeek.getType() == WeekType.SCHOOL);
    button.selectedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) -> {
      LOGGER.trace("Called change(obs: {}, oldValue: {}, newValue: {})", obs, oldValue, newValue);

      // Determine new type
      WeekType type = WeekType.COMPANY;
      if (Boolean.TRUE.equals(newValue)) {
        type = WeekType.SCHOOL;
      }

      // Do logic
      final TrainingPeriodLogic logic = new TrainingPeriodLogic();
      logic.switchWeekType(period, dataWeek, type);
    });

    // Set content TextField
    text.setText(dataWeek.getContentCompany());
    text.textProperty().addListener((obs, oldValue, newValue) -> dataWeek.setContentCompany(newValue));

    // Bind visibility TextField
    if (!Configuration.getInstance().isCompanyAndSchool()) {
      text.visibleProperty().bind(button.selectedProperty().not());
      text.managedProperty().bind(button.selectedProperty().not());
    }

    // Bind visibility SchoolContentButton
    setSchoolContent.visibleProperty().bind(button.selectedProperty());
    setSchoolContent.managedProperty().bind(button.selectedProperty());

    // Button Action
    setSchoolContent.setOnAction(event -> {
      final int weekNumber = dataWeek.getBegin().get(WeekFields.ISO.weekOfWeekBasedYear());
      headerSchool.setText("Schulwoche: KW " + weekNumber + ": Vom "
              + Utils.formatDate(dataWeek.getBegin()) + " bis " + Utils.formatDate(dataWeek.getEnd()));
      dataWeek.getContentSchool().forEach(content -> {
        try {
          final GuiLoader<ContentSchoolController, VBox> loader
                  = new GuiLoader<>("ContentSchool.fxml");

          // Set Node
          final Parent node = loader.getRoot();
          schoolContent.getChildren().add(node);
          // set Data
          final ContentSchoolController controller = loader.getController();
          controller.setData(content, dataWeek.getBegin(), dataWeek.getEnd());
        } catch (final IOException e) {
          Utils.createExceptionAlert(e);
        }
      });
    });
  }
}
