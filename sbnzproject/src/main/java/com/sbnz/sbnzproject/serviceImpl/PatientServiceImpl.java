package com.sbnz.sbnzproject.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.sbnz.sbnzproject.SbnzprojectApplication;
import com.sbnz.sbnzproject.model.DateChecker;
import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Medicine;
import com.sbnz.sbnzproject.model.MedicineType;
import com.sbnz.sbnzproject.model.Patient;
import com.sbnz.sbnzproject.model.User;
import com.sbnz.sbnzproject.repository.DiseaseRepository;
import com.sbnz.sbnzproject.repository.MedicineRepository;
import com.sbnz.sbnzproject.repository.PatientRepository;
import com.sbnz.sbnzproject.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

	@Autowired
	PatientRepository patientRepository;

	@Autowired
	DiseaseRepository diseaseRepository;
	
	@Autowired
	MedicineRepository medicineRepository;
	
	private final KieContainer kieContainer;

	@Override
	public Patient create(Patient patient) {
		return patientRepository.save(patient);
	}

	@Override
	public Collection<Patient> getAll() {
		return patientRepository.findAll();
	}

	@Override
	public Patient findById(Long id) {
		return patientRepository.getOne(id);
	}


	@Autowired
	public PatientServiceImpl(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}

	@Override
	public Set<Patient> getWithChronic(String username) {

		KieSession kieSession = SbnzprojectApplication.kieSessions.get("kieSession-"+username);
		System.err.println(kieSession);
		if(kieSession==null) {
			KieServices ks = KieServices.Factory.get();
			KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
			kbconf.setOption(EventProcessingOption.STREAM);
			KieBase kbase = kieContainer.newKieBase(kbconf);
			kieSession = kbase.newKieSession();
		}
		
		List<Patient> patients=patientRepository.findAll();
		for(Patient p:patients) {
			kieSession.insert(p);
		}
		
//		List<Disease> diseases=diseaseRepository.findAll();
//		for(Disease d:diseases) {
//			kieSession.insert(d);
//		}

		kieSession.insert(new DateChecker());
		
		QueryResults results = kieSession.getQueryResults( "patients with chronic disease", "list");
		System.out.println( "we have " + results.size());

		ArrayList<Patient> foundPatients=new ArrayList<>();
		ArrayList<Disease> foundDiseases=new ArrayList<>();
		for ( QueryResultsRow row : results ) {
		
		 	Patient p = ( Patient ) row.get( "p" );
		    Disease d = ( Disease ) row.get( "d" );
		
			Collection<Long> longs = ( Collection<Long> ) row.get( "list" );
			System.err.println(longs.size());
			foundPatients.add(p);
			foundDiseases.add(d);
		}
		Set<Patient> uniquePatients = new HashSet<Patient>(foundPatients);
		release(kieSession);
		return uniquePatients;
	}

	public void release(KieSession kieSession) {
		for (Object object : kieSession.getObjects()) {
			if(!object.getClass().equals(Disease.class))
				kieSession.delete(kieSession.getFactHandle(object));
		}
	}

	@Override
	public Set<Patient> getAddicts(String username) {

		KieSession kieSession = SbnzprojectApplication.kieSessions.get("kieSession-"+username);
		System.err.println("sesija "+kieSession);
		if(kieSession==null) {
			KieServices ks = KieServices.Factory.get();
			KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
			kbconf.setOption(EventProcessingOption.STREAM);
			KieBase kbase = kieContainer.newKieBase(kbconf);
			kieSession = kbase.newKieSession();
		}
		
		List<Patient> patients=patientRepository.findAll();
		for(Patient p:patients) {
			kieSession.insert(p);
		}
		kieSession.insert(new DateChecker());
		
		List<Medicine> medicines=medicineRepository.findAll();
		List<Medicine> analgetics=new ArrayList<>();
		for(Medicine m: medicines) {
			if(m.getMedicineType().equals(MedicineType.ANALGETIC))
				kieSession.insert(m);
		}
		QueryResults results = kieSession.getQueryResults( "addicts" );
		System.out.println( "we have " + results.size());

		ArrayList<Patient> foundPatients=new ArrayList<>();
		for ( QueryResultsRow row : results ) {
		 	Patient p = ( Patient ) row.get( "p" );
			foundPatients.add(p);
		}
		release(kieSession);
		Set<Patient> uniquePatients = new HashSet<Patient>(foundPatients);
		return uniquePatients;
	}
	

	@Override
	public Set<Patient> getWithWeakImmunity(String username) {

		KieSession kieSession = SbnzprojectApplication.kieSessions.get("kieSession-"+username);
		System.err.println("sesija "+kieSession);
		if(kieSession==null) {
			KieServices ks = KieServices.Factory.get();
			KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
			kbconf.setOption(EventProcessingOption.STREAM);
			KieBase kbase = kieContainer.newKieBase(kbconf);
			kieSession = kbase.newKieSession();
		}
		
		List<Patient> patients=patientRepository.findAll();
		for(Patient p:patients) {
			kieSession.insert(p);
		}
		kieSession.insert(new DateChecker());

		QueryResults results = kieSession.getQueryResults( "weak immunity" );
		System.out.println( "we have " + results.size());

		ArrayList<Patient> foundPatients=new ArrayList<>();
		for ( QueryResultsRow row : results ) {
		 	Patient p = ( Patient ) row.get( "p" );
		 	Collection<Medicine> med=(Collection<Medicine>) row.get("medicinesList");
		 	System.err.println("medlist "+med.size());
		 	Collection<Medicine> ant=(Collection<Medicine>) row.get("antibioticsList");
		 	System.err.println("antlist "+ant.size());
		 	
			foundPatients.add(p);
		}
		release(kieSession);
		Set<Patient> uniquePatients = new HashSet<Patient>(foundPatients);
		return uniquePatients;
	}

}
