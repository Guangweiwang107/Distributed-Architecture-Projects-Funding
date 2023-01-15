package com.atguigu.crowd.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private AuthMapper authMapper;
	
	@Override
	public List<Auth> getAll() {
		// TODO Auto-generated method stub
		
		return authMapper.selectByExample(new AuthExample());
	}

	@Override
	public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
		// TODO Auto-generated method stub
		return authMapper.selectAssignedAuthIdByRoleId(roleId);
		
	}

	@Override
	public void saveRoleAuthRelathinship(Map<String, List<Integer>> map) {
		// TODO Auto-generated method stub
		List<Integer> roleIdList = map.get("roleId");
		Integer roleId = roleIdList.get(0);
		
		authMapper.deleteOldRelationship(roleId);
		
		List<Integer> authIdList = map.get("authIdArray");
		
		if(authIdList != null && authIdList.size() > 0) {
			authMapper.insertNewRelationship(roleId,authIdList);
		}
		
	}

	@Override
	public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
		// TODO Auto-generated method stub
		return authMapper.selectAssignedAuthNameByAdminId(adminId);
	}
	
}
