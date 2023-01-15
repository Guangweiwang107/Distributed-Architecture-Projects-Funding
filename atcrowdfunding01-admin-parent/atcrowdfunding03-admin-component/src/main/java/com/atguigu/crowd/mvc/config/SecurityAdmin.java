package com.atguigu.crowd.mvc.config;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.atguigu.crowd.entity.Admin;

public class SecurityAdmin extends User {

	private static final long serialVersionUID = 1L;

	private Admin originalAdmin;

	public SecurityAdmin(
			
			Admin originalAdmin,

			List<GrantedAuthority> authorities) {
		super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);

		this.originalAdmin = originalAdmin;

		this.originalAdmin.setUserPswd(null);
	}

	public Admin getOriginalAdmin() {
		return originalAdmin;
	}

}
