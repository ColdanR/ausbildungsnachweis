package de.ravenguard.ausbildungsnachweis.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      final Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
      final Scene scene = new Scene(root, 400, 400);
      scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
      primaryStage.setMaximized(true);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
