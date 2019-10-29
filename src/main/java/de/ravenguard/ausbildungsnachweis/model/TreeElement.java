package de.ravenguard.ausbildungsnachweis.model;

import java.util.List;

public interface TreeElement<A> {

  List<A> getChildren();

  String getTreeLabel();
}
