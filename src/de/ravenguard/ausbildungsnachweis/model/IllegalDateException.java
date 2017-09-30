package de.ravenguard.ausbildungsnachweis.model;

public class IllegalDateException extends Exception {
  private static final long serialVersionUID = -2883147698978917211L;

  public IllegalDateException(String string) {
    super(string);
  }
}
