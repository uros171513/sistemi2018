package com.sbnz.sbnzproject.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String lastName;
	
	@OneToMany(fetch = FetchType.EAGER)
	private Set<MedicalRecord> patientHistory = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Medicine> medicineAlergies = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<MedicineComponent> componentAlergies = new HashSet<>();

	public Patient() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Collection<MedicalRecord> getPatientHistory() {
		return patientHistory;
	}

	public void setPatientHistory(Set<MedicalRecord> patientHistory) {
		this.patientHistory = patientHistory;
	}

	public Collection<Medicine> getMedicineAlergies() {
		return medicineAlergies;
	}

	public void setMedicineAlergies(Set<Medicine> medicineAlergies) {
		this.medicineAlergies = medicineAlergies;
	}

	public Collection<MedicineComponent> getComponentAlergies() {
		return componentAlergies;
	}

	public void setComponentAlergies(Set<MedicineComponent> componentAlergies) {
		this.componentAlergies = componentAlergies;
	}
	
	
}