package de.ravenguard.ausbildungsnachweis.gui.elements;

import de.ravenguard.ausbildungsnachweis.model.DataMonth;
import de.ravenguard.ausbildungsnachweis.model.TrainingPeriod;
import de.ravenguard.ausbildungsnachweis.model.TreeElement;
import javafx.scene.control.TreeItem;

public class TrainingPeriodWrapper extends TreeItem<TreeElement> {
  /**
   * Sets the value and adds the children elements.
   *
   * @param trainingPeriod value
   */
  public TrainingPeriodWrapper(TrainingPeriod trainingPeriod) {
    super(trainingPeriod);

    for (final DataMonth month : trainingPeriod.getMonths()) {
      getChildren().add(new DataMonthWrapper(month));
    }

    setExpanded(true);
  }

  @Override
  public String toString() {
    return getValue().getTreeLabel();
  }
}
