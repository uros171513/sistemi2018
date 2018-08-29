package com.sbnz.sbnzproject.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.sbnz.sbnzproject.model.Patient;

public interface PatientService {

	Patient create(Patient patient);

	Collection<Patient> getAll();

	Patient findById(Long id);
	
	ArrayList<Patient> getWithChronic(String username);

	ArrayList<Patient> getAddicts(String username);

	ArrayList<Patient> getWithWeakImmunity(String username);

}
