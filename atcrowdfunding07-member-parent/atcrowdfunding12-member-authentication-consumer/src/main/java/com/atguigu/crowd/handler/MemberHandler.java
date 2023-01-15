package com.atguigu.crowd.handler;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;

@Controller
public class MemberHandler {

	@Autowired
	private ShortMessageProperties shortMessageProperties;

	@Autowired
	private RedisRemoteService redisRemoteService;

	@Autowired
	private MySQLRemoteService mySQLRemoteService;
	
	@RequestMapping("/auth/member/logout")
	public String logout(HttpSession httpSession) {
		
		httpSession.invalidate();
		
		return "redirect:/";
	}

	@RequestMapping("/auth/member/do/login")
	public String login(
			@RequestParam("loginacct") String loginacct,
			@RequestParam("userpswd") String userpswd,
			ModelMap modelMap,
			HttpSession session) {
		
		ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
		
		if(ResultEntity.FAILED.equals(resultEntity.getResult())) {
			
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
			
			return "member-login";
			
		}
		
		MemberPO memberPO = resultEntity.getData();
		
		if(memberPO == null) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
			
			return "member-login";
		}
		
		String userpswdDataBase = memberPO.getUserpswd();
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		boolean matchresult = passwordEncoder.matches(userpswd, userpswdDataBase);
		
		if(!matchresult) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);

			return "member-login";
		}
		
		Integer id = memberPO.getId();
		String username = memberPO.getUsername();
		String email = memberPO.getEmail();
		
		MemberLoginVO memberLoginVO = new MemberLoginVO(id, username, email);
		
		session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
		
		
		return "redirect:/auth/member/to/center/page";
	}
	
	
	@RequestMapping("/auth/do/member/register")
	public String register(MemberVO memberVO, ModelMap modelMap) {

		String phoneNum = memberVO.getPhoneNum();

		String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

		ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);

		if (ResultEntity.FAILED.equals(resultEntity.getResult())) {

			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());

			return "member-reg";
		}

		String redisCode = resultEntity.getData();

		if (redisCode == null) {

			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);

			return "member-reg";
		}

		if (!Objects.equals(memberVO.getCode(), redisCode)) {

			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);

			return "member-reg";
		}

		redisRemoteService.removeRedisKeyRemote(key);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String userpswdBeforeEncode = memberVO.getUserpswd();

		String userpswdAfterEncode = passwordEncoder.encode(userpswdBeforeEncode);

		memberVO.setUserpswd(userpswdAfterEncode);

		MemberPO memberPO = new MemberPO();

		BeanUtils.copyProperties(memberVO, memberPO);

		ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);

		if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {

			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());

			return "member-reg";
		}

		return "redirect:/auth/member/to/login/page";
	}

	@ResponseBody
	@RequestMapping("/auth/member/send/short/message.json")
	public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {

		ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendCodeByShortMessage(
				shortMessageProperties.getHost(), shortMessageProperties.getPath(), shortMessageProperties.getMethod(),
				phoneNum, shortMessageProperties.getAppCode(), shortMessageProperties.getSign(),
				shortMessageProperties.getSkin());

		if (ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())) {

			String code = sendMessageResultEntity.getData();

			String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

			ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code,
					12, TimeUnit.MINUTES);

			if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
				return ResultEntity.successWithoutData();
			} else {
				return saveCodeResultEntity;
			}
		} else {
			return sendMessageResultEntity;
		}
	}

}
