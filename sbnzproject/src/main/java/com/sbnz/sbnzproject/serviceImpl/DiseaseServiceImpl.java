package com.sbnz.sbnzproject.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.sbnzproject.SbnzprojectApplication;
import com.sbnz.sbnzproject.model.DateChecker;
import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Patient;
import com.sbnz.sbnzproject.model.SalienceDiseaseChecker;
import com.sbnz.sbnzproject.model.SalienceDiseaseCheckerSinInf;
import com.sbnz.sbnzproject.model.Symptom;
import com.sbnz.sbnzproject.repository.DiseaseRepository;
import com.sbnz.sbnzproject.repository.PatientRepository;
import com.sbnz.sbnzproject.repository.SymptomRepository;
import com.sbnz.sbnzproject.service.DiseaseService;

@Service
public class DiseaseServiceImpl implements DiseaseService {

	@Autowired
	DiseaseRepository diseaseRepository;

	@Autowired
	SymptomRepository symptomRepository;
	
	@Autowired
	PatientRepository patientRepository;

	@Override
	public Disease findById(Long id) {
		return diseaseRepository.findOne(id);
	}
	
	@Override
	public Disease findByName(String name) {
		return diseaseRepository.findByName(name);
	}

	@Override
	public Disease create(Disease d) {
		return diseaseRepository.save(d);
	}

	@Override
	public Collection<Disease> getAll() {
		return diseaseRepository.findAll();
	}

	@Override
	public Disease delete(Long id) {
		Disease disease = diseaseRepository.findOne(id);
		diseaseRepository.deleteDiseaseById(id);
		return disease;
	}
	

	private final KieContainer kieContainer;
	
    @Autowired
    public DiseaseServiceImpl(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }
	
	@Override
	public MedicalRecord diseaseBySymptoms(Long id, ArrayList<Symptom> symptoms, String username) {

		KieSession kieSession = SbnzprojectApplication.kieSessions.get("kieSession-"+username);
		System.err.println(kieSession);
		if(kieSession==null) {
			KieServices ks = KieServices.Factory.get();
			KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
			kbconf.setOption(EventProcessingOption.STREAM);
			KieBase kbase = kieContainer.newKieBase(kbconf);
			kieSession = kbase.newKieSession();
		}
		
		kieSession.insert(new DateChecker());
		kieSession.insert(new SalienceDiseaseChecker());
		kieSession.insert(new SalienceDiseaseCheckerSinInf());
		
		Patient patient=patientRepository.findOne(id);
		
		//uzimanje simptoma iz baze
		ArrayList<Symptom> allSymptoms=(ArrayList<Symptom>) symptomRepository.findAll();
		ArrayList<Symptom> patientSymptoms=new ArrayList<>();
		for(Symptom s:allSymptoms) {
			for(Symptom s1:symptoms) {
				if(s.getName().equals(s1.getName())) {
					patientSymptoms.add(s1);
				}
					
			}
		}
		
		kieSession.insert(patient);
		
		//dodati medicalRecord-e od tog pacijenta
		Collection<MedicalRecord> records= patient.getPatientHistory();
		for(MedicalRecord mr:records) {
			kieSession.insert(mr);
		}
			
		
		//dodavanje medicalRecord sa tim simptomima i dodavanje svih bolesti
		MedicalRecord medicalRecord=new MedicalRecord();
		if(true) {
//			ArrayList<Disease> diseases = (ArrayList<Disease>) getAll();
			if(patientSymptoms.equals(null)) {
				System.err.println("yesss");
				medicalRecord.getSymptoms().addAll(new ArrayList<Symptom>());
			}
			else {
				for(Symptom s:patientSymptoms)
					System.err.println(s.getName());
				medicalRecord.getSymptoms().addAll(patientSymptoms);
			}
			medicalRecord.setDisease(null);
			kieSession.insert(medicalRecord);
//			for (Disease d:diseases) {
//				kieSession.insert(d);
//			}
		}
		
		
		//focus na tu grupu pravila
		kieSession.getAgenda().getAgendaGroup("diseases-group").setFocus();
		kieSession.fireAllRules();
			
		release(kieSession);
		System.err.println("Pronasli smo bolest: ");
		System.err.println(medicalRecord.getDisease());
		return medicalRecord;
	}

	public void release(KieSession kieSession){
		for (Object object : kieSession.getObjects()) {
			if(!object.getClass().equals(Disease.class))
				kieSession.delete(kieSession.getFactHandle(object));
		}
	}
	
	@Override
	public ArrayList<Disease> getDiseasesBySymptoms(ArrayList<Symptom> symptoms, String username) {
		KieSession kieSession = SbnzprojectApplication.kieSessions.get("kieSession-"+username);
		System.err.println(kieSession);
		if(kieSession==null) {
			KieServices ks = KieServices.Factory.get();
			KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
			kbconf.setOption(EventProcessingOption.STREAM);
			KieBase kbase = kieContainer.newKieBase(kbconf);
			kieSession = kbase.newKieSession();
		}
		kieSession.insert(new DateChecker());
//		List<Disease> diseases=diseaseRepository.findAll();
//		for(Disease d:diseases)
//			kieSession.insert(d);
		QueryResults results = kieSession.getQueryResults( "diseases containing symptoms", new Object[] { symptoms } );
		System.out.println( "we have " + results.size());

		Map<Disease, Long> map = new HashMap<>();
		for ( QueryResultsRow row : results ) {
		    Disease disease = ( Disease ) row.get("disease");
		    Long num = (Long) row.get("numOfSym");
		    map.put(disease, num);
		    System.err.println("Disease: "+disease.getName()+"  numOfSym: "+num);
		}
		
		Map<Disease, Long> result = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		ArrayList<Disease> relatedDiseases= new ArrayList<>();
		for(Disease d: result.keySet()) {
			relatedDiseases.add(d);
		}
		release(kieSession);
		return relatedDiseases;
	}
}
