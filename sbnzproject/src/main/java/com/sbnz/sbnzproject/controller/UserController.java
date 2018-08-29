package com.sbnz.sbnzproject.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnz.sbnzproject.model.User;
import com.sbnz.sbnzproject.model.UserRole;
import com.sbnz.sbnzproject.security.JwtAuthenticationRequest;
import com.sbnz.sbnzproject.security.JwtTokenUtil;
import com.sbnz.sbnzproject.service.UserService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class UserController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

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

	@PostMapping(value = "/user/registerDoctor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerDoctor(@RequestBody User user) {
		PasswordEncoder enc = new BCryptPasswordEncoder();
		String encoded = enc.encode(user.getPassword());
		user.setPassword(encoded);
		user.setRole(UserRole.DOCTOR);

		User u = userService.create(user);
		return new ResponseEntity<>(u, HttpStatus.CREATED);
	}

	@PostMapping(value = "/user/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody JwtAuthenticationRequest jwt) {
		Boolean response = userService.login(jwt.getUsername(), jwt.getPassword());
		if (!response) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		User user = userService.findByUsername(jwt.getUsername());
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/user/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> logout(HttpServletRequest request) {
		User user=getUser(request);
		Boolean bool = userService.logout(user.getUsername());
		if(bool)
			return new ResponseEntity<>(bool, HttpStatus.OK);
		else
			return new ResponseEntity<>(bool, HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/user/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll() {
		logger.info("> getting all users");
		Collection<User> users = userService.getAll();
		logger.info("size: {}", users.size());
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping(value = "/user/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		User user = userService.findById(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PutMapping(value = "/user/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody User user, HttpServletRequest request) {
		String token = request.getHeader(tokenHeader);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		User u = userService.findByUsername(username);
		u.setName(user.getName());
		u.setLastName(user.getLastName());
		u.setUsername(user.getUsername());
		PasswordEncoder enc = new BCryptPasswordEncoder();
		String encoded = enc.encode(user.getPassword());
		u.setPassword(encoded);
		User updated = userService.create(u);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

}
