package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.gui.elements.TraineeWrapper;
import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import de.ravenguard.ausbildungsnachweis.logic.TraineeLogic;
import de.ravenguard.ausbildungsnachweis.model.DataMonth;
import de.ravenguard.ausbildungsnachweis.model.IllegalDateException;
import de.ravenguard.ausbildungsnachweis.model.Trainee;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import de.ravenguard.ausbildungsnachweis.model.TreeElement;
import de.ravenguard.ausbildungsnachweis.utils.GuiLoader;
import de.ravenguard.ausbildungsnachweis.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainController {
  private static final Logger LOGGER = LogManager.getLogger(MainController.class);
  private final Configuration config = Configuration.getInstance();
  private Trainee trainee;
  private Stage primaryStage;

  @FXML
  private TreeView<TreeElement> treeView;
  @FXML
  private ScrollPane contentPane;
  @FXML
  private Label familyName;
  @FXML
  private Label givenNames;
  @FXML
  private Label school;
  @FXML
  private Label trainer;
  @FXML
  private Label training;
  @FXML
  private Label trainingBegin;
  @FXML
  private Label trainingEnd;

  public Stage getPrimaryStage() {
    LOGGER.trace("Called getPrimaryStage()");
    return primaryStage;
  }

  /**
   * Initializes the controller.
   */
  @FXML
  public void initialize() {
    LOGGER.trace("Called initialize()");
    treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    treeView.setEditable(false);
    treeView.setShowRoot(false);

    treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue != null) {
        try {
          if (newValue.getValue() instanceof TrainingPeriod) {
            final GuiLoader<ContentTrainingPeriodController, AnchorPane> helper =
                new GuiLoader<>("ContentTrainingPeriod.fxml");
            final ContentTrainingPeriodController controller = helper.getController();
            controller.setTrainingPeriod((TrainingPeriod) newValue.getValue());
            controller.setStage(primaryStage);
            contentPane.setContent(helper.getRoot());
          } else if (newValue.getValue() instanceof DataMonth) {
            final GuiLoader<ContentDataMonthController, AnchorPane> helper =
                new GuiLoader<>("ContentDataMonth.fxml");
            final ContentDataMonthController controller = helper.getController();
            controller.setData((DataMonth) newValue.getValue(),
                (TrainingPeriod) newValue.getParent().getValue(), primaryStage, trainee);
            contentPane.setContent(helper.getRoot());
          } else {
            contentPane.setContent(null);
          }
        } catch (final IOException e) {
          Utils.createExceptionAlert(e);
          contentPane.setContent(null);
        }
      } else {
        contentPane.setContent(null);
      }
    });
  }

  /**
   * Starts edit of trainee.
   *
   * @param event event
   * @throws IOException IOException
   */
  @FXML
  public void onEditTrainee(ActionEvent event) throws IOException {
    LOGGER.trace("Called onEditTrainee(event: {})", event);
    if (trainee == null) {
      Utils.createErrorMessage(
          "Kein Auszubildender geladen. Es können keine Änderungen vorgenommen werden.");
      return;
    }
    traineeDialog();
  }

  /**
   * Checks for unsaved changes, ask to save and exits the program.
   *
   * @param event event
   */
  @FXML
  public void onMenuExit(ActionEvent event) {
    LOGGER.trace("Called onMenuExit(event: {})", event);
    // Check unsaved changes
    if (!saveBeforeNew()) {
      return;
    }
    primaryStage.close();
  }

  /**
   * Checks for unsaved changes, ask to save and loads a chosen File for the user.
   *
   * @param event event
   */
  @FXML
  public void onMenuLoad(ActionEvent event) {
    LOGGER.trace("Called onMenuLoad(event: {})", event);
    // Check unsaved changes
    if (!saveBeforeNew()) {
      return;
    }
    // Neue Datei laden
    final Path loadPath = loadPath();
    if (loadPath == null) {
      return;
    }
    final TraineeLogic logic = new TraineeLogic();
    try {
      trainee = logic.readTrainee(loadPath);
      config.setCurrentFile(loadPath);
      setGuiFromTrainee();
      Utils.createInfoMessage("Datei erfolgreich geladen.");
    } catch (final JAXBException e) {
      Utils.createErrorMessage("Fehlerhafte Datei ausgewählt. Ladevorgang wurde abgebrochen.");
    }
  }

  /**
   * Checks for unsaved changes, ask to save and creates a new trainee.
   *
   * @param event event
   * @throws IOException error showing dialog
   */
  @FXML
  public void onMenuNew(ActionEvent event) throws IOException {
    LOGGER.trace("Called onMenuNew(event: {})", event);
    // Check unsaved changes
    if (!saveBeforeNew()) {
      return;
    }
    // Create and handle trainee Dialog
    traineeDialog();
  }

  /**
   * Saves the trainee, ask for store location, if none is known.
   *
   * @param event event
   */
  @FXML
  public void onMenuSave(ActionEvent event) {
    LOGGER.trace("Called onMenuSave(event: {})", event);
    final Path savePath = getSavePath();
    try {
      saveTrainee(savePath);
      Utils.createInfoMessage("Datei gespeichert.");
    } catch (final JAXBException e) {
      Utils.createExceptionAlert(e);
    } catch (final IOException e) {
      Utils.createExceptionAlert(e);
    }
  }

  /**
   * Ask for a store location and saves the trainee.
   *
   * @param event event
   */
  @FXML
  public void onMenuSaveUnder(ActionEvent event) {
    LOGGER.trace("Called onMenuSaveUnder(event: {})", event);
    final Path savePath = savePathFromDialog();
    try {
      saveTrainee(savePath);
      Utils.createInfoMessage("Datei gespeichert.");
    } catch (final JAXBException e) {
      Utils.createExceptionAlert(e);
    } catch (final IOException e) {
      Utils.createExceptionAlert(e);
    }
  }

  /**
   * Starts adding a new training period.
   *
   * @param event event
   * @throws IOException IO error
   */
  @FXML
  public void onNewTrainingPeriod(ActionEvent event) throws IOException {
    LOGGER.trace("Called onNewTrainingPeriod(event: {})", event);
    if (trainee == null) {
      Utils.createErrorMessage("Kein(e) Auszubildene(r) geladen oder erstellt.");
      return;
    }
    // Create Dialog
    final GuiLoader<DialogTrainingPeriodController, DialogPane> loader =
        new GuiLoader<>("DialogTrainingPeriod.fxml");
    final DialogPane root = loader.getRoot();
    final DialogTrainingPeriodController controller = loader.getController();

    final Dialog<ButtonType> dialog = new Dialog<>();
    dialog.initOwner(primaryStage);
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.setDialogPane(root);
    // getResult
    final Optional<ButtonType> result = dialog.showAndWait();
    if (result.get() == ButtonType.CANCEL) {
      return;
    } else if (result.get() == ButtonType.FINISH) {
      final String label = controller.getLabel();
      final String schoolClass = controller.getSchoolClass();
      final String classTeacher = controller.getClassTeacher();
      final LocalDate begin = controller.getBegin();
      final LocalDate end = controller.getEnd();

      // Validierung
      final List<String> errors = new ArrayList<>();
      if (label == null || label.trim().length() == 0) {
        errors.add("Die Bezeichnung darf nicht leer sein.");
      }
      if (schoolClass == null || schoolClass.trim().length() == 0) {
        errors.add("Die Berufsschulklasse darf nicht leer sein.");
      }
      if (classTeacher == null || classTeacher.trim().length() == 0) {
        errors.add("Der Klassenlehrer darf nicht leer sein.");
      }
      if (begin == null) {
        errors.add("Der Anfang muss ausgefüllt sein.");
      } else if (begin.isBefore(trainee.getBegin())) {
        errors.add("Der Anfang kann nicht vor dem Anfang der Ausbildung liegen.");
      }
      if (end == null) {
        errors.add("Das Ende muss ausgefüllt sein.");
      } else if (end.isAfter(trainee.getEnd())) {
        errors.add("Das Ende kann nicht nach dem Ende der Ausbildung liegen.");
      }
      if (begin != null && end != null) {
        for (final TrainingPeriod period : trainee.getTrainingPeriods()) {
          final LocalDate periodBegin = period.getBegin();
          final LocalDate periodEnd = period.getEnd();
          if ((begin.isAfter(periodBegin) || begin.isEqual(periodBegin))
              && (begin.isBefore(periodEnd) || begin.isEqual(periodEnd))) {
            errors.add("Der Anfang ist innerhalb des Zeitraums von " + period.getLabel());
          }
          if ((end.isAfter(periodBegin) || end.isEqual(periodBegin))
              && (end.isBefore(periodEnd) || end.isEqual(periodEnd))) {
            errors.add("Das Ende ist innerhalb des Zeitraums von " + period.getLabel());
          }
        }
      }

      // Validation Result
      if (errors.size() > 0) {
        Utils.createErrorMessage(errors);
        return;
      } else {
        final TraineeLogic logic = new TraineeLogic();
        try {
          logic.addTrainingPeriode(label, begin, end, schoolClass, classTeacher, trainee);
        } catch (final IllegalDateException e) {
          Utils.createExceptionAlert(e);
          return;
        }
      }
    }
    // Reset TreeView
    treeView.setRoot(new TraineeWrapper(trainee));
  }

  public void setPrimaryStage(Stage primaryStage) {
    LOGGER.trace("Called setPrimaryStage(primaryStage: {})", primaryStage);
    this.primaryStage = primaryStage;
  }

  private Path getSavePath() {
    LOGGER.trace("Called getSavePath()");
    Path savePath = config.getCurrentFile();
    if (savePath == null) {
      savePath = savePathFromDialog();
    }
    return savePath;
  }

  private Path loadPath() {
    LOGGER.trace("Called loadPath()");
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Datei laden...");
    fileChooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
    fileChooser.setInitialDirectory(Configuration.getInstance().getInstallPath().toFile());
    final File selectedFile = fileChooser.showOpenDialog(primaryStage);
    if (selectedFile == null) {
      return null;
    }
    return Paths.get(selectedFile.toURI());
  }

  private boolean saveBeforeNew() {
    LOGGER.trace("Called saveBeforeNew()");
    if (trainee != null && config.isModified()) {
      // Alert
      final Alert alert = new Alert(AlertType.CONFIRMATION,
          "Es gibt nicht gespeichete Änderungen am Datenstand. Diesen jetzt speichern?",
          ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
      final Optional<ButtonType> returnValue = alert.showAndWait();

      if (returnValue.get() == ButtonType.YES) {
        final Path savePath = getSavePath();
        try {
          saveTrainee(savePath);
          trainee = null;
          config.setModified(false);
          setGuiFromTrainee();
          return true;
        } catch (final JAXBException e) {
          Utils.createExceptionAlert(e);
          return false;
        } catch (final IOException e) {
          Utils.createExceptionAlert(e);
          return false;
        }
      } else if (returnValue.get() == ButtonType.NO) {
        trainee = null;
        config.setModified(false);
        setGuiFromTrainee();
        return true;
      } else if (returnValue.get() == ButtonType.CANCEL) {
        return false;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  private Path savePathFromDialog() {
    LOGGER.trace("Called savePathFromDialog()");
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Datei speichern...");
    fileChooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
    fileChooser.setInitialDirectory(Configuration.getInstance().getInstallPath().toFile());
    final File selectedFile = fileChooser.showOpenDialog(primaryStage);
    if (selectedFile == null) {
      return null;
    }
    return Paths.get(selectedFile.toURI());
  }

  private void saveTrainee(Path savePath) throws JAXBException, IOException {
    LOGGER.trace("Called saveTrainee(savePath: {})", savePath);
    // No savePath -> Return
    if (savePath == null) {
      return;
    }
    // No Trainee -> Error Alert
    if (trainee == null) {
      Utils.createErrorMessage("Keine Daten geladen.");
      return;
    }
    // Do save
    final TraineeLogic logic = new TraineeLogic();
    logic.saveTrainee(savePath, trainee);
  }

  private void setGuiFromTrainee() {
    LOGGER.trace("Called setGuiFromTrainee()");
    if (trainee == null) {
      familyName.setText("");
      givenNames.setText("");
      school.setText("");
      trainer.setText("");
      training.setText("");
      trainingBegin.setText("");
      trainingEnd.setText("");
      treeView.setRoot(null);
    } else {
      familyName.setText(trainee.getFamilyName());
      givenNames.setText(trainee.getGivenNames());
      school.setText(trainee.getSchool());
      trainer.setText(trainee.getTrainer());
      training.setText(trainee.getTraining());
      trainingBegin.setText(Utils.formatDate(trainee.getBegin()));
      trainingEnd.setText(Utils.formatDate(trainee.getEnd()));
      // Reset ListView
      treeView.setRoot(null);
      if (trainee != null) {
        treeView.setRoot(new TraineeWrapper(trainee));
      }
    }
  }

  private void traineeDialog() throws IOException {
    LOGGER.trace("Called traineeDialog()");
    // Dialog erschaffen
    final GuiLoader<DialogTraineeController, DialogPane> loader =
        new GuiLoader<>("DialogTrainee.fxml");
    final DialogPane root = loader.getRoot();
    final DialogTraineeController controller = loader.getController();

    final Dialog<ButtonType> dialog = new Dialog<>();
    dialog.initOwner(primaryStage);
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.setDialogPane(root);

    if (trainee != null) {
      controller.setBegin(trainee.getBegin());
      controller.setEnd(trainee.getEnd());
      controller.setFamilyName(trainee.getFamilyName());
      controller.setGivenNames(trainee.getGivenNames());
      controller.setSchool(trainee.getSchool());
      controller.setTrainer(trainee.getTrainer());
      controller.setTraining(trainee.getTraining());
    }

    final Optional<ButtonType> result = dialog.showAndWait();

    result.ifPresent(buttonType -> {
      if (buttonType == ButtonType.FINISH) {
        final String familyName = controller.getFamilyName();
        final String givenNames = controller.getGivenNames();
        final LocalDate begin = controller.getBegin();
        final LocalDate end = controller.getEnd();
        final String trainer = controller.getTrainer();
        final String school = controller.getSchool();
        final String training = controller.getTraining();
        final List<String> errors = new ArrayList<>();

        if (familyName == null || familyName.trim().length() == 0) {
          errors.add("Der Familienname muss ausgefüllt sein.");
        }
        if (givenNames == null || givenNames.trim().length() == 0) {
          errors.add("Vorname(n) muss ausgefüllt sein.");
        }
        if (trainer == null || trainer.trim().length() == 0) {
          errors.add("Der Ausbilder muss ausgefüllt sein.");
        }
        if (school == null || school.trim().length() == 0) {
          errors.add("Die Berufsschule muss ausgefüllt sein.");
        }
        if (training == null || training.trim().length() == 0) {
          errors.add("Der Ausbildungsberuf muss ausgefüllt sein.");
        }
        if (begin == null) {
          errors.add("Beginn der Ausbildung muss ausgewählt sein.");
        }
        if (begin == null) {
          errors.add("(Voraussichtliches) Ende der Ausbildung muss ausgewählt sein.");
        }
        if (begin != null && end != null && begin.isAfter(end)) {
          errors.add(
              "Beginn der Ausbildung muss vor dem (voraussichtlichen) Ende der Ausbildung sein.");
        }
        if (errors.size() > 0) {
          Utils.createErrorMessage(errors);
          return;
        } else if (trainee != null) {
          try {
            trainee.setBegin(begin);
            trainee.setEnd(end);
            trainee.setFamilyName(familyName);
            trainee.setGivenNames(givenNames);
            trainee.setSchool(school);
            trainee.setTrainer(trainer);
            trainee.setTraining(training);
            config.setModified(true);
            setGuiFromTrainee();
          } catch (final IllegalDateException e) {
            Utils.createExceptionAlert(e);
          }
        } else {
          final TraineeLogic logic = new TraineeLogic();
          try {
            trainee = logic.create(familyName, givenNames, begin, end, trainer, school, training);
            config.setCurrentFile(null);
            setGuiFromTrainee();
            dialog.close();
          } catch (IllegalDateException | IllegalArgumentException e) {
            Utils.createExceptionAlert(e);
          }
        }
      } else if (buttonType == ButtonType.CANCEL) {
        dialog.close();
      }
    });
  }
}
