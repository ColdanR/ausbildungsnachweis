package de.ravenguard.ausbildungsnachweis.logic;

import de.ravenguard.ausbildungsnachweis.logic.dao.TraineeDao;
import de.ravenguard.ausbildungsnachweis.model.Trainee;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TraineeLogic {
  private static final Logger LOGGER = LogManager.getLogger(TraineeLogic.class);

  /**
   * Creates a new Trainee Instance.
   *
   * @param familiyName family name, may not be null or empty
   * @param givenNames given name(s), may not be null or empty
   * @param begin begin of training, may not be null
   * @param end end of training, may not be null
   * @param trainer trainer name, may not be null or empty
   * @param school school name, may not be null or empty
   * @return new Trainee instance
   * @throws IllegalArgumentException if one parameter is null or a string is empty
   */
  public Trainee create(String familiyName, String givenNames, LocalDate begin, LocalDate end,
      String trainer, String school) {
    LOGGER.trace("Called create(familiyName: {}, givenNames: {}, begin: {}, end: {}, "
        + "trainer: {}, school: {}", familiyName, givenNames, begin, end, trainer, school);

    return new Trainee(familiyName.trim(), givenNames.trim(), begin, end, trainer.trim(),
        school.trim(), new ArrayList<>());
  }

  /**
   * Deletes a Trainee on the path.
   *
   * @param path path to the trainee
   * @throws IOException I/O error
   */
  public void deleteTrainee(Path path) throws IOException {
    LOGGER.trace("Called deleteTrainee(path: {})", path);

    TraineeDao.deletePath(path);
    if (Install.getInstance().getCurrentFile().equals(path)) {
      Install.getInstance().setCurrentFile(null);
    }
  }

  /**
   * Reads a trainee and returns it, after saving the path of it.
   *
   * @param path path to read from
   * @return Trainee instance
   * @throws JAXBException I/O error or parse error
   */
  public Trainee readTrainee(Path path) throws JAXBException {
    LOGGER.trace("Called readTrainee(path: {})", path);

    final Trainee trainee = TraineeDao.getFromPath(path);
    Install.getInstance().setCurrentFile(path);

    return trainee;
  }

  /**
   * Saves a trainee.
   *
   * @param path path to write to.
   * @param trainee Trainee to save
   * @throws JAXBException I/O error or parse error
   * @throws IOException I/O error
   */
  public void saveTrainee(Path path, Trainee trainee) throws JAXBException, IOException {
    LOGGER.trace("Called saveTrainee(path: {}, trainee: {})", path, trainee);

    TraineeDao.saveToPath(path, trainee);
    Install.getInstance().setCurrentFile(path);
  }
}
