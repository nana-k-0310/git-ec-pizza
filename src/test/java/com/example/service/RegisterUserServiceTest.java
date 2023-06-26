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
import com.example.repository.UserRepository;


@SpringBootTest
@DisplayName("ユーザー登録サービス")
class RegisterUserServiceTest {
	
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

	//事前にユーザー登録する
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

	//ユーザーが登録されているか確認
	@Test
	@DisplayName("メールアドレスからユーザー情報を取得")
	void ユーザー情報取得テスト() throws Exception{
		UserInfo user1 = registerUserService.findByEmail("kono@sample.co.jp");
		assertEquals("河野夏菜", user1.getName(), "名前が登録されていません");
		assertEquals("kono@sample.co.jp", user1.getEmail(), "メールアドレスが登録されていません");
		assertEquals("KonoKono", user1.getPassword(), "パスワードが登録されていません");
		assertEquals("123-4567", user1.getZipcode(), "郵便番号が登録されていません");
		assertEquals("東京都渋谷区１−１−１", user1.getAddress(), "住所が登録されていません");
		assertEquals("03-1234-5678", user1.getTelephone(), "電話番号が登録されていません");
	}
	
	@Test
	@DisplayName("DBなしのメールアドレスからユーザー情報取得")
	void ユーザー情報メール登録null確認テスト() throws Exception {
		UserInfo user2 = registerUserService.findByEmail("konoNull@gmail.com");
		assertEquals(null, user2, "DBなしのメールアドレスからnull確認失敗");
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
