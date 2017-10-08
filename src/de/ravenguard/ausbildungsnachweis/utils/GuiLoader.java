package de.ravenguard.ausbildungsnachweis.utils;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class GuiLoader<C, P extends Node> {
  private final C controller;
  private final P root;

  /**
   * Loads the controller and Node for the content or dialog.
   *
   * @param template Name of FXML file
   * @throws IOException loading error
   */
  public GuiLoader(String template) throws IOException {
    template = "/de/ravenguard/ausbildungsnachweis/gui/" + template;
    final FXMLLoader loader = new FXMLLoader(getClass().getResource(template));
    root = loader.load();
    controller = loader.getController();
  }

  public C getController() {
    return controller;
  }

  public P getRoot() {
    return root;
  }
}
