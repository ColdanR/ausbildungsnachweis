package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataWeek implements Comparable<DataWeek> {
  private static final Logger LOGGER = LogManager.getLogger(DataWeek.class);
  private LocalDate begin;
  private LocalDate end;
  private WeekType type = WeekType.COMPANY;
  private String notes = "";
  private String contentCompany = "";
  @XmlElementWrapper(name = "schoolContents")
  @XmlElement(name = "contentSchool")
  private final List<ContentSchoolSubject> contentSchool = new ArrayList<>();

  public DataWeek() {
    LOGGER.trace("Called DataWeek()");
  }

  /**
   * Field Constructor.
   *
   * @param begin begin of week
   * @param end end of week
   * @throws IllegalDateException if begin or end is null or end is before begin
   */
  public DataWeek(LocalDate begin, LocalDate end) throws IllegalDateException {
    LOGGER.trace("Called DataWeek(begin: {}, end: {})", begin, end);
    setBegin(begin);
    setEnd(end);
  }

  /**
   * Add an entity for content school.
   *
   * @param content content to add, may not be null
   */
  public void addContentSchool(ContentSchoolSubject content) {
    LOGGER.trace("Called addContentSchool(content: {})", content);
    if (content == null) {
      throw new NullPointerException("Content may not be null.");
    }

    boolean conflict = false;
    for (final ContentSchoolSubject subject : contentSchool) {
      if (subject.getSubject().equals(content.getSubject())) {
        conflict = true;
        break;
      }
    }
    if (conflict) {
      throw new IllegalArgumentException("SchoolSubject already added.");
    }

    contentSchool.add(content);
  }

  @Override
  public int compareTo(DataWeek o) {
    LOGGER.trace("Called compareTo(o: {})", o);
    return begin.compareTo(o.getBegin());
  }

  public LocalDate getBegin() {
    LOGGER.trace("Called getBegin()");
    return begin;
  }

  public String getContentCompany() {
    LOGGER.trace("Called getBegin()");
    return contentCompany;
  }

  public List<ContentSchoolSubject> getContentSchool() {
    LOGGER.trace("Called getBegin()");
    return contentSchool;
  }

  public LocalDate getEnd() {
    LOGGER.trace("Called getBegin()");
    return end;
  }

  public String getNotes() {
    LOGGER.trace("Called getBegin()");
    return notes;
  }

  public WeekType getType() {
    LOGGER.trace("Called getBegin()");
    return type;
  }

  /**
   * Remove an entity for content school.
   *
   * @param content content to remove, may not be null
   */
  public void removeContentSchool(ContentSchoolSubject content) {
    LOGGER.trace("Called getBegin()");
    if (content == null) {
      throw new NullPointerException("Content may not be null.");
    }

    contentSchool.remove(content);
  }

  /**
   * Sets the begin of the week.
   *
   * @param begin begin of the week, may not be null or after the end of the week.
   * @throws IllegalDateException if begin is not a working day or after end
   */
  public void setBegin(LocalDate begin) throws IllegalDateException {
    LOGGER.trace("Called setBegin(begin: {})", begin);
    if (begin == null) {
      throw new NullPointerException("begin may not be null.");
    }
    if (!DateUtils.checkWorkday(begin)) {
      throw new IllegalDateException("begin is not a working day.");
    }
    if (end != null && begin.isAfter(end)) {
      throw new IllegalDateException("begin may not be before begin.");
    }

    this.begin = begin;
  }

  public void setContentCompany(String contentCompany) {
    LOGGER.trace("Called setBegin(begin: {})", begin);
    this.contentCompany = contentCompany;
  }

  /**
   * Sets the content of school.
   *
   * @param contentSchool content of school to set, may not be null
   */
  public void setContentSchool(List<ContentSchoolSubject> contentSchool) {
    LOGGER.trace("Called setBegin(begin: {})", begin);
    if (contentSchool == null) {
      throw new NullPointerException("ContentSchool of week may not be null.");
    }

    this.contentSchool.clear();
    for (final ContentSchoolSubject content : contentSchool) {
      addContentSchool(content);
    }
  }

  /**
   * Sets the end of the week.
   *
   * @param end End of the week, may not be null or before the begin of the week.
   * @throws IllegalDateException if end is not a working day or is before begin
   */
  public void setEnd(LocalDate end) throws IllegalDateException {
    LOGGER.trace("Called setEnd(end: {})", end);
    if (end == null) {
      throw new NullPointerException("end may not be null.");
    }
    if (!DateUtils.checkWorkday(end)) {
      throw new IllegalDateException("end is not a working day.");
    }
    if (begin != null && end.isBefore(begin)) {
      throw new IllegalDateException("end may not be after begin.");
    }

    this.end = end;
  }

  public void setNotes(String notes) {
    LOGGER.trace("Called setNotes(notes: {})", notes);
    this.notes = notes;
  }

  /**
   * Sets the type of week.
   *
   * @param type type to set, may not be null
   */
  public void setType(WeekType type) {
    LOGGER.trace("Called setType(type: {})", type);
    if (type == null) {
      throw new NullPointerException("Type may not be null.");
    }
    this.type = type;
  }

  @Override
  public String toString() {
    return "DataWeek [begin=" + begin + ", end=" + end + ", type=" + type + ", notes=" + notes
        + ", contentCompany=" + contentCompany + ", contentSchool=" + contentSchool + "]";
  }
}
