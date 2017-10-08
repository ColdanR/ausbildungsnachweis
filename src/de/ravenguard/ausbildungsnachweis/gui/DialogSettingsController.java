package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogSettingsController {
  private static final Logger LOGGER = LogManager.getLogger(DialogSettingsController.class);
  @FXML
  private CheckBox saturdayWorkday;
  @FXML
  private CheckBox sundayWorkday;
  @FXML
  private CheckBox companyAndWork;

  private Configuration config;

  public Configuration getConfiguration() {
    LOGGER.trace("Called getConfiguration()");
    return config;
  }

  /**
   * Binds the {@link Configuration} to the dialog and initiates the values of the gui.
   *
   * @param config {@link Configuration} instance
   */
  public void setConfiguration(Configuration config) {
    LOGGER.trace("Called setConfiguration(config: {})", config);
    this.config = config;

    saturdayWorkday.selectedProperty().set(config.isSaturdayWorkday());
    sundayWorkday.selectedProperty().set(config.isSundayWorkday());
    companyAndWork.selectedProperty().set(config.isCompanyAndSchool());

    saturdayWorkday.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
      config.setSaturdayWorkday(isNowSelected.booleanValue());
    });

    sundayWorkday.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
      config.setSundayWorkday(isNowSelected.booleanValue());
    });

    companyAndWork.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
      config.setCompanyAndSchool(isNowSelected.booleanValue());
    });
  }
}
