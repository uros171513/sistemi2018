package com.sbnz.sbnzproject.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.Symptom;

public interface SymptomService {

	Symptom findById(Long id);

	Symptom create(Symptom s);

	Collection<Symptom> getAll();

	Symptom delete(Long id);

	ArrayList<Symptom> symptomsByDisease(Disease disease, HttpServletRequest request);

}
