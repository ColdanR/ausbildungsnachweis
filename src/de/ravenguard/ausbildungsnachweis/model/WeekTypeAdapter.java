package de.ravenguard.ausbildungsnachweis.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class WeekTypeAdapter extends XmlAdapter<String, WeekType> {
  @Override
  public String marshal(WeekType v) throws Exception {
    if (v == null) {
      return null;
    }
    return v.toString();
  }

  @Override
  public WeekType unmarshal(String v) throws Exception {
    if (v == null) {
      return null;
    }
    return WeekType.valueOf(v);
  }
}
