package de.ravenguard.ausbildungsnachweis.logic;

import de.ravenguard.ausbildungsnachweis.logic.dao.TraineeDao;
import de.ravenguard.ausbildungsnachweis.model.DataMonth;
import de.ravenguard.ausbildungsnachweis.model.DataWeek;
import de.ravenguard.ausbildungsnachweis.model.Trainee;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TraineeLogic {
  private static final Logger LOGGER = LogManager.getLogger(TraineeLogic.class);

  /**
   * Creates a new {@link TrainingPeriod} and creates the {@link DataMonth} and {@link DataWeek}.
   *
   * @param label label of the TrainingPeriod, may not be null or empty
   * @param begin begin of the TrainingPeriod, may not be null
   * @param end end of the TrainingPeriod, may not be null
   * @param schoolClass schoolClass of the TrainingPeriod, may not be null or empty
   * @param classTeacher class teacher of the period, may not be null or empty
   * @param trainee {@link Trainee} to add the new TrainingPeriode
   */
  public void addTrainingPeriode(String label, LocalDate begin, LocalDate end, String schoolClass,
      String classTeacher, Trainee trainee) {

    // Operation 1
    final List<DataMonth> months = new ArrayList<>();

    // Operation and validation
    final TrainingPeriod period =
        new TrainingPeriod(label, begin, end, schoolClass, classTeacher, months, new ArrayList<>());
    trainee.addTrainingPeriode(period);
  }

  /**
   * Creates a new Trainee Instance.
   *
   * @param familiyName family name, may not be null or empty
   * @param givenNames given name(s), may not be null or empty
   * @param begin begin of training, may not be null
   * @param end end of training, may not be null
   * @param trainer trainer name, may not be null or empty
   * @param school school name, may not be null or empty
   * @param training training, may not be null or empty
   * @return new Trainee instance
   * @throws IllegalArgumentException if one parameter is null or a string is empty
   */
  public Trainee create(String familiyName, String givenNames, LocalDate begin, LocalDate end,
      String trainer, String school, String training) {
    LOGGER.trace(
        "Called create(familiyName: {}, givenNames: {}, begin: {}, end: {}, "
            + "trainer: {}, school: {}, training: {}",
        familiyName, givenNames, begin, end, trainer, school, training);

    // Operation and validation
    return new Trainee(familiyName, givenNames, begin, end, trainer, school, training,
        new ArrayList<>());
  }

  /**
   * Deletes a Trainee on the path.
   *
   * @param path path to the trainee
   * @throws IOException I/O error
   */
  public void deleteTrainee(Path path) throws IOException {
    LOGGER.trace("Called deleteTrainee(path: {})", path);

    // Validation
    if (path == null || path.toString().trim().length() == 0) {
      throw new IllegalArgumentException("Path may not be null or empty.");
    }

    // Operation
    TraineeDao.deletePath(path);
    if (Configuration.getInstance().getCurrentFile().equals(path)) {
      Configuration.getInstance().setCurrentFile(null);
    }
  }

  /**
   * Reads a trainee and returns it, after saving the path of it.
   *
   * @param path path to read from, may not be null or empty
   * @return Trainee instance
   * @throws JAXBException I/O error or parse error
   */
  public Trainee readTrainee(Path path) throws JAXBException {
    LOGGER.trace("Called readTrainee(path: {})", path);

    // Validation
    if (path == null || path.toString().trim().length() == 0) {
      throw new IllegalArgumentException("Path may not be null or empty.");
    }

    // Operation
    final Trainee trainee = TraineeDao.getFromPath(path);
    Configuration.getInstance().setCurrentFile(path);

    return trainee;
  }

  /**
   * Saves a trainee.
   *
   * @param path path to write to, may not be null or empty.
   * @param trainee Trainee to save, may not be null.
   * @throws JAXBException I/O error or parse error
   * @throws IOException I/O error
   */
  public void saveTrainee(Path path, Trainee trainee) throws JAXBException, IOException {
    LOGGER.trace("Called saveTrainee(path: {}, trainee: {})", path, trainee);

    // Validation
    if (path == null || path.toString().trim().length() == 0) {
      throw new IllegalArgumentException("Path may not be null or empty.");
    }
    if (trainee == null) {
      throw new IllegalArgumentException("Trainee may not be null.");
    }

    // Operation
    TraineeDao.saveToPath(path, trainee);
    Configuration.getInstance().setCurrentFile(path);
  }
}
