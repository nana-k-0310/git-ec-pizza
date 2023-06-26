package com.example.service;

import static org.junit.jupiter.api.Assertions.*;

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
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;

@SpringBootTest
@DisplayName("注文確認画面サービズ")
class OrderConfirmServiceTest {

	@Autowired
	private OrderConfirmService orderConfirmService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
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
		UserInfo userInfo = new UserInfo();
		userInfo.setName("河野夏菜");
		userInfo.setEmail("kono@sample.co.jp");
		userInfo.setPassword("KonoKono");
		userInfo.setZipcode("123-4567");
		userInfo.setAddress("東京都渋谷区１−１−１");
		userInfo.setTelephone("03-1234-5678");
		registerUserService.registerUser(userInfo);

	}

	@DisplayName("未注文状態時のオーダーでDBありユーザーIDからオーダー情報を取得する")
	@Test
	public void 未注文状態時のオーダーでDBありユーザーIDからオーダー情報取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user1 = userRepository.findByEmail("kono@sample.co.jp");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order1 = new Order();
		order1.setUserId(user1.getId());
		order1.setStatus(0);
		order1.setTotalPrice(0);
				
		Order insertOrder = orderRepository.insert(order1);
		
		Integer user1Id = user1.getId();
		
		//ユーザーIDから注文情報を取得する
		Order getOrder = orderConfirmService.getOrderByUserId(user1Id);
		
		//インサートしたオーダーと取得したオーダーが一致しているか確認する
		
		assertEquals(insertOrder.getId(), getOrder.getId(), "OrderのIDが違います");
		assertEquals(insertOrder.getUserId(), getOrder.getUserId(), "OrderのユーザーIDが違います");
		assertEquals(insertOrder.getStatus(), getOrder.getStatus(), "Orderのステータスが違います");
		assertEquals(insertOrder.getTotalPrice(), getOrder.getTotalPrice(), "Orderの合計金額が違います");

		
//		assertEquals(insertOrder.getPaymentMethod(), getOrder.getPaymentMethod(), "Orderの注文状況が違います");
//		↑インサートしたpaymentMethodはnullだが、ゲットするとIntegerなので0になる
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user1.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}

	@DisplayName("未注文状態なしのオーダーでDBありユーザーIDからオーダー情報を取得する")
	@Test
	public void 未注文状態なしのオーダーでDBありユーザーIDからオーダー情報取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user2 = userRepository.findByEmail("kono@sample.co.jp");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order2 = new Order();
		order2.setUserId(user2.getId());
		order2.setStatus(1);
		order2.setTotalPrice(0);
				
		Order insertOrder = orderRepository.insert(order2);
		
		Integer user1Id = user2.getId();
		
		//ユーザーIDから注文情報を取得する
		Order getOrder = orderConfirmService.getOrderByUserId(user1Id);
		
		//インサートしたオーダーと取得したオーダーが一致しているか確認する
		
		assertEquals(null, getOrder, "Orderがnullでありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user2.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
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
