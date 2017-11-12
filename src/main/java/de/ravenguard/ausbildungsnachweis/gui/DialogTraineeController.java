package de.ravenguard.ausbildungsnachweis.gui;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogTraineeController {
  private static final Logger LOGGER = LogManager.getLogger(DialogTraineeController.class);
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
    LOGGER.trace("Called getBegin()");
    return begin.getValue();
  }

  public LocalDate getEnd() {
    LOGGER.trace("Called getEnd()");
    return end.getValue();
  }

  public String getFamilyName() {
    LOGGER.trace("Called getFamilyName()");
    return familyName.getText();
  }

  public String getGivenNames() {
    LOGGER.trace("Called getGivenNames()");
    return givenNames.getText();
  }

  public String getSchool() {
    LOGGER.trace("Called getSchool()");
    return school.getText();
  }

  public String getTrainer() {
    LOGGER.trace("Called getTrainer()");
    return trainer.getText();
  }

  public String getTraining() {
    LOGGER.trace("Called getTraining()");
    return training.getText();
  }

  public void setBegin(LocalDate begin) {
    LOGGER.trace("Called setBegin(begin: {})", begin);
    this.begin.setValue(begin);
  }

  public void setEnd(LocalDate end) {
    LOGGER.trace("Called setEnd(end: {})", end);
    this.end.setValue(end);
  }

  public void setFamilyName(String familyName) {
    LOGGER.trace("Called setFamilyName(familyName: {})", familyName);
    this.familyName.setText(familyName);
  }

  public void setGivenNames(String givenNames) {
    LOGGER.trace("Called setGivenNames(givenNames: {})", givenNames);
    this.givenNames.setText(givenNames);
  }

  public void setSchool(String school) {
    LOGGER.trace("Called setSchool(school: {})", school);
    this.school.setText(school);
  }

  public void setTrainer(String trainer) {
    LOGGER.trace("Called setTrainer(trainer: {})", trainer);
    this.trainer.setText(trainer);
  }

  public void setTraining(String training) {
    LOGGER.trace("Called setTraining(training: {})", training);
    this.training.setText(training);
  }
}
