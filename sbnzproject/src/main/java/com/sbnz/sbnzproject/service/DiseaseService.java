package com.sbnz.sbnzproject.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Patient;
import com.sbnz.sbnzproject.model.Symptom;

public interface DiseaseService {

	Disease findById(Long id);
	
	Disease findByName(String name);

	Disease create(Disease d);

	Collection<Disease> getAll();

	Disease delete(Long id);

	MedicalRecord diseaseBySymptoms(Long id, ArrayList<Symptom> symptoms, String username);

	ArrayList<Disease> getDiseasesBySymptoms(ArrayList<Symptom> symptoms, String username);

}
