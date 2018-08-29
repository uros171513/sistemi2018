package com.sbnz.sbnzproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbnz.sbnzproject.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
