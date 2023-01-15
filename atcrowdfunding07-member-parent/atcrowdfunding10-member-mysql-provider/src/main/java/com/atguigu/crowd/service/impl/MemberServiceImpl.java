package com.atguigu.crowd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.entity.po.MemberPOExample.Criteria;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.service.api.MemberService;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberPOMapper memberPOMapper;
	
	public MemberPO getMemberPOByLoginAcct(String loginacct) {
		// TODO Auto-generated method stub
		MemberPOExample example = new MemberPOExample();
		
		Criteria criteria = example.createCriteria();
		
		criteria.andLoginacctEqualTo(loginacct);
		
		List<MemberPO> list = memberPOMapper.selectByExample(example);
		
		if(list == null || list.size() == 0) {
			return null;
		}
		
		return list.get(0);
	}
	
	@Transactional(
			propagation = Propagation.REQUIRES_NEW,
			rollbackFor = Exception.class,
			readOnly = false
			)
	public void saveMember(MemberPO memberPO) {
		
		memberPOMapper.insertSelective(memberPO);
		
	}

}
