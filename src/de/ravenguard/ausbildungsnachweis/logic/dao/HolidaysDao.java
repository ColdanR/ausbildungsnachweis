package de.ravenguard.ausbildungsnachweis.logic.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HolidaysDao {
  @XmlRootElement
  public static class HolidayWrapper {
    private Collection<LocalDate> list = new ArrayList<>();

    public Collection<LocalDate> getList() {
      return list;
    }

    public void setList(Collection<LocalDate> list) {
      this.list = list;
    }
  }

  private static final Logger LOGGER = LogManager.getLogger(HolidaysDao.class);

  /**
   * Deletes the file at the path.
   *
   * @param holidayPath path to file
   * @throws IOException I/O error
   */
  public static void deletePath(Path holidayPath) throws IOException {
    LOGGER.trace("Called deletePath(holidayPath: {})", holidayPath);

    Files.deleteIfExists(holidayPath);
  }

  /**
   * Loads the Collection of legal holidays.
   *
   * @param holidayPath Path of XML file
   * @return Collection of legal holidays
   * @throws JAXBException Parse error
   */
  public static Collection<LocalDate> getFromPath(Path holidayPath) throws JAXBException {
    LOGGER.trace("Called readFromPath(holidayPath: {})", holidayPath);
    final JAXBContext jc = JAXBContext.newInstance(HolidayWrapper.class);
    final Unmarshaller u = jc.createUnmarshaller();
    final HolidayWrapper wrapper = (HolidayWrapper) u.unmarshal(holidayPath.toFile());

    return wrapper.getList();
  }

  /**
   * Saves a Collection of legal holidays to the path.
   *
   * @param holidayPath path of XML file
   * @param list list of legal holidays
   * @throws JAXBException parse error
   * @throws IOException I/O error
   */
  public static void saveToPath(Path holidayPath, Collection<LocalDate> list)
      throws JAXBException, IOException {
    LOGGER.trace("Called saveToPath(holidayPath: {}, list: {})", holidayPath, list);

    final HolidayWrapper wrapper = new HolidayWrapper();
    wrapper.setList(list);

    final JAXBContext jc = JAXBContext.newInstance(HolidayWrapper.class);
    final Marshaller m = jc.createMarshaller();
    final OutputStream os = Files.newOutputStream(holidayPath, StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

    m.marshal(wrapper, os);
  }
}
