package com.example.repository;

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

import com.example.domain.Order;
import com.example.domain.UserInfo;

@SpringBootTest
@DisplayName("オーダーリポジトリ")
class OrderRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private OrderRepository orderRepository;
	
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

	@DisplayName("注文情報登録")
	@Test
	void 注文情報登録する() throws Exception{
		//仮ユーザーログインする
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order1 = new Order();
		order1.setUserId(user.getId());
		order1.setStatus(0);
		order1.setTotalPrice(1639);
		//?後で記入
		
		Order orderInsert1 = orderRepository.insert(order1);
	}
	
	
	@DisplayName("注文状態検索（ユーザーIDと状態から）")
	@Test
	void 注文状態ID状態検索取得() throws Exception {
		Order order1 = orderRepository. findByUserIdAndStatus(3, 0);
		System.out.println(order1);
	}
	
	@AfterEach
	@DisplayName("仮ユーザーを削除する")
	void tearDown() throws Exception {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", "test@gmail.com");
		template.update("DELETE FROM users WHERE email = :email", param);
		System.out.println("確認完了しました");
	}

}
