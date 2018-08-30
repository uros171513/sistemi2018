package com.sbnz.sbnzproject.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.sbnz.sbnzproject.model.Symptom;
import com.sbnz.sbnzproject.model.User;
import com.sbnz.sbnzproject.security.JwtTokenUtil;
import com.sbnz.sbnzproject.service.SymptomService;
import com.sbnz.sbnzproject.service.UserService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class SymptomController {

	@Autowired
	SymptomService symptomService;
	
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

	@PostMapping(value = "/symptom/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody Symptom symptom) {

		symptom.setDeleted(false);

		Symptom s = symptomService.create(symptom);

		return new ResponseEntity<>(s, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/symptom/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Symptom symptom = symptomService.delete(id);
		return new ResponseEntity<>(symptom, HttpStatus.OK);
	}

	@GetMapping(value = "/symptom/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll() {
		Collection<Symptom> symptoms = symptomService.getAll();
		return new ResponseEntity<>(symptoms, HttpStatus.OK);
	}

	@GetMapping(value = "/symptom/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSymptom(@PathVariable Long id) {
		Symptom symptom = symptomService.findById(id);
		return new ResponseEntity<>(symptom, HttpStatus.OK);
	}

	@PutMapping(value = "/symptom/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Symptom symptom) {
		Symptom s = symptomService.findById(id);
		s.setName(symptom.getName());
		s.setSymptomType(symptom.getSymptomType());
		Symptom updated = symptomService.create(s);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

	@PostMapping(value = "/symptom/getRelatedSymptoms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody Disease disease, HttpServletRequest request) {
		User user=getUser(request);
		ArrayList<Symptom> symptoms = symptomService.symptomsByDisease(disease, user.getUsername());

		return new ResponseEntity<>(symptoms, HttpStatus.CREATED);
	}

}
