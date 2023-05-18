package com.example.service;

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
import com.example.form.ShoppingCartForm;
import com.example.repository.UserRepository;

@SpringBootTest
@DisplayName("ショッピングカート機能サービス")
class ShoppingCartServiceTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	@DisplayName("仮ユーザーを登録する")
	//仮ユーザーのIDは3になる
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

	@Test
	@DisplayName("注文情報インサート業務処理")
	void 注文情報インサート() throws Exception{
		
		//仮ショッピングカートを作成する
		ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
		shoppingCartForm.setItemId(1);
		shoppingCartForm.setQuantity(1);
		shoppingCartForm.setSize("M");
		shoppingCartForm.setToppingIdList(null);
	}
	
	@AfterEach
	@DisplayName("仮ユーザーを削除する")
	void tearDown() throws Exception {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", "test@gmail.com");
		template.update("DELETE FROM users WHERE email = :email", param);
		System.out.println("確認完了しました");
	}

}
