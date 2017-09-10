package de.ravenguard.ausbildungsnachweis.logic;

import de.ravenguard.ausbildungsnachweis.logic.dao.HolidaysDao;
import de.ravenguard.ausbildungsnachweis.logic.dao.SettingDao;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Install {
  private static final String SETTINGS = "settings.xml";
  private static final String HOLIDAYS = "holidays.xml";
  private static final Logger LOGGER = LogManager.getLogger(Install.class);

  private static Install INSTANCE = new Install();

  /**
   * Returns the singleton instance.
   *
   * @return Instance of Install
   */
  public static Install getInstance() {
    LOGGER.trace("Called getInstance()");
    return INSTANCE;
  }

  private final List<LocalDate> holidays = new ArrayList<>();
  private Settings settings;
  private final Path installPath = Paths.get(System.getenv("user.home"), "ausbildungsnachweis");
  private Path currentFile;

  private Install() {
    LOGGER.trace("Called Install()");
  }

  public void addHoliday(LocalDate holiday) {
    LOGGER.trace("Called addHoliday(holiday: {})", holiday);
    holidays.add(holiday);
  }

  public void deleteHoliday(LocalDate holiday) {
    LOGGER.trace("Called deleteHoliday(holiday: {})", holiday);
    holidays.remove(holiday);
  }

  public Path getCurrentFile() {
    LOGGER.trace("Called getCurrentFile()");
    return currentFile;
  }

  public String getDefaultStorage() {
    LOGGER.trace("Called getDefaultStorage()");
    return settings.getDefaultStorage();
  }

  public Path getHolidaysPath() {
    LOGGER.trace("Called getHolidaysPath()");
    return installPath.resolve(HOLIDAYS);
  }

  public Path getInstallPath() {
    LOGGER.trace("Called getInstallPath()");
    return installPath;
  }

  public Path getSettingsPath() {
    LOGGER.trace("Called getSettingsPath()");
    return installPath.resolve(SETTINGS);
  }

  public boolean isSaturdayWorkday() {
    LOGGER.trace("Called isSaturdayWorkday()");
    return settings.isSaturdayWorkday();
  }

  /**
   * Loads the current settings or returns the first error.
   *
   * @return InstallStatus
   */
  public InstallStatus loadSettings() {
    LOGGER.trace("Called loadSettings()");

    if (!Files.exists(installPath)) {
      return InstallStatus.NOT_INSTALLED;
    }

    final Path settingsPath = getSettingsPath();
    if (!Files.exists(settingsPath)) {
      return InstallStatus.NOT_FOUND_SETTINGS;
    }

    try {
      settings = SettingDao.readFromPath(settingsPath);
    } catch (final JAXBException e) {
      return InstallStatus.PARSE_ERROR_SETTINGS;
    }

    final Path holidayPath = getHolidaysPath();
    if (!Files.exists(holidayPath)) {
      return InstallStatus.NOT_FOUND_HOLIDAYS;
    }

    try {
      holidays.clear();
      holidays.addAll(HolidaysDao.getFromPath(holidayPath));
    } catch (final JAXBException e) {
      return InstallStatus.PARSE_ERROR_HOLIDAYS;
    }

    return InstallStatus.OK;
  }

  public void saveSettings() throws IOException, JAXBException {
    LOGGER.trace("Called saveSettings()");
    SettingDao.saveToPath(getSettingsPath(), settings);
  }

  public void setCurrentFile(Path currentFile) {
    LOGGER.trace("Called setCurrentFile(currentFile: {})", currentFile);
    this.currentFile = currentFile;
  }

  public void setDefaultStorage(String defaultStorage) {
    LOGGER.trace("Called setDefaultStorage(defaultStorage: {})", defaultStorage);
    settings.setDefaultStorage(defaultStorage);
  }

  public void setSaturdayWorkday(boolean saturdayWorkday) {
    LOGGER.trace("Called setSaturdayWorkday(saturdayWorkday: {})", saturdayWorkday);
    settings.setSaturdayWorkday(saturdayWorkday);
  }
}
