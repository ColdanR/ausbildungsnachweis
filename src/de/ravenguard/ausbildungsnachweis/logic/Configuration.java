package de.ravenguard.ausbildungsnachweis.logic;

import de.ravenguard.ausbildungsnachweis.logic.dao.HolidaysDao;
import de.ravenguard.ausbildungsnachweis.logic.dao.SettingDao;
import de.ravenguard.ausbildungsnachweis.model.Settings;
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

public class Configuration {
  private static final String SETTINGS = "settings.xml";
  private static final String HOLIDAYS = "holidays.xml";
  private static final Logger LOGGER = LogManager.getLogger(Configuration.class);

  private static Configuration INSTANCE = new Configuration();

  /**
   * Returns the singleton instance.
   *
   * @return Instance of Install
   */
  public static Configuration getInstance() {
    LOGGER.trace("Called getInstance()");
    return INSTANCE;
  }

  private final List<LocalDate> holidays = new ArrayList<>();
  private Settings settings;
  private final Path installPath = Paths.get(System.getenv("user.home"), "ausbildungsnachweis");
  private Path currentFile;

  private Configuration() {
    LOGGER.trace("Called Configuration()");
  }

  /**
   * Adds a holiday to the list.
   *
   * @param holiday the holiday to add, may not be null
   */
  public void addHoliday(LocalDate holiday) {
    LOGGER.trace("Called addHoliday(holiday: {})", holiday);
    if (holiday == null) {
      throw new IllegalArgumentException("holiday may not be null.");
    }
    if (holidays.contains(holiday)) {
      throw new IllegalArgumentException("holiday already in the list");
    }
    holidays.add(holiday);
  }

  /**
   * Deletes a holiday from the list.
   *
   * @param holiday holiday to delete, may not be null
   */
  public void deleteHoliday(LocalDate holiday) {
    LOGGER.trace("Called deleteHoliday(holiday: {})", holiday);
    if (holiday == null) {
      throw new IllegalArgumentException("holiday may not be null.");
    }
    holidays.remove(holiday);
  }

  public Path getCurrentFile() {
    LOGGER.trace("Called getCurrentFile()");
    return currentFile;
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

  public boolean isCompanyAndSchool() {
    return settings.isCompanyAndSchool();
  }

  /**
   * Checks if the date is a holiday.
   *
   * @param date date to check, may not be null.
   * @return true if holiday, false otherwise
   */
  public boolean isHoliday(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("date may not be null.");
    }
    return holidays.contains(date);
  }

  public boolean isSaturdayWorkday() {
    LOGGER.trace("Called isSaturdayWorkday()");
    return settings.isSaturdayWorkday();
  }

  public boolean isSundayWorkday() {
    return settings.isSundayWorkday();
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

  public void setCompanyAndSchool(boolean companyAndSchool) {
    settings.setCompanyAndSchool(companyAndSchool);
  }

  public void setCurrentFile(Path currentFile) {
    LOGGER.trace("Called setCurrentFile(currentFile: {})", currentFile);
    this.currentFile = currentFile;
  }

  public void setSaturdayWorkday(boolean saturdayWorkday) {
    LOGGER.trace("Called setSaturdayWorkday(saturdayWorkday: {})", saturdayWorkday);
    settings.setSaturdayWorkday(saturdayWorkday);
  }

  public void setSundayWorkday(boolean sundayWorkday) {
    settings.setSundayWorkday(sundayWorkday);
  }
}
