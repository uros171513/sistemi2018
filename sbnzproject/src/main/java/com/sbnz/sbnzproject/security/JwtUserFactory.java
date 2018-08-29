package com.sbnz.sbnzproject.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.sbnz.sbnzproject.model.User;
import com.sbnz.sbnzproject.model.UserRole;

public final class JwtUserFactory {

	private JwtUserFactory() {
	}

	public static JwtUser create(User user) {
		return new JwtUser(user.getId(), user.getUsername(), user.getPassword(),
				mapToGrantedAuthorities(user.getRole()), true);
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(
			UserRole userType) {
		List<GrantedAuthority> retVal = new ArrayList<>();
		retVal.add(new SimpleGrantedAuthority(userType.name()));
		return retVal;
	}
}