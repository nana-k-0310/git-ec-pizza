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

import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;

@SpringBootTest
@DisplayName("ログイン機能サービズ")
class LoginLogoutUserServiceTest {
	
	@Autowired
	private LoginLogoutUserService service;
	


	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void ユーザーログインテスト() throws Exception {
		LoginLogoutUserForm form = new LoginLogoutUserForm();
		form.setPassword("aaaaaaaa");
		form.setEmail("abc@gmail.com");
		UserInfo user = service.login(form);
		assertEquals(2, user.getId(), "IDが登録されていません");
		assertEquals("名字名前", user.getName(), "名前が登録されていません");
		assertEquals("abc@gmail.com", user.getEmail(), "メールアドレスが登録されていません");
		assertEquals("aaaaaaaa", user.getPassword(), "パスワードが登録されていません");
		assertEquals("111-1111", user.getZipcode(), "郵便番号が登録されていません");
		assertEquals("テスト住所", user.getAddress(), "住所が登録されていません");
		assertEquals("00-1234-5678", user.getTelephone(), "電話番号が登録されていません");
		System.out.println("確認完了しました");
	}

}
