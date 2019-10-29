package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import de.ravenguard.ausbildungsnachweis.model.ContentSchoolSubject;
import de.ravenguard.ausbildungsnachweis.model.DataMonth;
import de.ravenguard.ausbildungsnachweis.model.DataWeek;
import de.ravenguard.ausbildungsnachweis.model.Trainee;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import de.ravenguard.ausbildungsnachweis.model.WeekType;
import de.ravenguard.ausbildungsnachweis.utils.GuiLoader;
import de.ravenguard.ausbildungsnachweis.utils.JrExport;
import de.ravenguard.ausbildungsnachweis.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContentDataMonthController {

  private static final Logger LOGGER = LogManager.getLogger(ContentDataMonthController.class);
  private DataMonth dataMonth;
  private Stage stage;
  private String name;
  private String trainingsyear;
  private String profession;

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

  /**
   * Starts the export of the current month.
   *
   * @param event the event
   */
  @FXML
  public void onPdfExport(ActionEvent event) {
    LOGGER.trace("Called onPdfExport(event: {})", event);
    if (!validateContent()) {
      return;
    }
    final FileChooser chooser = new FileChooser();
    chooser.setTitle("Wähle den Speicherort für den Export");
    chooser.getExtensionFilters().add(new ExtensionFilter("Portable Document Format", "*.pdf"));
    chooser.setInitialDirectory(Configuration.getInstance().getInstallPath().toFile());
    chooser.setInitialFileName(Utils.formatFullNameDate(dataMonth.getBegin()));
    final File exportFile = chooser.showSaveDialog(stage);

    if (exportFile != null) {
      try {
        final String period
                = Utils.formatDate(dataMonth.getBegin()) + " - " + Utils.formatDate(dataMonth.getEnd());
        JrExport.export(dataMonth.getWeeks(), exportFile, name, profession, trainingsyear, period);
        Utils.createInfoMessage("Export abgeschlossen");
      } catch (final IOException | JRException e) {
        Utils.createExceptionAlert(e);
      }
    }
  }

  /**
   * Sets the dataMonth instance.
   *
   * @param dataMonth the dataMonth to set
   * @param period parent periode
   * @param stage java fx stage
   * @param trainee trainee to display
   */
  public void setData(DataMonth dataMonth, TrainingPeriod period, Stage stage, Trainee trainee) {
    LOGGER.trace("Called setData(dataMonth: {}, period: {}, stage: {}, trainee: {})", dataMonth,
            period, stage, trainee);
    this.dataMonth = dataMonth;
    this.stage = stage;
    profession = trainee.getTraining();
    name = trainee.getGivenNames() + " " + trainee.getFamilyName();
    trainingsyear = period.getLabel();

    // Set headers
    headerMonth.setText("Monat " + Utils.formatFullNameDate(dataMonth.getBegin()));
    headerBeginEnd.setText("Anfang: " + Utils.formatDate(dataMonth.getBegin()) + " - Ende: "
            + Utils.formatDate(dataMonth.getEnd()));

    // Set Weeks
    for (final DataWeek week : dataMonth.getWeeks()) {
      try {
        final GuiLoader<ContentDataWeekController, VBox> loader
                = new GuiLoader<>("ContentDataWeek.fxml");
        final VBox content = loader.getRoot();
        company.getChildren().add(content);
        final ContentDataWeekController controller = loader.getController();
        controller.setData(week, period, stage, headerSchool, school);
      } catch (final IOException e) {
        Utils.createExceptionAlert(e);
      }
    }
    closeSchool.setOnAction(event -> {
      school.getChildren().clear();
      headerSchool.setText("");
    });
  }

  private boolean validateContent() {
    final List<String> warnings = new ArrayList<>();
    for (final DataWeek week : dataMonth.getWeeks()) {
      if (week.getType() == WeekType.COMPANY
              && (week.getContentCompany() == null || week.getContentCompany().trim().length() == 0)) {
        warnings.add("Woche " + week.getBegin().get(WeekFields.ISO.weekOfWeekBasedYear())
                + " hat keinen Inhalt für Betrieb.");
      } else {
        if (Configuration.getInstance().isCompanyAndSchool() && (week.getContentCompany() == null
                || week.getContentCompany().trim().length() == 0)) {
          warnings.add("Woche " + week.getBegin().get(WeekFields.ISO.weekOfWeekBasedYear())
                  + " hat keinen Inhalt für Betrieb.");
        }
        for (final ContentSchoolSubject subject : week.getContentSchool()) {
          if (subject.getSubject().getExemptSince() != null
                  && !subject.getSubject().getExemptSince().isBefore(week.getBegin())
                  && (subject.getContent() == null || subject.getContent().trim().length() == 0)) {
            warnings.add("Schulfach " + subject.getSubject().getLabel() + " in KW "
                    + week.getBegin().get(WeekFields.ISO.weekOfWeekBasedYear())
                    + " hat keinen Inhalt.");
          }
        }
      }
    }
    if (warnings.isEmpty()) {
      return true;
    } else {
      final String warnung = "Es liegen Fehler vor:" + System.lineSeparator()
              + warnings.stream().collect(Collectors.joining(System.lineSeparator()));
      final Optional<ButtonType> result
              = new Alert(AlertType.CONFIRMATION, warnung, ButtonType.APPLY, ButtonType.CANCEL)
                      .showAndWait();
      if (result.isPresent()) {
        return result.get() == ButtonType.APPLY;
      } else {
        return false;
      }
    }
  }
}
