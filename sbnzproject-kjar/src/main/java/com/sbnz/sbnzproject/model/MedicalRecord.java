package com.sbnz.sbnzproject.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
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

	@Column
	private Date date;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean last60Days() {
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_MONTH, -60);
		Date res = cal.getTime();

		if(this.date!=null) {
			if (res.before(this.date)) {
				return true;
			}
		}
		return false;
	}

	public boolean last6Months() {
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(now);
		cal.add(Calendar.MONTH, -6);
		Date res = cal.getTime();

		if(this.date!=null) {
			if (res.before(this.date)) {
				return true;
			}
		}
		return false;
	}

	public boolean moreThan6Months() {
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(now);
		cal.add(Calendar.MONTH, -6);
		Date res = cal.getTime();

		if(this.date!=null) {
			if (res.after(this.date)) {
				return true;
			}
		}	
		return false;
	}

	public boolean last21Days() {
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_MONTH, -21);
		Date res = cal.getTime();

		if(this.date!=null) {
			if (res.before(this.date)) {
				return true;
			}
		}
		return false;
	}

	public boolean last14Days() {
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_MONTH, -14);
		Date res = cal.getTime();

		if(this.date!=null){
			if (res.before(this.date)) {
				return true;
			}
		}
		return false;
	}

}
