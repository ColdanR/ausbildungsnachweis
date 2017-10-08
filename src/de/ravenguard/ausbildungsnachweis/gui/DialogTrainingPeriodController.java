package de.ravenguard.ausbildungsnachweis.gui;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class DialogTrainingPeriodController {
  @FXML
  private TextField label;
  @FXML
  private TextField schoolClass;
  @FXML
  private TextField classTeacher;
  @FXML
  private DatePicker begin;
  @FXML
  private DatePicker end;

  public LocalDate getBegin() {
    return begin.getValue();
  }

  public String getClassTeacher() {
    return classTeacher.getText();
  }

  public LocalDate getEnd() {
    return end.getValue();
  }

  public String getLabel() {
    return label.getText();
  }

  public String getSchoolClass() {
    return schoolClass.getText();
  }

  public void setBegin(LocalDate begin) {
    this.begin.setValue(begin);
  }

  public void setClassTeacher(String classTeacher) {
    this.classTeacher.setText(classTeacher);
  }

  public void setEnd(LocalDate end) {
    this.end.setValue(end);
  }

  public void setLabel(String label) {
    this.label.setText(label);
  }

  public void setSchoolClass(String schoolClass) {
    this.schoolClass.setText(schoolClass);
  }
}
