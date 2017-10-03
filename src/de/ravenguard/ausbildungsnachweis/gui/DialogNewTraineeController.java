package de.ravenguard.ausbildungsnachweis.gui;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class DialogNewTraineeController {
  @FXML
  private TextField familyName;
  @FXML
  private TextField givenNames;
  @FXML
  private TextField trainer;
  @FXML
  private TextField school;
  @FXML
  private TextField training;
  @FXML
  private DatePicker begin;
  @FXML
  private DatePicker end;

  public LocalDate getBegin() {
    return begin.getValue();
  }

  public LocalDate getEnd() {
    return end.getValue();
  }

  public String getFamilyName() {
    return familyName.getText();
  }

  public String getGivenNames() {
    return givenNames.getText();
  }

  public String getSchool() {
    return school.getText();
  }

  public String getTrainer() {
    return trainer.getText();
  }

  public String getTraining() {
    return training.getText();
  }

  public void setBegin(LocalDate begin) {
    this.begin.setValue(begin);
  }

  public void setEnd(LocalDate end) {
    this.end.setValue(end);
  }

  public void setFamilyName(String familyName) {
    this.familyName.setText(familyName);
  }

  public void setGivenNames(String givenNames) {
    this.givenNames.setText(givenNames);
  }

  public void setSchool(String school) {
    this.school.setText(school);
  }

  public void setTrainer(String trainer) {
    this.trainer.setText(trainer);
  }

  public void setTraining(String training) {
    this.training.setText(training);
  }
}
