package de.ravenguard.ausbildungsnachweis.gui.elements;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AlertException extends Alert {
  /**
   * Creates an alert for the exception and displays it.
   *
   * @param throwable exception to show
   */
  public AlertException(Throwable throwable) {
    super(AlertType.ERROR);
    setTitle("Exception Dialog");
    setHeaderText("An unexpected exception has occured.");

    // Create expandable Exception.
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw);
    throwable.printStackTrace(pw);
    final String exceptionText = sw.toString();

    final Label label = new Label("The exception stacktrace was:");

    final TextArea textArea = new TextArea(exceptionText);
    textArea.setEditable(false);
    textArea.setWrapText(true);

    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    final GridPane expContent = new GridPane();
    expContent.setMaxWidth(Double.MAX_VALUE);
    expContent.add(label, 0, 0);
    expContent.add(textArea, 0, 1);

    // Set expandable Exception into the dialog pane.
    getDialogPane().setExpandableContent(expContent);
  }
}
