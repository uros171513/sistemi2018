package com.sbnz.sbnzproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sbnz.sbnzproject.model.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

	   @Modifying
	    @Query("update Medicine m set m.deleted = false where m.id = ?1")
	    @Transactional
	    void deleteMedicineById(Long id);

	    @Query("select m from Medicine m where m.deleted = false")
	    List<Medicine> findAll();
	
}
