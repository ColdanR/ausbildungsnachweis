package de.ravenguard.ausbildungsnachweis.logic;

public enum InstallStatus {
  /** No installation found. */
  NOT_INSTALLED,
  /** No file for settings found. */
  NOT_FOUND_SETTINGS,
  /** Cannot parse settings file. */
  PARSE_ERROR_SETTINGS,
  /** Everything loaded and fine. */
  OK;
}
