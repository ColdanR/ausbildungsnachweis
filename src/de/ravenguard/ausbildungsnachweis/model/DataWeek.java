package de.ravenguard.ausbildungsnachweis.model;

import de.ravenguard.ausbildungsnachweis.utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataWeek {
  private LocalDate begin;
  private LocalDate end;
  private WeekType type = WeekType.COMPANY;
  private String notes = "";
  private String contentCompany = "";
  @XmlElementWrapper(name = "schoolContents")
  @XmlElement(name = "contentSchool")
  private final List<ContentSchoolSubject> contentSchool = new ArrayList<>();

  public DataWeek() {}

  /**
   * Field Constructor.
   *
   * @param begin begin of week
   * @param end end of week
   */
  public DataWeek(LocalDate begin, LocalDate end) {
    super();
    setBegin(begin);
    setEnd(end);
  }

  /**
   * Add an entity for content school.
   *
   * @param content content to add, may not be null
   */
  public void addContentSchool(ContentSchoolSubject content) {
    if (content == null) {
      throw new IllegalArgumentException("Content may not be null.");
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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DataWeek other = (DataWeek) obj;
    if (begin == null) {
      if (other.begin != null) {
        return false;
      }
    } else if (!begin.equals(other.begin)) {
      return false;
    }
    if (contentCompany == null) {
      if (other.contentCompany != null) {
        return false;
      }
    } else if (!contentCompany.equals(other.contentCompany)) {
      return false;
    }
    if (contentSchool == null) {
      if (other.contentSchool != null) {
        return false;
      }
    } else if (!contentSchool.equals(other.contentSchool)) {
      return false;
    }
    if (end == null) {
      if (other.end != null) {
        return false;
      }
    } else if (!end.equals(other.end)) {
      return false;
    }
    if (notes == null) {
      if (other.notes != null) {
        return false;
      }
    } else if (!notes.equals(other.notes)) {
      return false;
    }
    if (type != other.type) {
      return false;
    }
    return true;
  }

  public LocalDate getBegin() {
    return begin;
  }

  public String getContentCompany() {
    return contentCompany;
  }

  public List<ContentSchoolSubject> getContentSchool() {
    return contentSchool;
  }

  public LocalDate getEnd() {
    return end;
  }

  public String getNotes() {
    return notes;
  }

  public WeekType getType() {
    return type;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (begin == null ? 0 : begin.hashCode());
    result = prime * result + (contentCompany == null ? 0 : contentCompany.hashCode());
    result = prime * result + (contentSchool == null ? 0 : contentSchool.hashCode());
    result = prime * result + (end == null ? 0 : end.hashCode());
    result = prime * result + (notes == null ? 0 : notes.hashCode());
    result = prime * result + (type == null ? 0 : type.hashCode());
    return result;
  }

  /**
   * Remove an entity for content school.
   *
   * @param content content to remove, may not be null
   */
  public void removeContentSchool(ContentSchoolSubject content) {
    if (content == null) {
      throw new IllegalArgumentException("Content may not be null.");
    }

    contentSchool.remove(content);
  }

  /**
   * Sets the begin of the week.
   *
   * @param begin begin of the week, may not be null or after the end of the week.
   */
  public void setBegin(LocalDate begin) {
    DateUtils.checkDate(begin);
    DateUtils.checkDate(begin, end);

    this.begin = begin;
  }

  public void setContentCompany(String contentCompany) {
    this.contentCompany = contentCompany;
  }

  /**
   * Sets the content of school.
   *
   * @param contentSchool content of school to set, may not be null
   */
  public void setContentSchool(List<ContentSchoolSubject> contentSchool) {
    if (contentSchool == null) {
      throw new IllegalArgumentException("ContentSchool of week may not be null.");
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
   */
  public void setEnd(LocalDate end) {
    DateUtils.checkDate(end);
    DateUtils.checkDate(begin, end);

    this.end = end;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * Sets the type of week.
   *
   * @param type type to set, may not be null
   */
  public void setType(WeekType type) {
    if (type == null) {
      throw new IllegalArgumentException("Type may not be null.");
    }
    this.type = type;
  }

  @Override
  public String toString() {
    return "DataWeek [begin=" + begin + ", end=" + end + ", type=" + type + ", notes=" + notes
        + ", contentCompany=" + contentCompany + ", contentSchool=" + contentSchool + "]";
  }
}
