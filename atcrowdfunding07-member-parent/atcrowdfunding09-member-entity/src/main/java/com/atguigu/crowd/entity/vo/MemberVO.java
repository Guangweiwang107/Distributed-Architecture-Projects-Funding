package com.atguigu.crowd.entity.vo;


public class MemberVO {
	
	private String loginacct;
	
	private String userpswd;
	
	private String username;
	
	private String email;
	
	private String phoneNum;
	
	private String code;

	public MemberVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MemberVO(String loginacct, String userpswd, String username, String email, String phoneNum, String code) {
		super();
		this.loginacct = loginacct;
		this.userpswd = userpswd;
		this.username = username;
		this.email = email;
		this.phoneNum = phoneNum;
		this.code = code;
	}

	@Override
	public String toString() {
		return "MemberVO [loginacct=" + loginacct + ", userpswd=" + userpswd + ", username=" + username + ", email="
				+ email + ", phoneNum=" + phoneNum + ", code=" + code + "]";
	}

	public String getLoginacct() {
		return loginacct;
	}

	public void setLoginacct(String loginacct) {
		this.loginacct = loginacct;
	}

	public String getUserpswd() {
		return userpswd;
	}

	public void setUserpswd(String userpswd) {
		this.userpswd = userpswd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
