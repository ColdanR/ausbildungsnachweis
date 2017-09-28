package de.ravenguard.ausbildungsnachweis.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Settings {
  private boolean saturdayWorkday;
  private boolean sundayWorkday;
  private boolean companyAndSchool;

  public boolean isCompanyAndSchool() {
    return companyAndSchool;
  }

  public boolean isSaturdayWorkday() {
    return saturdayWorkday;
  }

  public boolean isSundayWorkday() {
    return sundayWorkday;
  }

  public void setCompanyAndSchool(boolean companyAndSchool) {
    this.companyAndSchool = companyAndSchool;
  }

  public void setSaturdayWorkday(boolean saturdayWorkday) {
    this.saturdayWorkday = saturdayWorkday;
  }

  public void setSundayWorkday(boolean sundayWorkday) {
    this.sundayWorkday = sundayWorkday;
  }
}
