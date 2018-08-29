package com.sbnz.sbnzproject.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.Symptom;
import com.sbnz.sbnzproject.repository.SymptomRepository;
import com.sbnz.sbnzproject.service.SymptomService;

@Service
public class SymptomServiceImpl implements SymptomService {

	@Autowired
	SymptomRepository symptomRepository;

	@Override
	public Symptom findById(Long id) {
		return symptomRepository.getOne(id);
	}

	@Override
	public Symptom create(Symptom s) {
		return symptomRepository.save(s);
	}

	@Override
	public Collection<Symptom> getAll() {
		return symptomRepository.findAll();
	}

	@Override
	public Symptom delete(Long id) {
		Symptom symptom = symptomRepository.findOne(id);
		symptomRepository.deleteSymptomById(id);
		return symptom;
	}

	public void release(KieSession kieSession){
		kieSession.getObjects();
		
		for (Object object : kieSession.getObjects()) {
			kieSession.delete(kieSession.getFactHandle(object));
		}
	}
	
	private final KieContainer kieContainer;
	
    @Autowired
    public SymptomServiceImpl(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

	@Override
	public ArrayList<Symptom> symptomsByDisease(Disease disease, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		KieSession kieSession = (KieSession) request.getSession().getAttribute("kieSession");
		System.err.println(kieSession);
		if(kieSession==null) {
			KieServices ks = KieServices.Factory.get();
			KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
			kbconf.setOption(EventProcessingOption.STREAM);
			KieBase kbase = kieContainer.newKieBase(kbconf);
			kieSession = kbase.newKieSession();
		}
		
		QueryResults results = kieSession.getQueryResults( "disease symptoms", new Object[] { disease } );
		System.out.println( "we have " + results.size());

		Map<Symptom, Long> map = new HashMap<>();
		for ( QueryResultsRow row : results ) {System.err.println("usao u ovo");
			Collection<Symptom> generalSymptoms = ( Collection<Symptom> ) row.get( "generalSymptoms" );
			Collection<Symptom> specificSymptoms = ( Collection<Symptom> ) row.get( "specificSymptoms" );
			for(Symptom s : generalSymptoms) {
			    map.put(s, 1L);
			}
			for(Symptom s : specificSymptoms) {
			    map.put(s, 2L);
			}
		}
		
		ArrayList<Symptom> relatedSymptoms=new ArrayList<>();
		Map<Symptom, Long> result = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		for(Symptom ss: result.keySet()) {
			relatedSymptoms.add(ss);
		}
		return relatedSymptoms;
	}

}
