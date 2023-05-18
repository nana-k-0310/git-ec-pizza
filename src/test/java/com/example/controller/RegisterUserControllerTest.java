package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.domain.UserInfo;
import com.example.form.RegisterUserForm;
import com.example.service.RegisterUserService;

@SpringBootTest
@DisplayName("ユーザー登録コントローラー")
class RegisterUserControllerTest {
	
	@Autowired
	private RegisterUserController registerUserController;
	
	@Autowired
	private RegisterUserService registerUserService;
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	@DisplayName("仮ユーザーを登録する")
	void ユーザー登録テスト() throws Exception {
		RegisterUserForm form = new RegisterUserForm();
		
		form.setFirstName("河野夏菜");
		form.setEmail("kono@sample.co.jp");
		form.setPassword("KonoKono");
		form.setConfirmationPassword("KonoKono");
		form.setZipcode("123-4567");
		form.setAddress("東京都渋谷区1-1-1");
		form.setTelephone("03-1234-5678");
		
//		BindingResult result = null;
//		Model model = null;
		
//		registerUserController.registerUser(form, result, model);

		
	}

	@Test
	void ユーザー情報取得テスト() throws Exception {
		UserInfo user1 = registerUserService.findByEmail("kono@sample.co.jp");
		assertEquals("河野夏菜", user1.getName(), "名前が登録されていません");
		assertEquals("kono@sample.co.jp", user1.getEmail(), "メールアドレスが登録されていません");
		assertEquals("KonoKono", user1.getPassword(), "パスワードが登録されていません");
		assertEquals("123-4567", user1.getZipcode(), "郵便番号が登録されていません");
		assertEquals("東京都渋谷区１−１−１", user1.getAddress(), "住所が登録されていません");
		assertEquals("03-1234-5678", user1.getTelephone(), "電話番号が登録されていません");
	}
	
	@AfterEach
	@DisplayName("仮ユーザーを削除する")
	void 登録ユーザー削除() throws Exception {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", "kono@sample.co.jp");
		template.update("DELETE FROM users WHERE email = :email", param);
		System.out.println("確認完了しました");
	}
	

}
