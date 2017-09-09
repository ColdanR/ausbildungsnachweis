package de.ravenguard.ausbildungsnachweis.model;

import java.time.LocalDate;
import java.util.List;

public class Trainee {
	private String familiyName;
	private String givenNames;
	private LocalDate begin;
	private LocalDate end;
	private String trainer;
	private String school;
	private List<TrainingPeriode> trainingPeriodes;

	public LocalDate getBegin() {
		return begin;
	}
	public LocalDate getEnd() {
		return end;
	}
	public String getFamiliyName() {
		return familiyName;
	}
	public String getGivenNames() {
		return givenNames;
	}
	public String getSchool() {
		return school;
	}
	public String getTrainer() {
		return trainer;
	}
	public List<TrainingPeriode> getTrainingPeriodes() {
		return trainingPeriodes;
	}
	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	public void setFamiliyName(String familiyName) {
		this.familiyName = familiyName;
	}
	public void setGivenNames(String givenNames) {
		this.givenNames = givenNames;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}
	public void setTrainingPeriodes(List<TrainingPeriode> trainingPeriodes) {
		this.trainingPeriodes = trainingPeriodes;
	}
}
