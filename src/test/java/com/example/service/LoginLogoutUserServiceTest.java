package com.example.service;

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

import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;

@SpringBootTest
@DisplayName("ログイン機能サービズ")
class LoginLogoutUserServiceTest {
	
	@Autowired
	private LoginLogoutUserService service;
	
	@Autowired
	private RegisterUserService registerUserService;
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	@BeforeEach
	@DisplayName("仮ユーザーを登録する")
	void ユーザー登録テスト() throws Exception {
		UserInfo userInfo = new UserInfo();
		userInfo.setName("河野夏菜");
		userInfo.setEmail("kono@sample.co.jp");
		userInfo.setPassword("KonoKono");
		userInfo.setZipcode("123-4567");
		userInfo.setAddress("東京都渋谷区１−１−１");
		userInfo.setTelephone("03-1234-5678");
		registerUserService.registerUser(userInfo);

	}

	@Test
	@DisplayName("ログインする（DBあり）")
	void 正常ユーザーログインテスト() throws Exception {
		
		//ログインフォームを入力する
		LoginLogoutUserForm form = new LoginLogoutUserForm();
		form.setPassword("KonoKono");
		form.setEmail("kono@sample.co.jp");
		
		UserInfo user = service.login(form);
		
		System.out.println(user);
		
//		assertEquals(3, user.getId(), "IDが登録されていません");
		assertEquals("河野夏菜", user.getName(), "名前が登録されていません");
		assertEquals("kono@sample.co.jp", user.getEmail(), "メールアドレスが登録されていません");
		assertEquals("KonoKono", user.getPassword(), "パスワードが登録されていません");
		assertEquals("123-4567", user.getZipcode(), "郵便番号が登録されていません");
		assertEquals("東京都渋谷区１−１−１", user.getAddress(), "住所が登録されていません");
		assertEquals("03-1234-5678", user.getTelephone(), "電話番号が登録されていません");
	}
	
	@Test
	@DisplayName("ログインする（DBなしメール）")
	void メールなし不正ユーザーログインテスト() throws Exception {
		
		//ログインフォームを入力する
				LoginLogoutUserForm form = new LoginLogoutUserForm();
				form.setPassword("KonoKono");
				form.setEmail("1kono@sample.co.jp");
				
				UserInfo user = service.login(form);
				
				assertEquals(null, user, "DBなしメールですがnullになっていません");
				
	}
	
	@Test
	@DisplayName("ログインする（DBなしパスワード）")
	void パスワードなし不正ユーザーログインテスト() throws Exception {
		
		//ログインフォームを入力する
				LoginLogoutUserForm form = new LoginLogoutUserForm();
				form.setPassword("1KonoKono");
				form.setEmail("kono@sample.co.jp");
				
				UserInfo user = service.login(form);
				
				assertEquals(null, user, "DBなしパスワードですがnullになっていません");
				
	}

	
	//ユーザー情報を削除する
		@AfterEach
		@DisplayName("仮ユーザーを削除する")
		void 登録ユーザー削除() throws Exception {
			SqlParameterSource param = new MapSqlParameterSource().addValue("email", "kono@sample.co.jp");
			template.update("DELETE FROM users WHERE email = :email", param);
			System.out.println("確認完了しました");
		}
		
}
