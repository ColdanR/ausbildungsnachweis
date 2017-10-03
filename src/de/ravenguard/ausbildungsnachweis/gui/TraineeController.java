package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.gui.elements.AlertException;
import de.ravenguard.ausbildungsnachweis.model.IllegalDateException;
import de.ravenguard.ausbildungsnachweis.model.Trainee;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class TraineeController {
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

  private Trainee trainee;

  public Trainee getTrainee() {
    return trainee;
  }

  /**
   * Sets the trainee and binds the values.
   *
   * @param trainee trainee parameter
   */
  public void setTrainee(Trainee trainee) {
    familyName.setText(trainee.getFamilyName());
    givenNames.setText(trainee.getGivenNames());
    trainer.setText(trainee.getTrainer());
    school.setText(trainee.getSchool());
    training.setText(trainee.getTraining());
    begin.setValue(trainee.getBegin());
    end.setValue(trainee.getEnd());

    familyName.textProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue == null || newValue.trim().length() == 0) {
        Utils.createErrorMessage("Der Familienname darf nicht leer sein.");
        return;
      }
      trainee.setFamilyName(newValue);
    });
    givenNames.textProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue == null || newValue.trim().length() == 0) {
        Utils.createErrorMessage("Vorname(n) darf nicht leer sein.");
        return;
      }
      trainee.setGivenNames(newValue);
    });
    trainer.textProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue == null || newValue.trim().length() == 0) {
        Utils.createErrorMessage("Der Ausbilder darf nicht leer sein.");
        return;
      }
      trainee.setTrainer(newValue);
    });
    school.textProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue == null || newValue.trim().length() == 0) {
        Utils.createErrorMessage("Die Berufsschule darf nicht leer sein.");
        return;
      }
      trainee.setSchool(newValue);
    });
    training.textProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue == null || newValue.trim().length() == 0) {
        Utils.createErrorMessage("Der Ausbildungsberuf darf nicht leer sein.");
        return;
      }
      trainee.setTraining(newValue);
    });
    begin.valueProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue == null) {
        Utils.createErrorMessage("Der Beginn kann nicht leer sein.");
        return;
      } else if (trainee.getEnd().isBefore(newValue)) {
        Utils.createErrorMessage("Der Beginn kann nicht nach dem Ende sein.");
        return;
      }
      try {
        trainee.setBegin(newValue);
      } catch (final IllegalDateException e) {
        new AlertException(e).show();
      }
    });
    end.valueProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue == null) {
        Utils.createErrorMessage("Das Ende kann nicht leer sein.");
        return;
      } else if (trainee.getBegin().isAfter(newValue)) {
        Utils.createErrorMessage("Das Ende kann nicht nach vo dem Beginn sein.");
        return;
      }
      try {
        trainee.setEnd(newValue);
      } catch (final IllegalDateException e) {
        new AlertException(e).show();
      }
    });
  }
}
