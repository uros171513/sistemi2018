package com.sbnz.sbnzproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sbnz.sbnzproject.model.Symptom;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {
	@Modifying
	@Query("update Symptom s set s.deleted = false where s.id = ?1")
	@Transactional
	void deleteSymptomById(Long id);

	@Query("select s from Symptom s where s.deleted = false")
	List<Symptom> findAll();
}
