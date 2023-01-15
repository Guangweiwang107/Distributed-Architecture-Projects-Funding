package com.atguigu.crowd.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.entity.AdminExample.Criteria;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void saveAdmin(Admin admin) {
		// TODO Auto-generated method stub
		
		String userPswd = admin.getUserPswd();
		
		//String userPswd = CrowdUtil.md5(admin.getUserPswd());
		userPswd = passwordEncoder.encode(userPswd);
		
		admin.setUserPswd(userPswd);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date data = new Date();
		String createTime = format.format(data);
		admin.setCreateTime(createTime);
		
		try {
			adminMapper.insert(admin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e instanceof DuplicateKeyException) {
				throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
		}
	}

	@Override
	public List<Admin> getall() {
		// TODO Auto-generated method stub
		return adminMapper.selectByExample(new AdminExample());
	}

	@Override
	public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
		
		AdminExample adminExample = new AdminExample();
		
		Criteria criteria = adminExample.createCriteria();
		
		criteria.andLoginAcctEqualTo(loginAcct);
		
		List<Admin> list = adminMapper.selectByExample(adminExample);
		
		if(list == null || list.size() == 0) {
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
		}
		
		Admin admin = list.get(0);
		
		if(admin == null) {
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		
		String userPswdDB = admin.getUserPswd();
		
		String userPswdForm = CrowdUtil.md5(userPswd);
		
		if(!Objects.equals(userPswdDB, userPswdForm)) {
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		return admin;
		
	}

	@Override
	public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		
		List<Admin> list = adminMapper.selectAdminByKeyword(keyword);
		
		return new PageInfo<Admin>(list);
	}

	@Override
	public void remove(Integer adminId) {
		// TODO Auto-generated method stub
		adminMapper.deleteByPrimaryKey(adminId);
	}

	@Override
	public Admin getAdminById(Integer adminId) {
		return adminMapper.selectByPrimaryKey(adminId);
	}

	@Override
	public void update(Admin admin) {
		try {
			adminMapper.updateByPrimaryKeySelective(admin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e instanceof DuplicateKeyException) {
				throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
		}
		
	}

	@Override
	public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
		// TODO Auto-generated method stub
		adminMapper.deleteOLdRelationship(adminId);
		
		if(roleIdList != null && roleIdList.size() > 0) {
			adminMapper.insertNewRelationship(adminId,roleIdList);
		}
	}

	@Override
	public Admin getAdminByLoginAcct(String username) {
		AdminExample example = new AdminExample();
		Criteria criteria = example.createCriteria();
		criteria.andLoginAcctEqualTo(username);
		return adminMapper.selectByExample(example).get(0);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
