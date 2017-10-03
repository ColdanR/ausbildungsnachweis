package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.gui.elements.AlertException;
import de.ravenguard.ausbildungsnachweis.gui.elements.TraineeWrapper;
import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import de.ravenguard.ausbildungsnachweis.logic.TraineeLogic;
import de.ravenguard.ausbildungsnachweis.model.IllegalDateException;
import de.ravenguard.ausbildungsnachweis.model.Trainee;
import de.ravenguard.ausbildungsnachweis.model.TreeElement;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TreeView;
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

  public Stage getPrimaryStage() {
    LOGGER.trace("Called getPrimaryStage()");
    return primaryStage;
  }

  /**
   * Checks for unsaved changes, ask to save and exits the program.
   *
   * @param event event
   */
  @FXML
  public void onExit(ActionEvent event) {
    LOGGER.trace("Called onExit(event: {})", event);
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
      createTreeViewFromTrainee();
    } catch (final JAXBException e) {
      new AlertException(e).show();
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
    // Dialog erschaffen
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogNewTrainee.fxml"));
    final DialogPane root = loader.load();
    final DialogNewTraineeController controller = loader.getController();

    final Dialog<ButtonType> dialog = new Dialog<>();
    dialog.initOwner(primaryStage);
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.setDialogPane(root);

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
          final String errorText =
              errors.stream().collect(Collectors.joining(System.lineSeparator()));
          final Alert alert = new Alert(AlertType.ERROR, errorText, ButtonType.OK);
          alert.show();
        } else {
          final TraineeLogic logic = new TraineeLogic();
          try {
            trainee = logic.create(familyName, givenNames, begin, end, trainer, school, training);
            config.setCurrentFile(null);
            createTreeViewFromTrainee();
            dialog.close();
          } catch (IllegalDateException | IllegalArgumentException e) {
            new AlertException(e).show();
          }
        }
      } else if (buttonType == ButtonType.CANCEL) {
        dialog.close();
      }
    });
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
    } catch (final JAXBException e) {
      new AlertException(e).show();
    } catch (final IOException e) {
      new AlertException(e).show();
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
    } catch (final JAXBException e) {
      new AlertException(e).show();
    } catch (final IOException e) {
      new AlertException(e).show();
    }
  }

  @FXML
  public void onTreeSelect(ActionEvent event) {
    LOGGER.trace("Called onTreeSelect(event: {})", event);

  }

  public void setPrimaryStage(Stage primaryStage) {
    LOGGER.trace("Called setPrimaryStage(primaryStage: {})", primaryStage);
    this.primaryStage = primaryStage;
  }

  private void createTreeViewFromTrainee() {
    LOGGER.trace("Called createTreeViewFromTrainee()");
    treeView.setRoot(new TraineeWrapper(trainee));
    treeView.setVisible(true);
    treeView.setShowRoot(true);
    treeView.setEditable(false);
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
          // Refresh TreeView
          treeView.setRoot(null);
          return true;
        } catch (final JAXBException e) {
          new AlertException(e).show();
          return false;
        } catch (final IOException e) {
          new AlertException(e).show();
          return false;
        }
      } else if (returnValue.get() == ButtonType.NO) {
        trainee = null;
        config.setModified(false);
        // Refresh TreeView
        treeView.setRoot(null);
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
      final Alert alert = new Alert(AlertType.ERROR, "Keine Daten geladen.", ButtonType.OK);
      alert.show();
      return;
    }
    // Do save
    final TraineeLogic logic = new TraineeLogic();
    logic.saveTrainee(savePath, trainee);
  }
}
