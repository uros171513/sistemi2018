package com.sbnz.sbnzproject.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.sbnz.sbnzproject.model.Medicine;
import com.sbnz.sbnzproject.model.MedicineComponent;
import com.sbnz.sbnzproject.model.Patient;
import com.sbnz.sbnzproject.service.MedicineService;
import com.sbnz.sbnzproject.service.PatientService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class MedicineController {

	@Autowired
	MedicineService medicineService;

	@Autowired
	PatientService patientService;

	@PostMapping(value = "/medicine/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody Medicine medicine) {
		medicine.setDeleted(false);

		Medicine m = medicineService.create(medicine);

		return new ResponseEntity<>(m, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/medicine/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Medicine medicine = medicineService.delete(id);
		return new ResponseEntity<>(medicine, HttpStatus.OK);
	}

	@GetMapping(value = "/medicine/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll() {
		Collection<Medicine> medicine = medicineService.getAll();
		return new ResponseEntity<>(medicine, HttpStatus.OK);
	}

	@PutMapping(value = "/medicine/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Medicine medicine) {
		Medicine m = medicineService.findById(id);
		m.setName(medicine.getName());
		m.setMedicineType(medicine.getMedicineType());
		m.setComponents((Set<MedicineComponent>) medicine.getComponents());
		Medicine updated = medicineService.create(m);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

	@GetMapping(value = "/medicine/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMedicine(@PathVariable Long id) {
		Medicine medicine = medicineService.findById(id);
		return new ResponseEntity<>(medicine, HttpStatus.OK);
	}

	@PostMapping(value = "/medicine/verifyMedicines/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyMedicines(@PathVariable Long id, @RequestBody ArrayList<Medicine> medicines,
			HttpServletRequest request) {
		Patient patient=patientService.findById(id);
		boolean alright=true;
		
//		//da li je pacijent alergican na neki od lekova
//		for(Medicine m: medicines) {
//			for(Medicine m1:patient.getMedicineAllergies()) {
//				if(m1.getName().equals(m.getName()))
//					alright=false;
//			}
//		}
//		
//		//da li neki od lekova sadrzi komponentu na koju je pacijent alergican
//		for(Medicine m:medicines) {
//			for(MedicineComponent c:m.getComponents()) {
//				for(MedicineComponent c1: patient.getComponentAllergies()) {
//					if(c1.getName().equals(c.getName()))
//						alright=false;
//				}
//			}
//			
//		}
		alright=medicineService.checkAllergies(id,medicines, request);
		return new ResponseEntity<>(alright, HttpStatus.OK);
	}

}
