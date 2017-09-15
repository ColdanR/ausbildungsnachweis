package test;

import de.ravenguard.ausbildungsnachweis.logic.dao.TraineeDao;
import de.ravenguard.ausbildungsnachweis.model.Trainee;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;

public class TextXml {
  public static void main(String[] args) throws JAXBException, IOException {

    final TrainingPeriod period = new TrainingPeriod("test Eintrag", LocalDate.of(2017, 9, 1),
        LocalDate.of(2018, 8, 31), "IFI10a", new ArrayList<>(), new ArrayList<>());
    final Trainee trainee = new Trainee("Schmidt", "Anika", LocalDate.of(2017, 9, 1),
        LocalDate.of(2020, 2, 28), "Herr Bla", "BS 1 Bayreuth", new ArrayList<>());
    trainee.addTrainingPeriode(period);
    final TrainingPeriod period2 = new TrainingPeriod("test Eintrag", LocalDate.of(2018, 9, 1),
        LocalDate.of(2019, 8, 31), "IFI10a", new ArrayList<>(), new ArrayList<>());
    trainee.addTrainingPeriode(period2);

    final Path path = Paths.get("D:", "temp", "text.xml");

    TraineeDao.saveToPath(path, trainee);
  }
}
