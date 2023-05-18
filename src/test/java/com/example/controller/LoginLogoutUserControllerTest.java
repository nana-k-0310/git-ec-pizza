package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
import com.example.service.LoginLogoutUserService;

import ch.qos.logback.core.model.Model;

@SpringBootTest
@DisplayName("ログイン機能コントローラー")
class LoginLogoutUserControllerTest {

	@Autowired
	private LoginLogoutUserController loginLogoutUserController;
	
	@Autowired
	private LoginLogoutUserService loginLogoutUserService;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@DisplayName("ログインする")
	void ユーザーログインテスト() throws Exception {
		LoginLogoutUserForm form = new LoginLogoutUserForm();
		form.setPassword("aaaaaaaa");
		form.setEmail("abc@gmail.com");
		Model model1 = new Model();
//		loginLogoutUserController.toLogin(form, model1);
		
		
	}
	
	@AfterEach
	@DisplayName("ログアウトする")
	void ログアウトする() throws Exception{
		
		System.out.println("ログアウト完了しました");
	}

}
