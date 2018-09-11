package com.sbnz.sbnzproject.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.drools.core.ClassObjectFilter;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.sbnzproject.SbnzprojectApplication;
import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.Medicine;
import com.sbnz.sbnzproject.model.MedicineComponent;
import com.sbnz.sbnzproject.model.Patient;
import com.sbnz.sbnzproject.repository.MedicineRepository;
import com.sbnz.sbnzproject.repository.PatientRepository;
import com.sbnz.sbnzproject.service.MedicineService;

@Service
public class MedicineServiceImpl implements MedicineService {

	@Autowired
	MedicineRepository medicineRepository;

	@Override
	public Medicine create(Medicine medicine) {
		return medicineRepository.save(medicine);
	}

	@Override
	public Medicine delete(Long id) {
		Medicine medicine = medicineRepository.findOne(id);
		medicineRepository.deleteMedicineById(id);
		return medicine;
	}

	@Override
	public Collection<Medicine> getAll() {
		return medicineRepository.findAll();
	}

	@Override
	public Medicine findById(Long id) {
		return medicineRepository.getOne(id);
	}

	private final KieContainer kieContainer;
	
    @Autowired
    public MedicineServiceImpl(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }
	
    @Autowired
    PatientRepository patientRepository;
	
	@Override
	public boolean checkAllergies(Long id, ArrayList<Medicine> medicines, String username) {
		KieSession kieSession = SbnzprojectApplication.kieSessions.get("kieSession-"+username);
		if(kieSession==null) {
			KieServices ks = KieServices.Factory.get();
			KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
			kbconf.setOption(EventProcessingOption.STREAM);
			KieBase kbase = kieContainer.newKieBase(kbconf);
			kieSession = kbase.newKieSession();
		}
		Patient patient=patientRepository.findOne(id);
		kieSession.setGlobal("pId", patient.getId());

		kieSession.insert(patient);
		
		for(Medicine m:medicines) {
			kieSession.insert(m);
			for(MedicineComponent mc:m.getComponents())
				kieSession.insert(mc);
		}
		
		//focus na tu grupu pravila
		kieSession.getAgenda().getAgendaGroup("allergies").setFocus();
		kieSession.fireAllRules();
		Collection<String> allergiesFound = (Collection<String>) kieSession.getObjects(new ClassObjectFilter(String.class));
		Iterator<String> iterMed = allergiesFound.iterator();
		int mCounter=0;
		while (iterMed.hasNext()) {
			System.err.println(iterMed.next());
			mCounter++;
		}
		
		boolean alright=true;
		System.err.println(mCounter);
		if(mCounter!=0)
			alright=false;
		release(kieSession);
		return alright;
	}

	public void release(KieSession kieSession){
		kieSession.getObjects();
		
		for (Object object : kieSession.getObjects()) {
			if(!object.getClass().equals(Disease.class))
				kieSession.delete(kieSession.getFactHandle(object));
		}
	}
	
}
