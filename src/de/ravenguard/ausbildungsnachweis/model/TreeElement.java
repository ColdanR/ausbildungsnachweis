package de.ravenguard.ausbildungsnachweis.model;

import java.util.List;

public interface TreeElement {
  List<? extends TreeElement> getChildren();

  String getTreeLabel();
}
