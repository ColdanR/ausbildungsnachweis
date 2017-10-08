package de.ravenguard.ausbildungsnachweis.gui;

import de.ravenguard.ausbildungsnachweis.model.DataMonth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContentDataMonthController {
  private static final Logger LOGGER = LogManager.getLogger(ContentDataMonthController.class);
  private DataMonth dataMonth;

  /**
   * Returns the dataMonth instance.
   * 
   * @return the dataMonth
   */
  public DataMonth getDataMonth() {
    LOGGER.trace("Called getDataMonth()");
    return dataMonth;
  }

  /**
   * Sets the dataMonth instance.
   * 
   * @param dataMonth the dataMonth to set
   */
  public void setDataMonth(DataMonth dataMonth) {
    LOGGER.trace("Called setDataMonth(dataMonth: {})", dataMonth);
    this.dataMonth = dataMonth;
  }
}
