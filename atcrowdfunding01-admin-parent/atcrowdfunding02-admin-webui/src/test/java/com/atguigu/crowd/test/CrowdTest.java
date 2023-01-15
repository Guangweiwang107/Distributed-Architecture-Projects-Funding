package com.atguigu.crowd.test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.RoleService;


//指定 Spring 给 Junit 提供的运行器类
//加载 Spring 配置文件的注解
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml" })
public class CrowdTest {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private RoleMapper roleMapper;
	
	
	@Test
	public void testRoleSave() {
		for(int i = 0; i < 235; i++) {
			roleMapper.insert(new Role(null, "role"+i));
		}
	}
	
	@Test
	public void test() {
		for(int i = 0; i < 238; i++) {
			adminMapper.insert(new Admin(null, "loginAcct"+i, "userPswd"+i, "userName"+i, "email"+i, null));
		}
	}
	
	@Test
	public void testTx() {
		adminService.saveAdmin(new Admin(null,"jerry","123456","杰瑞","jerry@qq.com",null));
	}
	@Test
	public void testAdminMapperAutowired() {
		Admin admin = new Admin(null,"tom","123123","汤姆","tom@qq.com",null);
		
		int count = adminMapper.insert(admin);
		System.out.println(count);
	}

	@Test
	public void testDataSource() throws SQLException {
//1.通过数据源对象获取数据源连接
		Connection connection = dataSource.getConnection();
//2.打印数据库连接
		System.out.println(connection);
	}
}
