package com.sbnz.sbnzproject.controller;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Patient;
import com.sbnz.sbnzproject.model.User;
import com.sbnz.sbnzproject.security.JwtTokenUtil;
import com.sbnz.sbnzproject.service.MedicalRecordService;
import com.sbnz.sbnzproject.service.PatientService;
import com.sbnz.sbnzproject.service.UserService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class MedicalController {

	@Autowired
	MedicalRecordService medicalRecordService;

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

	@PostMapping(value = "/medicalRecord/create/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord,
			HttpServletRequest request) {

		User user=getUser(request);
		medicalRecord.setDate(new Date());
		MedicalRecord md = medicalRecordService.create(medicalRecord, user.getUsername());

		Patient patient = patientService.findById(id);

		patient.getPatientHistory().add(md);

		Patient p = patientService.create(patient);

		return new ResponseEntity<>(md, HttpStatus.CREATED);
	}

	@GetMapping(value = "/medicalRecord/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll() {

		Collection<MedicalRecord> records = medicalRecordService.getAll();

		return new ResponseEntity<>(records, HttpStatus.OK);
	}

	@GetMapping(value = "/medicalRecord/getAll/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllForThePatient(@PathVariable Long id) {
		Patient patient = patientService.findById(id);
		Collection<MedicalRecord> records = patient.getPatientHistory();
		return new ResponseEntity<>(records, HttpStatus.OK);
	}

	@GetMapping(value = "/medicalRecord/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMedicalRecord(@PathVariable Long id) {
		MedicalRecord medicalRecord = medicalRecordService.findById(id);
		return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
	}

}
