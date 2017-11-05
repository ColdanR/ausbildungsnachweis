package de.ravenguard.ausbildungsnachweis.utils;

import java.util.List;

public class ReportBean {
  private String headerPageType;
  private String headerPageYear;
  private String headerPageName;
  private String headerPageProfession;
  private String headerPagePeriod;
  private String pageType;
  private List<ReportContent> content;

  /**
   * Fields constructor.
   *
   * @param headerPageType header for type
   * @param headerPageYear header for year
   * @param headerPageName header for name
   * @param headerPageProfession header for profession
   * @param headerPagePeriod header for period
   * @param pageType type of page, must be "school" for school week
   * @param content content of list
   */
  public ReportBean(String headerPageType, String headerPageYear, String headerPageName,
      String headerPageProfession, String headerPagePeriod, String pageType,
      List<ReportContent> content) {
    super();
    this.headerPageType = headerPageType;
    this.headerPageYear = headerPageYear;
    this.headerPageName = headerPageName;
    this.headerPageProfession = headerPageProfession;
    this.headerPagePeriod = headerPagePeriod;
    this.pageType = pageType;
    this.content = content;
  }

  public List<ReportContent> getContent() {
    return content;
  }

  public String getHeaderPageName() {
    return headerPageName;
  }

  public String getHeaderPagePeriod() {
    return headerPagePeriod;
  }

  public String getHeaderPageProfession() {
    return headerPageProfession;
  }

  public String getHeaderPageType() {
    return headerPageType;
  }

  public String getHeaderPageYear() {
    return headerPageYear;
  }

  public String getPageType() {
    return pageType;
  }

  public void setContent(List<ReportContent> content) {
    this.content = content;
  }

  public void setHeaderPageName(String headerPageName) {
    this.headerPageName = headerPageName;
  }

  public void setHeaderPagePeriod(String headerPagePeriod) {
    this.headerPagePeriod = headerPagePeriod;
  }

  public void setHeaderPageProfession(String headerPageProfession) {
    this.headerPageProfession = headerPageProfession;
  }

  public void setHeaderPageType(String headerPageType) {
    this.headerPageType = headerPageType;
  }

  public void setHeaderPageYear(String headerPageYear) {
    this.headerPageYear = headerPageYear;
  }

  public void setPageType(String pageType) {
    this.pageType = pageType;
  }
}
