package de.ravenguard.ausbildungsnachweis.utils;

import java.util.List;
import java.util.stream.Collectors;

public class TransformUtils {
  public static String transformErrorList(List<String> errors) {
    return errors.stream().collect(Collectors.joining(System.lineSeparator()));
  }
}
