package com.sbnz.sbnzproject.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.sbnz.sbnzproject.model.Patient;

public interface PatientService {

	Patient create(Patient patient);

	Collection<Patient> getAll();

	Patient findById(Long id);
	
	Set<Patient> getWithChronic(String username);

	Set<Patient> getAddicts(String username);

	Set<Patient> getWithWeakImmunity(String username);

}
