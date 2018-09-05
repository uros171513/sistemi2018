package com.sbnz.sbnzproject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.drools.core.ClassObjectFilter;
import org.drools.core.ClockType;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.sbnz.sbnzproject.controller.MedicalController;
import com.sbnz.sbnzproject.controller.WebSocketController;
import com.sbnz.sbnzproject.event.DialysisImmediatelyEvent;
import com.sbnz.sbnzproject.event.HeartbeatEvent;
import com.sbnz.sbnzproject.event.HeartbeatNotGoodEvent;
import com.sbnz.sbnzproject.event.OxygenEvent;
import com.sbnz.sbnzproject.event.OxygenLevelNotGoodEvent;
import com.sbnz.sbnzproject.event.UrinatingEvent;
import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Patient;
import com.sbnz.sbnzproject.model.User;

@SpringBootApplication
public class SbnzprojectApplication {

	private static Logger log = LoggerFactory.getLogger(SbnzprojectApplication.class);

	public static Map<String, KieSession> kieSessions = new HashMap<>();
	public static Map<String, User> users = new HashMap<>();

	@Bean
	public KieContainer kieContainer() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("drools-spring-v2", "drools-spring-v2-kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		return kContainer;
	}

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = SpringApplication.run(SbnzprojectApplication.class, args);

		KieSession kieSession = realtimeSession();

		WebSocketController controller = ctx.getBean(WebSocketController.class);

		Thread.sleep(1000 * 12);
		while (true) {
			oxygen(kieSession, controller);
			heartbeats(kieSession, controller);
			dialysis(kieSession, controller);
		}

	}

	public static void oxygen(final KieSession realtimeSession, WebSocketController controller)
			throws InterruptedException {
		Thread.sleep(1000 * 45);
		Thread t = new Thread() {
			@Override
			public void run() {
				Patient p=new Patient(1L, "Djordje");
				for (int index = 0; index < 5; index++) {
					OxygenEvent oxygenEvent = new OxygenEvent( p, 67);
					realtimeSession.insert(oxygenEvent);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}

		};
		t.setDaemon(true);
		t.start();
		try {
			Thread.sleep(1000*5);
		} catch (InterruptedException e) {
		}
		realtimeSession.getAgenda().getAgendaGroup("oxygen").setFocus();
		realtimeSession.fireUntilHalt();
		Collection<OxygenLevelNotGoodEvent> newEvents = (Collection<OxygenLevelNotGoodEvent>) realtimeSession
				.getObjects(new ClassObjectFilter(OxygenLevelNotGoodEvent.class));

		Iterator<OxygenLevelNotGoodEvent> iter = newEvents.iterator();
		while (iter.hasNext()) {
			OxygenLevelNotGoodEvent ole = iter.next();
			System.out.println(ole);
			controller.onReceivedMesage(ole.getMessage());
			break;
		}

	}

	public static void heartbeats(final KieSession realtimeSession, WebSocketController controller) throws InterruptedException {
		Thread t = new Thread() {
			@Override
			public void run() {
				Patient p=new Patient(1L, "Uros");
				for (int index = 0; index <30; index++) {
					HeartbeatEvent heartbeatEvent = new HeartbeatEvent(p);
					realtimeSession.insert(heartbeatEvent);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		};
		t.setDaemon(true);
		t.start();
		try {
			Thread.sleep(1000*2);
		} catch (InterruptedException e) {
		}
		realtimeSession.getAgenda().getAgendaGroup("heartbeats").setFocus();
		realtimeSession.fireUntilHalt();
		Collection<HeartbeatNotGoodEvent> newEvents = (Collection<HeartbeatNotGoodEvent>) realtimeSession
				.getObjects(new ClassObjectFilter(HeartbeatNotGoodEvent.class));

		Iterator<HeartbeatNotGoodEvent> iter = newEvents.iterator();
		while (iter.hasNext()) {
			HeartbeatNotGoodEvent hbe = iter.next();
			System.out.println(hbe);
			controller.onReceivedMesage(hbe.getMessage());
			break;
		}

	}

	
	public static void dialysis(final KieSession realtimeSession, WebSocketController controller) throws InterruptedException {
		Thread.sleep(1000 * 45);
		Thread t = new Thread() {
			@Override
			public void run() {
					Disease disease= new Disease(); disease.setName("chronic kidney disease");					
					realtimeSession.insert(disease);
					Patient p=new Patient();
					p.setId(1L);
					p.setName("Djurdjica");
					MedicalRecord medicalRecord=new MedicalRecord();
					medicalRecord.setDisease(disease);
					p.getPatientHistory().add(medicalRecord);
					for(int i=0;i<12;i++) {
						HeartbeatEvent heartbeatEvent = new HeartbeatEvent(p);
						realtimeSession.insert(heartbeatEvent);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							//do nothing
						}
					}
					for(int i=0;i<7;i++) {
						UrinatingEvent ue= new UrinatingEvent(p, 13);
						realtimeSession.insert(ue);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							//do nothing
						}
					}

			}
		};
		
		t.setDaemon(true);
		t.start();
		try {
			Thread.sleep(1000*5);
		} catch (InterruptedException e) {
		}
		realtimeSession.getAgenda().getAgendaGroup("dialysis").setFocus();
		realtimeSession.fireUntilHalt();
		Collection<DialysisImmediatelyEvent> newEvents = (Collection<DialysisImmediatelyEvent>) realtimeSession
				.getObjects(new ClassObjectFilter(DialysisImmediatelyEvent.class));

		Iterator<DialysisImmediatelyEvent> iter = newEvents.iterator();
		while (iter.hasNext()) {
			DialysisImmediatelyEvent die = iter.next();
			System.out.println(die);
			controller.onReceivedMesage(die.getMessage());
			break;
		}
	}
	
	public static KieSession realtimeSession() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("drools-spring-v2", "drools-spring-v2-kjar", "0.0.1-SNAPSHOT"));

		KieBaseConfiguration kconf = ks.newKieBaseConfiguration();
		kconf.setOption(EventProcessingOption.STREAM);
		KieBase kieBase = kContainer.newKieBase(kconf);

		KieSessionConfiguration kconfig1 = ks.newKieSessionConfiguration();
		kconfig1.setOption(ClockTypeOption.get(ClockType.REALTIME_CLOCK.getId()));
		KieSession ksession = kieBase.newKieSession(kconfig1, null);

		return ksession;
	}

}
