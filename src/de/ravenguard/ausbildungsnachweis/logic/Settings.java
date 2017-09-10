package de.ravenguard.ausbildungsnachweis.logic;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Settings {
  private boolean saturdayWorkday;
  private String defaultStorage;

  public String getDefaultStorage() {
    return defaultStorage;
  }

  public boolean isSaturdayWorkday() {
    return saturdayWorkday;
  }

  public void setDefaultStorage(String defaultStorage) {
    this.defaultStorage = defaultStorage;
  }

  public void setSaturdayWorkday(boolean saturdayWorkday) {
    this.saturdayWorkday = saturdayWorkday;
  }
}
