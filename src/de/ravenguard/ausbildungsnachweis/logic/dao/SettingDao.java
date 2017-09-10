package de.ravenguard.ausbildungsnachweis.logic.dao;

import de.ravenguard.ausbildungsnachweis.logic.Settings;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SettingDao {
  private static final Logger LOGGER = LogManager.getLogger(SettingDao.class);

  /**
   * Deletes the file at the path.
   *
   * @param settingsPath path to file
   * @throws IOException I/O error
   */
  public static void deletePath(Path settingsPath) throws IOException {
    LOGGER.trace("Called deletePath(settingsPath: {})", settingsPath);

    Files.deleteIfExists(settingsPath);
  }

  /**
   * Reads and loads the Settings from the XML file on settingsPath.
   *
   * @param settingsPath Path of XML file.
   * @return Loaded Settings instance or null if not parseable
   * @throws JAXBException IO error or parse error
   */
  public static Settings readFromPath(Path settingsPath) throws JAXBException {
    LOGGER.trace("Called readFromPath(settingsPath: {})", settingsPath);

    final JAXBContext jc = JAXBContext.newInstance(Settings.class);
    final Unmarshaller u = jc.createUnmarshaller();
    return (Settings) u.unmarshal(settingsPath.toFile());
  }

  /**
   * Saves a Settings instance to a file.
   *
   * @param settingsPath path to save to
   * @param settings Settings instance to save
   * @throws JAXBException Parse error
   * @throws IOException Read / write error
   */
  public static void saveToPath(Path settingsPath, Settings settings)
      throws IOException, JAXBException {
    LOGGER.trace("Called saveToPath(settingsPath: {}, settings: {})", settingsPath, settings);

    final JAXBContext jc = JAXBContext.newInstance(Settings.class);
    final Marshaller m = jc.createMarshaller();
    final OutputStream os = Files.newOutputStream(settingsPath, StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    m.marshal(settings, os);
  }
}
