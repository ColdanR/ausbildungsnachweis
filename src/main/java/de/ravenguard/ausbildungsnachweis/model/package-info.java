/**
 *
 */
/**
 * @author Bernd
 *
 */
@XmlJavaTypeAdapters({@XmlJavaTypeAdapter(value = LocalDateAdapter.class, type = LocalDate.class),
    @XmlJavaTypeAdapter(value = WeekTypeAdapter.class, type = WeekType.class)})
package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
