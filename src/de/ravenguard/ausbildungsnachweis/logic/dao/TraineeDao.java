package de.ravenguard.ausbildungsnachweis.logic.dao;

import de.ravenguard.ausbildungsnachweis.model.Trainee;
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

public class TraineeDao {
  private static final Logger LOGGER = LogManager.getLogger(TraineeDao.class);

  /**
   * Deletes the file at the path.
   *
   * @param path path to file
   * @throws IOException I/O error
   */
  public static void deletePath(Path path) throws IOException {
    LOGGER.trace("Called deletePath(path: {})", path);

    if (path == null) {
      throw new NullPointerException("Path cannot be null.");
    }

    Files.deleteIfExists(path);
  }

  /**
   * Loads the Collection of legal holidays.
   *
   * @param path Path of XML file
   * @return Trainee instance
   * @throws JAXBException Parse error
   */
  public static Trainee getFromPath(Path path) throws JAXBException {
    LOGGER.trace("Called readFromPath(holidayPath: {})", path);

    if (path == null) {
      throw new NullPointerException("Path cannot be null.");
    }

    final JAXBContext jc = JAXBContext.newInstance(Trainee.class);
    final Unmarshaller u = jc.createUnmarshaller();
    return (Trainee) u.unmarshal(path.toFile());
  }

  /**
   * Saves a Collection of legal holidays to the path.
   *
   * @param path path of XML file
   * @param trainee list of legal holidays
   * @throws JAXBException parse error
   * @throws IOException I/O error
   */
  public static void saveToPath(Path path, Trainee trainee) throws JAXBException, IOException {
    LOGGER.trace("Called saveToPath(path: {}, trainee: {})", path, trainee);

    if (trainee == null) {
      throw new IllegalArgumentException("trainee cannot be null.");
    }

    if (path == null) {
      throw new IllegalArgumentException("path cannot be null.");
    }

    final JAXBContext jc = JAXBContext.newInstance(Trainee.class);
    final Marshaller m = jc.createMarshaller();
    final OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

    m.marshal(trainee, os);
  }
}
