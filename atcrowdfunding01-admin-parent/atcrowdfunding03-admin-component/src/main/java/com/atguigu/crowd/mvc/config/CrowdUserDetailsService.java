package com.atguigu.crowd.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;

@Component
public class CrowdUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthService authService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Admin admin = adminService.getAdminByLoginAcct(username);
		
		Integer adminId = admin.getId();
		
		List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
		
		List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);
		
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for (Role role : assignedRoleList) {
			
			String roleName = "ROLE_" + role.getName();
			
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
			
			authorities.add(simpleGrantedAuthority);
		}
		
		for (String authName : authNameList) {
			
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(username);
			
			authorities.add(simpleGrantedAuthority);
		}
		
		SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);
		
		
		return securityAdmin;
	}
	

}
