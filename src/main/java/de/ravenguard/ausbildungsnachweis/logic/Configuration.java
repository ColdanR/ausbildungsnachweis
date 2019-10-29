package de.ravenguard.ausbildungsnachweis.logic;

import de.ravenguard.ausbildungsnachweis.logic.dao.SettingDao;
import de.ravenguard.ausbildungsnachweis.model.Settings;
import java.io.IOException;
import java.nio.file.Path;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Configuration {

  private static final String SETTINGS = "settings.xml";
  private static final Logger LOGGER = LogManager.getLogger(Configuration.class);

  private static final Configuration INSTANCE = new Configuration();

  /**
   * Returns the singleton instance.
   *
   * @return Instance of Install
   */
  public static Configuration getInstance() {
    LOGGER.trace("Called getInstance()");
    return INSTANCE;
  }

  private Settings settingsLoaded = new Settings();
  private Path installPath = null;
  private Path currentFile = null;
  private boolean modified = false;

  private Configuration() {
    LOGGER.trace("Called Configuration()");
  }

  /**
   * Returns the current file path, can be null if no such path is set.
   *
   * @return the current file path
   */
  public Path getCurrentFile() {
    LOGGER.trace("Called getCurrentFile()");
    LOGGER.debug("Returned getCurrentFile(): {}", currentFile);
    return currentFile;
  }

  /**
   * Returns the install path.
   *
   * @return install path
   */
  public Path getInstallPath() {
    LOGGER.trace("Called getInstallPath()");
    LOGGER.debug("Returned getInstallPath(): {}", installPath);
    return installPath;
  }

  /**
   * Returns the path to the current settings.
   *
   * @return settings path
   */
  public Path getSettingsPath() {
    LOGGER.trace("Called getSettingsPath()");
    LOGGER.debug("Returned getSettingsPath(): {}", installPath.resolve(SETTINGS));
    return installPath.resolve(SETTINGS);
  }

  /**
   * Returns if the trainee has school and company in one week.
   *
   * @return if trainee has school and company
   */
  public boolean isCompanyAndSchool() {
    LOGGER.trace("Called isCompanyAndSchool()");
    LOGGER.debug("Returned isCompanyAndSchool(): {}", settingsLoaded.isCompanyAndSchool());
    return settingsLoaded.isCompanyAndSchool();
  }

  public boolean isModified() {
    return modified;
  }

  /**
   * Returns if the trainee works Saturdays.
   *
   * @return trainee has to work Saturdays
   */
  public boolean isSaturdayWorkday() {
    LOGGER.trace("Called isSaturdayWorkday()");
    LOGGER.debug("Returned getInstallPath(): {}", installPath);
    return settingsLoaded.isSaturdayWorkday();
  }

  /**
   * Returns if the trainee works Sundays.
   *
   * @return trainee has to work Sundays
   */
  public boolean isSundayWorkday() {
    LOGGER.trace("Called isSaturdayWorkday()");
    LOGGER.debug("Returned isSundayWorkday(): {}", installPath);
    return settingsLoaded.isSundayWorkday();
  }

  /**
   * Loads the current settings or returns the first error.
   *
   * @return InstallStatus
   */
  public InstallStatus loadSettings() {
    LOGGER.trace("Called loadSettings()");

    if (!installPath.toFile().exists()) {
      return InstallStatus.NOT_INSTALLED;
    }

    final Path settingsPath = getSettingsPath();
    if (!settingsPath.toFile().exists()) {
      return InstallStatus.NOT_FOUND_SETTINGS;
    }

    try {
      settingsLoaded = SettingDao.readFromPath(settingsPath);
    } catch (final JAXBException e) {
      return InstallStatus.PARSE_ERROR_SETTINGS;
    }

    return InstallStatus.OK;
  }

  public void saveSettings() throws IOException, JAXBException {
    LOGGER.trace("Called saveSettings()");
    SettingDao.saveToPath(getSettingsPath(), settingsLoaded);
  }

  public void setCompanyAndSchool(boolean companyAndSchool) {
    LOGGER.trace("Called setCompanyAndSchool(companyAndSchool: {})", companyAndSchool);
    settingsLoaded.setCompanyAndSchool(companyAndSchool);
  }

  public void setCurrentFile(Path currentFile) {
    LOGGER.trace("Called setCurrentFile(currentFile: {})", currentFile);
    this.currentFile = currentFile;
  }

  public void setModified(boolean modified) {
    this.modified = modified;
  }

  public void setSaturdayWorkday(boolean saturdayWorkday) {
    LOGGER.trace("Called setSaturdayWorkday(saturdayWorkday: {})", saturdayWorkday);
    settingsLoaded.setSaturdayWorkday(saturdayWorkday);
  }

  public void setSettingsPath(Path path) {
    LOGGER.trace("Called setSettingsPath(path: {})", path);
    installPath = path;
  }

  public void setSundayWorkday(boolean sundayWorkday) {
    LOGGER.trace("Called setSundayWorkday(sundayWorkday: {})", sundayWorkday);
    settingsLoaded.setSundayWorkday(sundayWorkday);
  }
}
