package com.sbnz.sbnzproject.service;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.sbnz.sbnzproject.model.User;

public interface UserService {

	public User create(User user);

	public Collection<User> getAll();

	public User findById(Long id);

	public User findByUsername(String username);

	Boolean login(String username, String password);

	Boolean logout(String username);

	public void delete(Long id);

}
