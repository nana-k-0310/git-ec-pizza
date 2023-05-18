package com.example.repository;

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

@SpringBootTest
@DisplayName("ユーザー登録リポジトリ")
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

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
	void setUp() throws Exception {
		UserInfo user = new UserInfo();
		user.setName("テスト名");
		user.setEmail("test@gmail.com");
		user.setPassword("testtest");
		user.setZipcode("123-4567");
		user.setAddress("テスト住所");
		user.setTelephone("00-1234-5678");
		userRepository.save(user);
	}

//insertがあるときには最後に情報を削除しないとならないため、@AfterEachを最後にして記入.
//	@AfterEach
//	void tearDown() throws Exception {
//	}

	//** メールアドレスからユーザー情報を取得する./
	@Test
	void ユーザー情報メール登録テスト() throws Exception {
		UserInfo user1 = userRepository.findByEmail("test@gmail.com");
		assertEquals("テスト名", user1.getName(), "名前が登録されていません");
		assertEquals("test@gmail.com", user1.getEmail(), "メールアドレスが登録されていません");
		assertEquals("testtest", user1.getPassword(), "パスワードが登録されていません");
		assertEquals("123-4567", user1.getZipcode(), "郵便番号が登録されていません");
		assertEquals("テスト住所", user1.getAddress(), "住所が登録されていません");
		assertEquals("00-1234-5678", user1.getTelephone(), "電話番号が登録されていません");
	}

	//** パスワードとメールアドレスからユーザー情報を取得する./
	@Test
	void ユーザー情報メールパスワード登録テスト() throws Exception {
		UserInfo user2 = userRepository.fincByPasswordAndEmail("testtest", "test@gmail.com");
		assertEquals("テスト名", user2.getName(), "名前が登録されていません");
		assertEquals("test@gmail.com", user2.getEmail(), "メールアドレスが登録されていません");
		assertEquals("testtest", user2.getPassword(), "パスワードが登録されていません");
		assertEquals("123-4567", user2.getZipcode(), "郵便番号が登録されていません");
		assertEquals("テスト住所", user2.getAddress(), "住所が登録されていません");
		assertEquals("00-1234-5678", user2.getTelephone(), "電話番号が登録されていません");
	}

	@AfterEach
	@DisplayName("仮ユーザーを削除する")
	void tearDown() throws Exception {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", "test@gmail.com");
		template.update("DELETE FROM users WHERE email = :email", param);
		System.out.println("確認完了しました");
	}

}
