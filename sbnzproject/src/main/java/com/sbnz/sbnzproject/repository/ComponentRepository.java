package com.sbnz.sbnzproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sbnz.sbnzproject.model.MedicineComponent;

public interface ComponentRepository extends JpaRepository<MedicineComponent, Long> {

	@Modifying
	@Query("update MedicineComponent c set c.deleted = false where c.id = ?1")
	@Transactional
	void deleteComponentById(Long id);

	@Query("select c from MedicineComponent c where c.deleted = false")
	List<MedicineComponent> findAll();
}
