package com.sbnz.sbnzproject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

import com.sbnz.sbnzproject.model.User;

@SpringBootApplication
public class SbnzprojectApplication {

	private static Logger log = LoggerFactory.getLogger(SbnzprojectApplication.class);
	
	public static Map<String, KieSession> kieSessions = new HashMap<>();
	public static Map<String, User> users = new HashMap<>();
	
	@Bean
    public KieContainer kieContainer() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.newKieContainer(ks.newReleaseId("drools-spring-v2","drools-spring-v2-kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		return kContainer;
    }

	public static void main(String[] args) {
		SpringApplication.run(SbnzprojectApplication.class, args);
	}
	
}
