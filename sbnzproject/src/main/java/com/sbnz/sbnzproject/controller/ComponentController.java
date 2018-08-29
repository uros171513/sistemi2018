package com.sbnz.sbnzproject.controller;

import java.awt.Component;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnz.sbnzproject.model.MedicineComponent;
import com.sbnz.sbnzproject.service.ComponentService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class ComponentController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ComponentService componentService;

	@PostMapping(value = "/component/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody MedicineComponent component) {

		component.setDeleted(false);

		MedicineComponent c = componentService.create(component);

		return new ResponseEntity<>(c, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/component/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable Long id) {

		MedicineComponent component = componentService.delete(id);

		return new ResponseEntity<>(component, HttpStatus.OK);
	}

	@GetMapping(value = "/component/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll() {

		List<MedicineComponent> components = componentService.getAll();

		return new ResponseEntity<>(components, HttpStatus.OK);
	}

	@GetMapping(value = "/component/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getComponent(@PathVariable Long id) {

		MedicineComponent component = componentService.findById(id);

		return new ResponseEntity<>(component, HttpStatus.OK);
	}

	@PutMapping(value = "/component/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Component component) {
		MedicineComponent c = componentService.findById(id);
		c.setName(component.getName());
		MedicineComponent updated = componentService.create(c);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

}
