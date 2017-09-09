package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;

public class SchoolSubject {
	private String label;
	private LocalDate exemptSince;

	public LocalDate getExemptSince() {
		return exemptSince;
	}
	public String getLabel() {
		return label;
	}
	public void setExemptSince(LocalDate exemptSince) {
		this.exemptSince = exemptSince;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
