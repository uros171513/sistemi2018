package com.sbnz.sbnzproject.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Symptom;
import com.sbnz.sbnzproject.model.User;
import com.sbnz.sbnzproject.security.JwtTokenUtil;
import com.sbnz.sbnzproject.service.DiseaseService;
import com.sbnz.sbnzproject.service.PatientService;
import com.sbnz.sbnzproject.service.UserService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
@Scope("session")
public class DiseaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DiseaseService diseaseService;

	@Autowired
	PatientService patientService;

	@Autowired
	UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.header}")
	private String tokenHeader;

	public User getUser(HttpServletRequest request) {
		String token = request.getHeader(tokenHeader);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByUsername(username);
	}

	@PostMapping(value = "/disease/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody Disease disease) {

		disease.setDeleted(false);

		Disease d = diseaseService.create(disease);

		return new ResponseEntity<>(d, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/disease/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Disease disease = diseaseService.delete(id);
		return new ResponseEntity<>(disease, HttpStatus.OK);
	}

	@GetMapping(value = "/disease/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll() {
		logger.info("> getting all diseases");
		Collection<Disease> diseases = diseaseService.getAll();
		logger.info("size: {}", diseases.size());
		return new ResponseEntity<>(diseases, HttpStatus.OK);
	}

	@GetMapping(value = "/disease/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDisease(@PathVariable Long id) {
		Disease disease = diseaseService.findById(id);
		logger.info("getting disease: {}", disease.getName());
		return new ResponseEntity<>(disease, HttpStatus.OK);
	}

	@PutMapping(value = "/disease/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody Disease disease) {
		Disease d = diseaseService.findById(disease.getId());
		d.setName(disease.getName());
		d.setDiseaseType(disease.getDiseaseType());
		d.setSymptoms((Set<Symptom>) disease.getSymptoms());
		Disease updated = diseaseService.create(d);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

	@PostMapping(value = "/disease/diseaseBySymptoms/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> diseaseBySymptoms(@PathVariable Long id, @RequestBody ArrayList<Symptom> symptoms,
			HttpServletRequest request) {
		// id pacijenta ciji su simptomi
		User user = getUser(request);
		MedicalRecord mr = diseaseService.diseaseBySymptoms(id, symptoms, user.getUsername());
		return new ResponseEntity<>(mr, HttpStatus.OK);
	}

	@PostMapping(value = "/disease/getRelatedDiseases", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRelatedDiseases(@RequestBody ArrayList<Symptom> symptoms, HttpServletRequest request) {
		User user = getUser(request);
		ArrayList<Disease> diseases = diseaseService.getDiseasesBySymptoms(symptoms, user.getUsername());
		return new ResponseEntity<>(diseases, HttpStatus.OK);
	}

	@PostMapping(value = "/disease/getByName", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getByName(@RequestBody String name) {
		System.err.println(name);
		String[] dName = name.split("\"");
		System.err.println(dName.length);
		for (String s : dName)
			System.err.println(s);
		logger.info("> getting disease by name");
		Disease disease = diseaseService.findByName(dName[1]);
		logger.info("name: {}", disease.getName());
		return new ResponseEntity<>(disease, HttpStatus.OK);
	}

}
