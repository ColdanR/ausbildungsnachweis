package de.ravenguard.ausbildungsnachweis.gui.elements;

import de.ravenguard.ausbildungsnachweis.model.Trainee;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import de.ravenguard.ausbildungsnachweis.model.TreeElement;
import javafx.scene.control.TreeItem;

public class TraineeWrapper extends TreeItem<TreeElement> {
  /**
   * Sets the value and adds the Childelements.
   *
   * @param trainee element
   */
  public TraineeWrapper(Trainee trainee) {
    super(trainee);

    for (final TrainingPeriod child : trainee.getTrainingPeriods()) {
      getChildren().add(new TrainingPeriodWrapper(child));
    }

    setExpanded(true);
  }

  @Override
  public String toString() {
    return getValue().getTreeLabel();
  }
}
