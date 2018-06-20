package com.sbnz.sbnzproject.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class MedicalRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Disease disease;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Symptom> symptoms = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Medicine> medicine = new ArrayList<>();

	public MedicalRecord() {
		
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Disease getDisease() {
		return disease;
	}
	public void setDisease(Disease disease) {
		this.disease = disease;
	}
	public Collection<Symptom> getSymptoms() {
		return symptoms;
	}
	public void setSymptoms(Set<Symptom> symptoms) {
		this.symptoms = symptoms;
	}
	public Collection<Medicine> getMedicine() {
		return medicine;
	}
	public void setMedicine(Collection<Medicine> medicine) {
		this.medicine = medicine;
	}

	
	
}
