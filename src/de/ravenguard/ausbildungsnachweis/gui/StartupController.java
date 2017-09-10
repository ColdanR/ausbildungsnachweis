package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.logic.Install;
import de.ravenguard.ausbildungsnachweis.logic.InstallStatus;
import java.io.IOException;
import java.nio.file.Files;
import javax.xml.bind.JAXBException;

public class StartupController {
  /**
   * Initializes the Configuration and returns possible errors.
   *
   * @throws IOException I/O error
   * @throws JAXBException I/O or parse error
   */
  public void startUp() {
    final Install install = Install.getInstance();
    final InstallStatus status = install.loadSettings();

    switch (status) {
      case NOT_INSTALLED:
        // create folders
        try {
          Files.createDirectories(install.getInstallPath());
        } catch (final IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        // Require new settings
        // Create dialog and sets new settings.
        // save settings
        try {
          install.saveSettings();
        } catch (IOException | JAXBException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      case NOT_FOUND_SETTINGS:
        // Require new settings
        // save settings
        try {
          install.saveSettings();
        } catch (IOException | JAXBException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      case PARSE_ERROR_SETTINGS:
        // Require new settings
        // save settings
        try {
          install.saveSettings();
        } catch (IOException | JAXBException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      case NOT_FOUND_HOLIDAYS:
        // Inform user
        break;
      case PARSE_ERROR_HOLIDAYS:
        // Inform user
        break;
      case OK:
        // Nothing to do
        break;
      default:
        break;
    }
  }
}
