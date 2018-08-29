package com.sbnz.sbnzproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sbnz.sbnzproject.model.Disease;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
	
	Disease findByName(String name);

	@Modifying
    @Query("update Disease d set d.deleted = false where d.id = ?1")
    @Transactional
	void deleteDiseaseById(Long id);
	

    @Query("select d from Disease d where d.deleted = false")
    List<Disease> findAll();

}
