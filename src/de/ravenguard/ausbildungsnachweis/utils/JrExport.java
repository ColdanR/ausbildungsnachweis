package de.ravenguard.ausbildungsnachweis.utils;

import de.ravenguard.ausbildungsnachweis.logic.Configuration;
import de.ravenguard.ausbildungsnachweis.model.ContentSchoolSubject;
import de.ravenguard.ausbildungsnachweis.model.DataWeek;
import de.ravenguard.ausbildungsnachweis.model.WeekType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Helper class for Jasper Reports Operations.
 *
 * @author Bernd
 *
 */
public class JrExport {
  /**
   * Exports the data to pdf file.
   *
   * @param data data
   * @param destination file destination
   * @param name name of trainee
   * @param profession profession of trainee
   * @param year year label
   * @param period period label
   * @throws IOException IO error
   * @throws JRException JR error
   */
  public static void export(List<DataWeek> data, File destination, String name, String profession,
      String year, String period) throws IOException, JRException {
    final List<ReportBean> dataList = new ArrayList<>();
    final List<ReportContent> company = new ArrayList<>();

    for (final DataWeek week : data) {
      if (week.getType() == WeekType.COMPANY || Configuration.getInstance().isCompanyAndSchool()) {
        final String headLine = "Woche vom " + Utils.formatDate(week.getBegin()) + " bis " + "("
            + Utils.getWeekNumberFromDate(week.getBegin()) + ". KW)";
        final String content = week.getContentCompany();
        company.add(new ReportContent(headLine, content));
      }
      if (week.getType() == WeekType.SCHOOL) {
        final List<ReportContent> school = new ArrayList<>();
        for (final ContentSchoolSubject schoolContent : week.getContentSchool()) {
          final String headLine = schoolContent.getSubject().getLabel();
          String content = schoolContent.getContent();
          if (content == null) {
            content = "";
          } else {
            content = content.trim();
          }
          final LocalDate exemptSince = schoolContent.getSubject().getExemptSince();
          if (exemptSince != null && exemptSince.isBefore(week.getEnd())) {
            if (content.length() > 0) {
              content = content + System.lineSeparator() + System.lineSeparator() + "Befreit seit "
                  + Utils.formatDate(exemptSince);
            } else {
              content = "Befreit seit " + Utils.formatDate(exemptSince);
            }
          }
          school.add(new ReportContent(headLine, content));
        }
        dataList.add(
            new ReportBean("schulischer Teil", year, name, profession, period, "school", school));
      }
    }

    dataList.add(0,
        new ReportBean("betrieblicher Teil", year, name, profession, period, "company", company));


    try (
        InputStream sub = JrExport.class
            .getResourceAsStream("/de/ravenguard/ausbildungsnachweis/utils/Content.jrxml");
        InputStream main = JrExport.class
            .getResourceAsStream("/de/ravenguard/ausbildungsnachweis/utils/Seitentemplate.jrxml");
        InputStream lines = JrExport.class.getClassLoader()
            .getResourceAsStream("/de/ravenguard/ausbildungsnachweis/utils/Linien.jpg");
        InputStream logo = JrExport.class.getClassLoader()
            .getResourceAsStream("/de/ravenguard/ausbildungsnachweis/utils/Logo.png");) {
      final String destinationPath = destination.getAbsolutePath();

      final JasperReport masterReport = JasperCompileManager.compileReport(main);
      final JasperReport subReport = JasperCompileManager.compileReport(sub);

      final Map<String, Object> parameters = new HashMap<>();
      parameters.put("subReport", subReport);
      parameters.put("lines", lines);
      parameters.put("logo", logo);

      final JRDataSource dataSource = new JRBeanCollectionDataSource(dataList, false);

      final JasperPrint report = JasperFillManager.fillReport(masterReport, parameters, dataSource);

      JasperExportManager.exportReportToPdfFile(report, destinationPath);
    }
  }
}
