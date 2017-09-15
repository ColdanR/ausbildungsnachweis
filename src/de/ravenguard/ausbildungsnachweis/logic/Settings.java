package de.ravenguard.ausbildungsnachweis.logic;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Settings {
  private boolean saturdayWorkday;
  private boolean sundayWorkday;
  private String defaultStorage;

  public String getDefaultStorage() {
    return defaultStorage;
  }

  public boolean isSaturdayWorkday() {
    return saturdayWorkday;
  }

  public boolean isSundayWorkday() {
    return sundayWorkday;
  }

  public void setDefaultStorage(String defaultStorage) {
    this.defaultStorage = defaultStorage;
  }

  public void setSaturdayWorkday(boolean saturdayWorkday) {
    this.saturdayWorkday = saturdayWorkday;
  }

  public void setSundayWorkday(boolean sundayWorkday) {
    this.sundayWorkday = sundayWorkday;
  }
}
