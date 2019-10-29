package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import de.ravenguard.ausbildungsnachweis.model.SchoolSubject;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import de.ravenguard.ausbildungsnachweis.utils.GuiLoader;
import de.ravenguard.ausbildungsnachweis.utils.Utils;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContentTrainingPeriodController {

  private static final Logger LOGGER = LogManager.getLogger(ContentTrainingPeriodController.class);

  private TrainingPeriod trainingPeriod;
  private Stage stage;

  @FXML
  private TextField label;
  @FXML
  private TextField schoolClass;
  @FXML
  private TextField classTeacher;
  @FXML
  private Label begin;
  @FXML
  private Label end;
  @FXML
  private ListView<SchoolSubject> subjects;

  public Stage getStage() {
    return stage;
  }

  /**
   * Returns the current TrainingPeriod.
   *
   * @return the trainingPeriod
   */
  public TrainingPeriod getTrainingPeriod() {
    LOGGER.trace("Called getTrainingPeriod()");
    return trainingPeriod;
  }

  /**
   * Handles add SchoolSubject event.
   *
   * @param event event
   * @throws IOException Dialog loading error
   */
  @FXML
  public void onAddSchoolSubject(ActionEvent event) throws IOException {
    LOGGER.trace("Called onAddSchoolSubject(event: {})", event);
    // Get Dialog Content
    final GuiLoader<DialogSchoolSubjectController, DialogPane> helper
            = new GuiLoader<>("DialogSchoolSubject.fxml");
    final DialogSchoolSubjectController controller = helper.getController();
    final DialogPane root = helper.getRoot();

    // Initialize Dialog
    final Dialog<ButtonType> dialog = new Dialog<>();
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.initOwner(stage);
    dialog.setTitle("Neues Schulfach hinzufügen");
    dialog.setDialogPane(root);

    // Open Dialog and evaluate result
    final Optional<ButtonType> result = dialog.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.FINISH) {
      // Absenden
      final LocalDate exemptSince = controller.getExemptSince();
      final String labelSchool = controller.getLabel();

      // Validiation
      final List<String> errors = new ArrayList<>();
      if (labelSchool == null || labelSchool.trim().length() == 0) {
        errors.add("Die Bezeichnung darf nicht leer sein.");
      }

      if (!errors.isEmpty()) {
        // Errors
        Utils.createErrorMessage(errors);
      } else {
        // Set new content
        final SchoolSubject subject = new SchoolSubject(labelSchool, exemptSince);
        trainingPeriod.addSchoolSubject(subject);
        Configuration.getInstance().setModified(true);
        initSchoolSubjects();
      }
    }
  }

  /**
   * Handles the delete SchoolSubject event.
   *
   * @param event event
   */
  @FXML
  public void onDeleteSchoolSubject(ActionEvent event) {
    LOGGER.trace("Called onDeleteSchoolSubject(event: {})", event);
    final SchoolSubject subject = subjects.getSelectionModel().getSelectedItem();
    if (subject == null) {
      Utils.createErrorMessage("Kein Schulfach in der Liste ausgewählt");
      return;
    }
    if (!trainingPeriod.getSchoolSubjects().remove(subject)) {
      Utils.createErrorMessage("Schulfach wurde nicht gefunden.");
      return;
    }
    initSchoolSubjects();
  }

  /**
   * Handles the edit SchoolSubject event.
   *
   * @param event event
   * @throws IOException Loading error
   */
  @FXML
  public void onEditSchoolSubject(ActionEvent event) throws IOException {
    LOGGER.trace("Called onEditSchoolSubject(event: {})", event);
    final SchoolSubject subject = subjects.getSelectionModel().getSelectedItem();
    if (subject == null) {
      Utils.createErrorMessage("Kein Schulfach in der Liste ausgewählt");
      return;
    }
    // Get Dialog Content
    final GuiLoader<DialogSchoolSubjectController, DialogPane> helper
            = new GuiLoader<>("DialogSchoolSubject.fxml");
    final DialogSchoolSubjectController controller = helper.getController();
    final DialogPane root = helper.getRoot();

    // Initialize Dialog
    final Dialog<ButtonType> dialog = new Dialog<>();
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.initOwner(stage);
    dialog.setTitle("Neues Schulfach hinzufügen");
    dialog.setDialogPane(root);

    // Initialize Dialog Content
    controller.setExemptSince(subject.getExemptSince());
    controller.setLabel(subject.getLabel());

    // Open Dialog and evaluate result
    final Optional<ButtonType> result = dialog.showAndWait();
    if (result.get() == ButtonType.CANCEL) {
      // Abbruch
    } else if (result.get() == ButtonType.FINISH) {
      // Absenden
      final LocalDate exemptSince = controller.getExemptSince();
      final String labelValue = controller.getLabel();

      // Validiation
      final List<String> errors = new ArrayList<>();
      if (labelValue == null || labelValue.trim().length() == 0) {
        errors.add("Die Bezeichnung darf nicht leer sein.");
      }

      if (!errors.isEmpty()) {
        // Errors
        Utils.createErrorMessage(errors);
      } else {
        // Set new content
        subject.setExemptSince(exemptSince);
        subject.setLabel(labelValue);
        Configuration.getInstance().setModified(true);
        initSchoolSubjects();
      }
    }
  }

  public void setStage(Stage stage) {
    LOGGER.trace("Called setStage(stage: {})", stage);
    this.stage = stage;
  }

  /**
   * Sets the current TrainingPeriod.
   *
   * @param trainingPeriod the trainingPeriod to set, not null
   */
  public void setTrainingPeriod(TrainingPeriod trainingPeriod) {
    LOGGER.trace("Called setTrainingPeriod(trainingPeriod: {})", trainingPeriod);
    if (trainingPeriod == null) {
      throw new NullPointerException("trainingPeriod cannot be null.");
    }
    this.trainingPeriod = trainingPeriod;
    final Configuration config = Configuration.getInstance();

    label.setText(trainingPeriod.getLabel());
    label.textProperty().addListener((observable, oldValue, newValue) -> {
      trainingPeriod.setLabel(newValue);
      config.setModified(true);
    });
    schoolClass.setText(trainingPeriod.getSchoolClass());
    schoolClass.textProperty().addListener((observable, oldValue, newValue) -> {
      trainingPeriod.setLabel(newValue);
      config.setModified(true);
    });
    classTeacher.setText(trainingPeriod.getClassTeacher());
    classTeacher.textProperty().addListener((observable, oldValue, newValue) -> {
      trainingPeriod.setLabel(newValue);
      config.setModified(true);
    });
    begin.setText(Utils.formatDate(trainingPeriod.getBegin()));
    end.setText(Utils.formatDate(trainingPeriod.getEnd()));
    initSchoolSubjects();
  }

  @FXML
  private void initialize() {
    LOGGER.trace("Called initialize()");
    subjects.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  private void initSchoolSubjects() {
    LOGGER.trace("Called initSchoolSubjects()");
    subjects.getItems().clear();
    subjects.getItems().addAll(trainingPeriod.getSchoolSubjects());
  }
}
