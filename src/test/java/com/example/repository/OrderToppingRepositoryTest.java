package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

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
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.UserInfo;

@SpringBootTest
@DisplayName("注文トッピングリポジトリ")
class OrderToppingRepositoryTest {

	@Autowired
	private OrderToppingRepository orderToppingRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
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
	
	@DisplayName("注文トッピングを登録する")
	@Test
	public void 注文トッピング登録テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
				
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order1 = new Order();
		order1.setUserId(user.getId());
		order1.setStatus(0);
		order1.setTotalPrice(0);
				
		orderRepository.insert(order1);
				
		//オーダー商品を作る
		OrderItem orderItem1 = new OrderItem();
		orderItem1.setItemId(1);
		orderItem1.setOrderId(order1.getId());
		orderItem1.setQuantity(1);
		orderItem1.setSize("M");
								
		//オーダー商品を登録する
		orderItemRepository.insert(orderItem1);
		
		//オーダートッピングを作る
		OrderTopping orderTopping1 = new OrderTopping();
		
		orderTopping1.setToppingId(1);
		orderTopping1.setOrderItemId(orderItem1.getId());
		
		System.out.println("orderTopping1は" + orderTopping1);
		
		//オーダートッピングを登録する
		OrderTopping orderToppingInsert1 = orderToppingRepository.insert(orderTopping1);
		
		System.out.println("インサートしたorderToppingInsert1は" + orderToppingInsert1);
		
		//リポジトリで作成したIDがあるか確認
		assertNotNull(orderToppingInsert1.getId());
		
		assertEquals(1, orderToppingInsert1.getToppingId(), "トッピングIDが登録されていません");
		assertEquals(orderItem1.getId(), orderToppingInsert1.getOrderItemId(), "注文商品IDが登録されていません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("DBありの注文トッピングリストを取得する")
	@Test
	public void DBあり注文トッピングリスト取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
				
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order1 = new Order();
		order1.setUserId(user.getId());
		order1.setStatus(0);
		order1.setTotalPrice(0);
				
		orderRepository.insert(order1);
				
		//オーダー商品を作る
		OrderItem orderItem1 = new OrderItem();
		orderItem1.setItemId(1);
		orderItem1.setOrderId(order1.getId());
		orderItem1.setQuantity(1);
		orderItem1.setSize("M");
								
		//オーダー商品を登録する
		orderItemRepository.insert(orderItem1);
		
		//オーダートッピング（1つ目）を作る
		OrderTopping orderTopping1 = new OrderTopping();
		
		orderTopping1.setToppingId(1);
		orderTopping1.setOrderItemId(orderItem1.getId());
		
		//オーダートッピング（1つ目）を登録する
		orderToppingRepository.insert(orderTopping1);
		
		//オーダートッピング（2つ目）を作る
		OrderTopping orderTopping2 = new OrderTopping();
				
		orderTopping2.setToppingId(2);
		orderTopping2.setOrderItemId(orderItem1.getId());
				
		//オーダートッピング（2つ目）を登録する
		orderToppingRepository.insert(orderTopping2);
		
		//登録したオーダー商品のトッピングリストを取得する
		List<OrderTopping> orderToppingList = orderToppingRepository.findOrderItemId(orderItem1.getId());
		
		OrderTopping orderToppingGet1 = orderToppingList.get(0);
		OrderTopping orderToppingGet2 = orderToppingList.get(1); 
		
		assertNotNull(orderToppingGet1.getId());
		assertEquals(1, orderToppingGet1.getToppingId(), "トッピングIDが登録されていません");
		assertEquals(orderItem1.getId(), orderToppingGet1.getOrderItemId(), "注文商品IDが登録されていません");
		
		assertNotNull(orderToppingGet2.getId());
		assertEquals(2, orderToppingGet2.getToppingId(), "トッピングIDが登録されていません");
		assertEquals(orderItem1.getId(), orderToppingGet2.getOrderItemId(), "注文商品IDが登録されていません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	
	@AfterEach
	@DisplayName("仮ユーザーを削除する")
	public void tearDown() throws Exception {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", "test@gmail.com");
		template.update("DELETE FROM users WHERE email = :email", param);
		System.out.println("確認完了しました");
	}

}
