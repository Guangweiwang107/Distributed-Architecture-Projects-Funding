package com.atguigu.crowd.entity.vo;

public class MemberLoginVO {
	
	private Integer id;
	
	private String username;
	
	private String email;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "MemberLoginVO [id=" + id + ", username=" + username + ", email=" + email + "]";
	}

	public MemberLoginVO(Integer id, String username, String email) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public MemberLoginVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
