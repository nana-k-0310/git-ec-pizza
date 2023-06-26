package com.example.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.example.domain.UserInfo;

@SpringBootTest
@DisplayName("オーダーアイテムリポジトリ")
class OrderItemRepositoryTest {

	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private UserRepository userRepository;
	
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

	@DisplayName("注文商品登録")
	@Test
	public void 注文商品登録テスト() throws Exception{

		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
				
		//オーダーテーブルに同じユーザーのオーダー（カート）を作る
		Order order1 = new Order();
		order1.setUserId(user.getId());
		order1.setStatus(0);
		order1.setTotalPrice(0);
				
		orderRepository.insert(order1);
		
		//オーダー商品を作る
		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(1);
		orderItem.setOrderId(order1.getId());
		orderItem.setQuantity(1);
		orderItem.setSize("M");
		
		//オーダー商品を登録する
		OrderItem orderItemInsert1 = orderItemRepository.insert(orderItem);
		
		//リポジトリで作成したIDがあるか確認
		assertNotNull(orderItemInsert1.getId());
		
		assertEquals(orderItem.getItemId(), orderItemInsert1.getItemId(), "オーダー商品IDの登録がありません");
		assertEquals(order1.getId(), orderItemInsert1.getOrderId(), "オーダーIDの登録がありません");
		assertEquals(orderItem.getQuantity(), orderItemInsert1.getQuantity(), "オーダー商品数の登録がありません");
		assertEquals(orderItem.getSize(), orderItemInsert1.getSize(), "オーダー商品サイズの登録がありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("DBありオーダー商品リスト検索（同じオーダーIDから）")
	@Test
	public void DBあり同じオーダーIDからDBありオーダー商品リスト検索取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user2 = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order2 = new Order();
		order2.setUserId(user2.getId());
		order2.setStatus(0);
		order2.setTotalPrice(0);
				
		Order orderInsert2 = orderRepository.insert(order2);
		System.out.println("インサートしたorder2は" + orderInsert2);
		
		
		//オーダー商品を作る
		OrderItem orderItem2 = new OrderItem();
		orderItem2.setItemId(1);
		orderItem2.setOrderId(order2.getId());
		orderItem2.setQuantity(1);
		orderItem2.setSize("M");
				
		//オーダー商品を登録する
		OrderItem orderItemInsert2 = orderItemRepository.insert(orderItem2);
		
		//OrderItemテーブルから検索する
		List<OrderItem> orderItemGetList = orderItemRepository.sameOrderLoad(order2.getId());
		
		OrderItem orderGetItem = orderItemGetList.get(0);
		
		//リポジトリで作成したIDがあるか確認
		assertNotNull(orderGetItem.getId());
		
		assertEquals(orderItem2.getItemId(), orderGetItem.getItemId(), "商品IDの登録がありません");
		assertEquals(orderItem2.getOrderId(), orderGetItem.getOrderId(), "オーダーIDの登録がありません");
		assertEquals(orderItem2.getQuantity(), orderGetItem.getQuantity(), "注文数の登録がありません");
		assertEquals(orderItem2.getSize(), orderGetItem.getSize(), "注文サイズの登録がありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user2.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("DBなしのオーダー商品でDBありのオーダーIDでオーダー商品リスト検索（同じオーダーIDから）")
	@Test
	public void DBなしオーダー商品の商品リスト検索取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user3 = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order3 = new Order();
		order3.setUserId(user3.getId());
		order3.setStatus(0);
		order3.setTotalPrice(0);
				
		orderRepository.insert(order3);
		
		//OrderItemテーブルから検索する
		List<OrderItem> orderItemGetList = orderItemRepository.sameOrderLoad(order3.getId());
		
		//取得したオーダー商品リストがnullか確認
		assertEquals(null, orderItemGetList, "オーダー商品リストがnullでありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user3.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("DBなしのオーダー商品リスト検索（同じオーダーIDから）")
	@Test
	public void DBなしのオーダー商品リスト検索取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user4 = userRepository.findByEmail("test@gmail.com");
		
		//OrderItemテーブルから検索する
		List<OrderItem> orderItemGetList = orderItemRepository.sameOrderLoad(1000);
		
		//取得したオーダー商品リストがnullか確認
		assertEquals(null, orderItemGetList, "オーダー商品リストがnullでありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user4.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("同じオーダー商品の個数を更新する")
	@Test
	public void 同じオーダー商品個数更新テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user5 = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order5 = new Order();
		order5.setUserId(user5.getId());
		order5.setStatus(0);
		order5.setTotalPrice(0);
				
		Order orderInsert5 = orderRepository.insert(order5);
		
		//オーダー商品を作る
		OrderItem orderItem5 = new OrderItem();
		orderItem5.setItemId(1);
		orderItem5.setOrderId(order5.getId());
		orderItem5.setQuantity(1);
		orderItem5.setSize("M");
				
		//オーダー商品を登録する
		OrderItem orderItemInsert5 = orderItemRepository.insert(orderItem5);
		
		//同じオーダー商品を追加し、個数を更新する
		orderItemRepository.updateCount(2, orderItemInsert5.getId());
		
		//OrderItemテーブルから検索する
		List<OrderItem> orderItemGetList = orderItemRepository.sameOrderLoad(order5.getId());
		
		assertEquals(2, orderItemGetList.get(0).getQuantity(), "商品IDの登録がありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user5.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	
	@DisplayName("注文商品を削除する")
	@Test
	public void 注文商品削除テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user6 = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order6 = new Order();
		order6.setUserId(user6.getId());
		order6.setStatus(0);
		order6.setTotalPrice(0);
				
		Order orderInsert6 = orderRepository.insert(order6);
		
		//オーダー商品を作る
		OrderItem orderItem6 = new OrderItem();
		orderItem6.setItemId(1);
		orderItem6.setOrderId(order6.getId());
		orderItem6.setQuantity(1);
		orderItem6.setSize("M");
				
		//オーダー商品を登録する
		OrderItem orderItemInsert6 = orderItemRepository.insert(orderItem6);
		
		//登録したオーダー商品を削除する.
		orderItemRepository.deleteByOrderId(orderItemInsert6.getId());
		
		//同じオーダーIDから注文商品を探し、nullかどうか確認する.
		List<OrderItem> orderItemGetList = orderItemRepository.sameOrderLoad(order6.getId());
		
		assertEquals(null, orderItemGetList, "注文商品を削除が完了していません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user6.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("注文商品を一括削除する")
	@Test
	public void 注文商品一括削除テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user7 = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order7 = new Order();
		order7.setUserId(user7.getId());
		order7.setStatus(0);
		order7.setTotalPrice(0);
				
		Order orderInsert7 = orderRepository.insert(order7);
		
		//オーダー商品を作る（1つ目）
		OrderItem orderItem71 = new OrderItem();
		orderItem71.setItemId(1);
		orderItem71.setOrderId(order7.getId());
		orderItem71.setQuantity(1);
		orderItem71.setSize("M");
				
		//オーダー商品を登録する（1つ目）
		OrderItem orderItemInsert71 = orderItemRepository.insert(orderItem71);
		
		//オーダー商品を作る（2つ目）
		OrderItem orderItem72 = new OrderItem();
		orderItem72.setItemId(2);
		orderItem72.setOrderId(order7.getId());
		orderItem72.setQuantity(1);
		orderItem72.setSize("M");
						
		//オーダー商品を登録する（2つ目）
		OrderItem orderItemInsert72 = orderItemRepository.insert(orderItem72);
		
		//登録したオーダー商品を一括削除する.
		orderItemRepository.allDeleteOrderItem(order7.getId());
		
		//同じオーダーIDから注文商品を探し、nullかどうか確認する.
		List<OrderItem> orderItemGetList = orderItemRepository.sameOrderLoad(order7.getId());
		
		assertEquals(null, orderItemGetList, "注文商品を一括削除が完了していません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user7.getId());
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
