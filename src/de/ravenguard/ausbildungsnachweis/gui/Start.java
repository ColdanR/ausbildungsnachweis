package de.ravenguard.ausbildungsnachweis.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Start extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      final BorderPane root = new BorderPane();
      final Scene scene = new Scene(root, 400, 400);
      scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
