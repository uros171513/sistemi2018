package com.sample;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Medicine;
import com.sbnz.sbnzproject.model.Patient;
import com.sbnz.sbnzproject.model.Symptom;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

	public static final void main(String[] args) {
		try {
			// load up the knowledge base
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession kSession = kContainer.newKieSession("bolest");

			// go !
		
			String pattern = "MM/dd/yyyy";
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			Date date = format.parse("11/20/2017");
			
			Disease chronic= new Disease();
			chronic.setName("chronic kidney disease");
			Disease hypertension= new Disease();
			hypertension.setName("hypertension");
			Disease diabetes= new Disease();
			diabetes.setName("diabetes");
			
			
			Symptom s1=new Symptom(); s1.setName("tiredness");
			Symptom s2=new Symptom(); s2.setName("nocturia");
			Symptom s3=new Symptom(); s3.setName("chest pain");
			Symptom s4=new Symptom(); s4.setName("choking");
			Symptom s5=new Symptom(); s5.setName("legs and joints swelling");
			
			MedicalRecord mr= new MedicalRecord();
			mr.setDisease(null);
			mr.getSymptoms().add(s1);mr.getSymptoms().add(s3);
			mr.getSymptoms().add(s2);mr.getSymptoms().add(s4);
			mr.getSymptoms().add(s5);
			
			Patient patient= new Patient();
//			MedicalRecord past= new MedicalRecord();
//			past.setDisease(hypertension);
//			past.setDate(date);
			MedicalRecord past= new MedicalRecord();
			past.setDisease(diabetes);
			Set<MedicalRecord> history= new HashSet<>();
			history.add(past);
			patient.setPatientHistory(history);
			
			kSession.insert(patient);
			kSession.insert(mr);
			kSession.insert(diabetes);
			kSession.insert(chronic);
			kSession.insert(hypertension);		
			
			kSession.fireAllRules();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
