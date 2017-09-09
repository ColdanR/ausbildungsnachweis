package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;
import java.util.List;

public class DataMonth {
	private LocalDate begin;
	private List<DataWeek> weeks;
	private String notes;

	public LocalDate getBegin() {
		return begin;
	}
	public String getNotes() {
		return notes;
	}
	public List<DataWeek> getWeeks() {
		return weeks;
	}
	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public void setWeeks(List<DataWeek> weeks) {
		this.weeks = weeks;
	}
}
