package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;
import java.util.List;

public class DataWeek {
	private LocalDate begin;
	private WeekType type;
	private List<ContentSchoolSubject> contentSchool;
	private String notes;
	private List<String> contentCompany;

	public LocalDate getBegin() {
		return begin;
	}
	public List<String> getContentCompany() {
		return contentCompany;
	}
	public List<ContentSchoolSubject> getContentSchool() {
		return contentSchool;
	}
	public String getNotes() {
		return notes;
	}
	public WeekType getType() {
		return type;
	}
	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}
	public void setContentCompany(List<String> contentCompany) {
		this.contentCompany = contentCompany;
	}
	public void setContentSchool(List<ContentSchoolSubject> contentSchool) {
		this.contentSchool = contentSchool;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public void setType(WeekType type) {
		this.type = type;
	}
}
