package com.sbnz.sbnzproject.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnz.sbnzproject.model.Patient;
import com.sbnz.sbnzproject.model.User;
import com.sbnz.sbnzproject.security.JwtTokenUtil;
import com.sbnz.sbnzproject.service.PatientService;
import com.sbnz.sbnzproject.service.UserService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class PatientController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

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


	@PostMapping(value = "/patient/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody Patient patient) {

		Patient p = patientService.create(patient);

		return new ResponseEntity<>(p, HttpStatus.CREATED);
	}

	@GetMapping(value = "/patient/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll() {
		logger.info("> getting all patient");
		Collection<Patient> patients = patientService.getAll();
		logger.info("size: {}", patients.size());
		return new ResponseEntity<>(patients, HttpStatus.OK);
	}

	@GetMapping(value = "/patient/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPatient(@PathVariable Long id) {
		Patient patient = patientService.findById(id);
		System.err.println(patient.getName());
		return new ResponseEntity<>(patient, HttpStatus.OK);
	}

	@PutMapping(value = "/patient/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Patient patient) {
		Patient p = patientService.findById(id);
		p.setName(patient.getName());
		p.setLastName(patient.getLastName());
		p.setMedicineAllergies(patient.getMedicineAllergies());
		p.setComponentAllergies(patient.getComponentAllergies());
		Patient updated = patientService.create(p);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}
	
	@GetMapping(value = "/patient/chronic", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getWithChronic(HttpServletRequest request) {
		User user=getUser(request);
		ArrayList<Patient> patients = patientService.getWithChronic(user.getUsername());
		//System.err.println(patients.size());
		return new ResponseEntity<>(patients, HttpStatus.OK);
	}
	
	@GetMapping(value = "/patient/addicts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAddicts(HttpServletRequest request) {
		User user=getUser(request);
		ArrayList<Patient> patients = patientService.getAddicts(user.getUsername());
		//System.err.println(patients.size());
		return new ResponseEntity<>(patients, HttpStatus.OK);
	}
	
	@GetMapping(value = "/patient/weakImmunity", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getWithWeakImmunity(HttpServletRequest request) {
		User user=getUser(request);
		ArrayList<Patient> patients = patientService.getWithWeakImmunity(user.getUsername());
		//System.err.println(patients.size());
		return new ResponseEntity<>(patients, HttpStatus.OK);
	}
}
