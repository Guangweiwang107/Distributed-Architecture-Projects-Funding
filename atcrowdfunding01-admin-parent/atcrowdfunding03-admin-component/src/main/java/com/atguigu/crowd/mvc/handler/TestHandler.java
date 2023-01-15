package com.atguigu.crowd.mvc.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;

@Controller
public class TestHandler {
	@Autowired
	private AdminService adminService;
	
	@RequestMapping("/test/ssm.html")
	public String testssm(Model model) {
		List<Admin> list = adminService.getall();
		model.addAttribute("list", list);
		return "target";
	}
	
	
}
