package de.ravenguard.ausbildungsnachweis.utils;

public class ReportContent {
  private String headLine;
  private String content;

  /**
   * Field Constructor.
   * 
   * @param headLine headline to print
   * @param content content to print
   */
  public ReportContent(String headLine, String content) {
    super();
    this.headLine = headLine;
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public String getHeadLine() {
    return headLine;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setHeadLine(String headLine) {
    this.headLine = headLine;
  }
}
