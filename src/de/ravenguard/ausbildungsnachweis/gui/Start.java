package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.gui.elements.AlertException;
import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import de.ravenguard.ausbildungsnachweis.logic.InstallStatus;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.JAXBException;

public class Start extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      // Do basic loading
      final Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
      final Scene scene = new Scene(root, 400, 400);
      scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
      primaryStage.setMaximized(true);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Ausbildungsnachweis");
      primaryStage.show();

      // Load settings
      final Configuration config = Configuration.getInstance();

      // get directory
      final DirectoryChooser dirChooser = new DirectoryChooser();
      dirChooser.setTitle("Wähle Setting Verzeichnis");
      final File selectedDir = dirChooser.showDialog(primaryStage);
      if (selectedDir != null) {
        config.setSettingsPath(Paths.get(selectedDir.toURI()));

        final InstallStatus status = config.loadSettings();
        switch (status) {
          case NOT_INSTALLED:
            // create folders
            Files.createDirectories(config.getInstallPath());
            createNewSettings(config, primaryStage);
            break;
          case NOT_FOUND_SETTINGS:
            // Create dialog and sets new settings.
            createNewSettings(config, primaryStage);
            break;
          case PARSE_ERROR_SETTINGS:
            // Create dialog and sets new settings.
            createNewSettings(config, primaryStage);
            break;
          case OK:
            // Nothing to do
            break;
          default:
            // Unknown status
            break;
        }
      } else {
        final Alert alert = new Alert(AlertType.ERROR,
            "Kein Verzeichnis gewählt. Das Programm wird beendet.", ButtonType.OK);
        alert.showAndWait();

        primaryStage.close();
      }
    } catch (final Exception e) {
      new AlertException(e).show();
      primaryStage.close();
    }
  }

  private void createNewSettings(Configuration config, Stage primaryStage)
      throws IOException, JAXBException {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogSettings.fxml"));
    final DialogPane root = loader.load();
    final DialogSettingsController controller = loader.getController();
    controller.setConfiguration(config);

    final Dialog<ButtonType> dialog = new Dialog<>();
    dialog.initOwner(primaryStage);
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.setDialogPane(root);

    final Optional<ButtonType> result = dialog.showAndWait();

    result.ifPresent(buttonType -> {
      if (buttonType == ButtonType.FINISH) {
        try {
          config.saveSettings();
        } catch (final IOException e) {
          new AlertException(e).show();
        } catch (final JAXBException e) {
          new AlertException(e).show();
        }
        dialog.close();
      }
    });
  }
}
