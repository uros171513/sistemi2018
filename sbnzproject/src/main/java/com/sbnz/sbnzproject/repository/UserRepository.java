package com.sbnz.sbnzproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbnz.sbnzproject.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsername(String username);

}
