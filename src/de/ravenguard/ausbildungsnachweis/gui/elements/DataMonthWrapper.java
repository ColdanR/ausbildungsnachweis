package de.ravenguard.ausbildungsnachweis.gui.elements;

import de.ravenguard.ausbildungsnachweis.model.DataMonth;
import de.ravenguard.ausbildungsnachweis.model.TreeElement;
import javafx.scene.control.TreeItem;

public class DataMonthWrapper extends TreeItem<TreeElement> {
  public DataMonthWrapper(DataMonth dataMonth) {
    super(dataMonth);
  }

  @Override
  public String toString() {
    return getValue().getTreeLabel();
  }
}
