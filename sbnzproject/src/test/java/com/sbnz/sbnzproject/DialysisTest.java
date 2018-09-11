package com.sbnz.sbnzproject;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.drools.core.ClassObjectFilter;
import org.drools.core.ClockType;
import org.drools.core.time.SessionPseudoClock;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;

import com.sbnz.sbnzproject.event.DialysisImmediatelyEvent;
import com.sbnz.sbnzproject.event.HeartbeatEvent;
import com.sbnz.sbnzproject.event.UrinatingEvent;
import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Patient;

public class DialysisTest {

	@Test
	public void testDialysis(){
		
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kContainer = kieServices.newKieContainer(kieServices.newReleaseId("drools-spring-v2", "drools-spring-v2-kjar", "0.0.1-SNAPSHOT"));
		
		
		KieBaseConfiguration kconf = kieServices.newKieBaseConfiguration();
		kconf.setOption(EventProcessingOption.STREAM);
		KieBase kieBase = kContainer.newKieBase(kconf);
		
		KieSessionConfiguration kconfig1 = kieServices.newKieSessionConfiguration();
		kconfig1.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
		KieSession kSession1 = kieBase.newKieSession(kconfig1, null);
		
		KieSessionConfiguration kconfig2 = kieServices.newKieSessionConfiguration();
		kconfig2.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
		KieSession kSession2 = kieBase.newKieSession(kconfig2, null);
		
		runPositiveTest(kSession1);
		runNegativeTest(kSession2);
		
	}
	
	private void runPositiveTest(KieSession kieSession){
		SessionPseudoClock clock = kieSession.getSessionClock();
		
		Disease disease= new Disease(); disease.setName("chronic kidney disease");					
		kieSession.insert(disease);
		Patient p=new Patient();
		p.setId(1L);
		p.setName("Djurdjica");
		MedicalRecord medicalRecord=new MedicalRecord();
		medicalRecord.setDisease(disease);
		p.getPatientHistory().add(medicalRecord);
		for(int i=0;i<12;i++) {
			HeartbeatEvent heartbeatEvent = new HeartbeatEvent(p);
			kieSession.insert(heartbeatEvent);
		}
		for(int i=0;i<7;i++) {
			UrinatingEvent ue= new UrinatingEvent(p, 13);
			kieSession.insert(ue);
			clock.advanceTime(1, TimeUnit.HOURS);
		}
		
		kieSession.getAgenda().getAgendaGroup("dialysis").setFocus();
		kieSession.fireUntilHalt();
		Collection<?> coll = kieSession.getObjects(new ClassObjectFilter(DialysisImmediatelyEvent.class));
		assertTrue(coll.size()!=0);
		DialysisImmediatelyEvent die = (DialysisImmediatelyEvent) coll.iterator().next();
		Long l=1L;
		assertEquals(l, die.getPatient().getId());
		assertTrue(die!=null);
	}
	
	private void runNegativeTest(KieSession kieSession){
		SessionPseudoClock clock = kieSession.getSessionClock();
		
		Disease disease= new Disease(); disease.setName("chronic kidney disease");					
		kieSession.insert(disease);
		Patient p=new Patient();
		p.setId(1L);
		p.setName("Djurdjica");
		MedicalRecord medicalRecord=new MedicalRecord();
		medicalRecord.setDisease(disease);
		p.getPatientHistory().add(medicalRecord);
		for(int i=0;i<12;i++) {
			HeartbeatEvent heartbeatEvent = new HeartbeatEvent(p);
			kieSession.insert(heartbeatEvent);
		}
		for(int i=0;i<7;i++) {
			UrinatingEvent ue= new UrinatingEvent(p, 20);
			kieSession.insert(ue);
			clock.advanceTime(1, TimeUnit.HOURS);
		}
		
		kieSession.getAgenda().getAgendaGroup("dialysis").setFocus();
		kieSession.fireAllRules();
		Collection<?> coll = kieSession.getObjects(new ClassObjectFilter(DialysisImmediatelyEvent.class));
		assertTrue(coll.size()==0);
		DialysisImmediatelyEvent die = (DialysisImmediatelyEvent) coll.iterator().next();
		assertTrue(die==null);
	}
}
