package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
  @Override
  public String marshal(LocalDate v) throws Exception {
    if (v == null) {
      return null;
    }
    return v.toString();
  }

  @Override
  public LocalDate unmarshal(String v) throws Exception {
    if (v == null) {
      return null;
    }
    return LocalDate.parse(v);
  }
}
