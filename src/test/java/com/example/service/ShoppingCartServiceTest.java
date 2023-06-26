package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
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
import com.example.domain.Topping;
import com.example.domain.UserInfo;
import com.example.form.ShoppingCartForm;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;
import com.example.repository.UserRepository;

@SpringBootTest
@DisplayName("ショッピングカート機能サービス")
class ShoppingCartServiceTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderToppingRepository orderToppingRepository;
	
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
	@DisplayName("注文情報インサート（オーダー（カートの中身）なし、トッピングなし）業務処理")
	void オーダーなしとトッピングなしでトッピング注文情報インサート() throws Exception{
		
		//仮ショッピングカートを作成する
		ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
		shoppingCartForm.setItemId(1);
		shoppingCartForm.setQuantity(1);
		shoppingCartForm.setSize("M");
		shoppingCartForm.setToppingIdList(null);
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//インサートする
		shoppingCartService.insertCart(shoppingCartForm, user.getId());
		
		//オーダーを取得する
		Order getOrder = orderRepository.findByUserIdAndStatus(user.getId(), 0);
		
		//オーダー商品を取得する
		List<OrderItem> getOrderItemList = orderItemRepository.sameOrderLoad(getOrder.getId());
		
		assertEquals(1, getOrderItemList.size(), "登録されたオーダー商品数が違います");
		OrderItem getOrderItem = getOrderItemList.get(0);
		
		assertNotNull(getOrderItem.getId());
		assertEquals(1, getOrderItem.getItemId(), "インサートされた商品IDが違います");
		assertEquals(getOrder.getId(), getOrderItem.getOrderId(), "インサートされたオーダーIDが違います");
		assertEquals(1, getOrderItem.getQuantity(), "インサートされた商品数が違います");
		assertEquals("M", getOrderItem.getSize(), "インサートされた商品サイズが違います");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}
	
	@Test
	@DisplayName("注文情報インサート（オーダー（カートの中身）なし、トッピングあり）業務処理")
	void オーダーなしとトッピングありでトッピング注文情報インサート() throws Exception{
		
		//仮ショッピングカートを作成する
		ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
		shoppingCartForm.setItemId(1);
		shoppingCartForm.setQuantity(1);
		shoppingCartForm.setSize("M");
		
		//インサートするトッピングリストを作成
		Topping topping1 = new Topping();
		topping1.setId(1);
		
		Topping topping2 = new Topping();
		topping2.setId(2);
		
		List<Integer> toppingIdList = new ArrayList<Integer>();
		toppingIdList.add(topping1.getId());
		toppingIdList.add(topping2.getId());
		
		//仮ショッピングカートにトッピングリストをつめる
		shoppingCartForm.setToppingIdList(toppingIdList);
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//インサートする
		shoppingCartService.insertCart(shoppingCartForm, user.getId());
		
		//オーダーを取得する
		Order getOrder = orderRepository.findByUserIdAndStatus(user.getId(), 0);
		
		//オーダー商品を取得する
		List<OrderItem> getOrderItemList = orderItemRepository.sameOrderLoad(getOrder.getId());
		
		assertEquals(1, getOrderItemList.size(), "登録されたオーダー商品数が違います");
		OrderItem getOrderItem = getOrderItemList.get(0);
		
		assertNotNull(getOrderItem.getId());
		assertEquals(1, getOrderItem.getItemId(), "インサートされた商品IDが違います");
		assertEquals(getOrder.getId(), getOrderItem.getOrderId(), "インサートされたオーダーIDが違います");
		assertEquals(1, getOrderItem.getQuantity(), "インサートされた商品数が違います");
		assertEquals("M", getOrderItem.getSize(), "インサートされた商品サイズが違います");
		
		//オーダートッピングを取得する
		List<OrderTopping> getOrderToppingList = orderToppingRepository.findOrderItemId(getOrderItem.getId());
		
		assertEquals(2, getOrderToppingList.size(), "登録されたオーダートッピング数が違います");
		OrderTopping getOrderTopping1 = getOrderToppingList.get(0);
		OrderTopping getOrderTopping2 = getOrderToppingList.get(1);
		
		assertNotNull(getOrderTopping1.getId());
		assertEquals(1, getOrderTopping1.getToppingId(), "インサートされたトッピングIDが違います");
		assertEquals(getOrderItem.getId(), getOrderTopping1.getOrderItemId(), "インサートされたオーダー商品IDが違います");
		
		assertNotNull(getOrderTopping2.getId());
		assertEquals(2, getOrderTopping2.getToppingId(), "インサートされたトッピングIDが違います");
		assertEquals(getOrderItem.getId(), getOrderTopping1.getOrderItemId(), "インサートされたオーダー商品IDが違います");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}
	
	@Test
	@DisplayName("注文情報インサート（オーダー（カートの中身）あり、トッピングなし）業務処理")
	void オーダーありとトッピングなしでトッピング注文情報インサート() throws Exception{
		
		//仮ショッピングカートを作成する
		ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
		shoppingCartForm.setItemId(1);
		shoppingCartForm.setQuantity(1);
		shoppingCartForm.setSize("M");
		shoppingCartForm.setToppingIdList(null);
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order1 = new Order();
		order1.setUserId(user.getId());
		order1.setStatus(0);
		order1.setTotalPrice(0);
						
		orderRepository.insert(order1);
		
		//インサートする
		shoppingCartService.insertCart(shoppingCartForm, user.getId());
		
		//オーダーを取得する
		Order getOrder = orderRepository.findByUserIdAndStatus(user.getId(), 0);
		
		//オーダー商品を取得する
		List<OrderItem> getOrderItemList = orderItemRepository.sameOrderLoad(getOrder.getId());
		
		assertEquals(1, getOrderItemList.size(), "登録されたオーダー商品数が違います");
		OrderItem getOrderItem = getOrderItemList.get(0);
		
		assertNotNull(getOrderItem.getId());
		assertEquals(1, getOrderItem.getItemId(), "インサートされた商品IDが違います");
		assertEquals(getOrder.getId(), getOrderItem.getOrderId(), "インサートされたオーダーIDが違います");
		assertEquals(1, getOrderItem.getQuantity(), "インサートされた商品数が違います");
		assertEquals("M", getOrderItem.getSize(), "インサートされた商品サイズが違います");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}
	
	@Test
	@DisplayName("注文情報インサート（オーダー（カートの中身）あり、トッピングあり）業務処理")
	void オーダーありとトッピングありでトッピング注文情報インサート() throws Exception{
		
		//仮ショッピングカートを作成する
		ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
		shoppingCartForm.setItemId(1);
		shoppingCartForm.setQuantity(1);
		shoppingCartForm.setSize("M");
		
		//インサートするトッピングリストを作成
		Topping topping1 = new Topping();
		topping1.setId(1);
		
		Topping topping2 = new Topping();
		topping2.setId(2);
		
		List<Integer> toppingIdList = new ArrayList<Integer>();
		toppingIdList.add(topping1.getId());
		toppingIdList.add(topping2.getId());
		
		//仮ショッピングカートにトッピングリストをつめる
		shoppingCartForm.setToppingIdList(toppingIdList);
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//インサートする
		shoppingCartService.insertCart(shoppingCartForm, user.getId());
		
		//オーダーを取得する
		Order getOrder = orderRepository.findByUserIdAndStatus(user.getId(), 0);
		
		//オーダー商品を取得する
		List<OrderItem> getOrderItemList = orderItemRepository.sameOrderLoad(getOrder.getId());
		
		assertEquals(1, getOrderItemList.size(), "登録されたオーダー商品数が違います");
		OrderItem getOrderItem = getOrderItemList.get(0);
		
		assertNotNull(getOrderItem.getId());
		assertEquals(1, getOrderItem.getItemId(), "インサートされた商品IDが違います");
		assertEquals(getOrder.getId(), getOrderItem.getOrderId(), "インサートされたオーダーIDが違います");
		assertEquals(1, getOrderItem.getQuantity(), "インサートされた商品数が違います");
		assertEquals("M", getOrderItem.getSize(), "インサートされた商品サイズが違います");
		
		//オーダートッピングを取得する
		List<OrderTopping> getOrderToppingList = orderToppingRepository.findOrderItemId(getOrderItem.getId());
		
		assertEquals(2, getOrderToppingList.size(), "登録されたオーダートッピング数が違います");
		OrderTopping getOrderTopping1 = getOrderToppingList.get(0);
		OrderTopping getOrderTopping2 = getOrderToppingList.get(1);
		
		assertNotNull(getOrderTopping1.getId());
		assertEquals(1, getOrderTopping1.getToppingId(), "インサートされたトッピングIDが違います");
		assertEquals(getOrderItem.getId(), getOrderTopping1.getOrderItemId(), "インサートされたオーダー商品IDが違います");
		
		assertNotNull(getOrderTopping2.getId());
		assertEquals(2, getOrderTopping2.getToppingId(), "インサートされたトッピングIDが違います");
		assertEquals(getOrderItem.getId(), getOrderTopping1.getOrderItemId(), "インサートされたオーダー商品IDが違います");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}
	
	@Test
	@DisplayName("オーダー（カート）が既にある状態からカート中身表示")
	void オーダーが既にある状態からカート中身表示確認テスト() throws Exception{
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order1 = new Order();
		order1.setUserId(user.getId());
		order1.setStatus(0);
		order1.setTotalPrice(0);
								
		orderRepository.insert(order1);
				
		//カート中身表示サービス
		Order getOrder = shoppingCartService.showCart(user.getId());
		
		//カートの中身を確認する.
		
		assertEquals(order1.getId(), getOrder.getId(), "表示されたオーダーのIDが違います");
		assertEquals(order1.getUserId(), getOrder.getUserId(), "表示されたオーダーのユーザーIDが違います");
		assertEquals(order1.getStatus(), getOrder.getStatus(), "表示されたオーダーの注文状況が違います");
		assertEquals(order1.getTotalPrice(), getOrder.getTotalPrice(), "表示されたオーダーの合計金額が違います");
		assertEquals(order1.getOrderDate(), getOrder.getOrderDate(), "表示されたオーダーの注文日が違います");
		assertEquals(order1.getDestinationName(), getOrder.getDestinationName(), "表示されたオーダーの注文者名が違います");
		assertEquals(order1.getDestinationEmail(), getOrder.getDestinationEmail(), "表示されたオーダーのメールアドレスが違います");
		assertEquals(order1.getDestinationZipcode(), getOrder.getDestinationZipcode(), "表示されたオーダーの郵便番号が違います");
		assertEquals(order1.getDestinationAddress(), getOrder.getDestinationAddress(), "表示されたオーダーの住所が違います");
		assertEquals(order1.getDestinationTel(), getOrder.getDestinationTel(), "表示されたオーダーの電話番号が違います");
		assertEquals(order1.getDeliveryTime(), getOrder.getDeliveryTime(), "表示されたオーダーの配達時間が違います");
		assertEquals(0, getOrder.getPaymentMethod(), "表示されたオーダーの注文状況が違います");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}
	
	@Test
	@DisplayName("オーダー（カート）がない状態からカート中身表示")
	void オーダーがない状態からカート中身表示確認テスト() throws Exception{
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//カート中身表示サービス
		Order getOrder = shoppingCartService.showCart(user.getId());
		
		//カートの中身を確認する.
		
		assertNotNull(getOrder.getId());
		assertEquals(user.getId(), getOrder.getUserId(), "表示されたオーダーのユーザーIDが違います");
		assertEquals(0, getOrder.getStatus(), "表示されたオーダーの注文状況が違います");
		assertEquals(0, getOrder.getTotalPrice(), "表示されたオーダーの合計金額が違います");
		assertEquals(null, getOrder.getOrderDate(), "表示されたオーダーの注文日が違います");
		assertEquals(null, getOrder.getDestinationName(), "表示されたオーダーの注文者名が違います");
		assertEquals(null, getOrder.getDestinationEmail(), "表示されたオーダーのメールアドレスが違います");
		assertEquals(null, getOrder.getDestinationZipcode(), "表示されたオーダーの郵便番号が違います");
		assertEquals(null, getOrder.getDestinationAddress(), "表示されたオーダーの住所が違います");
		assertEquals(null, getOrder.getDestinationTel(), "表示されたオーダーの電話番号が違います");
		assertEquals(null, getOrder.getDeliveryTime(), "表示されたオーダーの配達時間が違います");
		assertEquals(0, getOrder.getPaymentMethod(), "表示されたオーダーの注文状況が違います");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}
	
	@DisplayName("DBありのオーダー商品IDからオーダー商品削除する")
	@Test
	public void DBありオーダー商品IDからオーダー商品削除テスト() throws Exception{

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
		
		assertNotNull(orderItemRepository.load(orderItemInsert1.getId()));
		
		//オーダー商品削除する
		shoppingCartService.deleteByOrderId(orderItemInsert1.getId());
		
		//リポジトリで作成したIDがあるか確認
		assertEquals(null, orderItemRepository.load(orderItemInsert1.getId()), "オーダー商品が削除出来ていません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("オーダー商品一括削除する")
	@Test
	public void オーダー商品一括削除テスト() throws Exception{

		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
				
		//オーダーテーブルに同じユーザーのオーダー（カート）を作る
		Order order1 = new Order();
		order1.setUserId(user.getId());
		order1.setStatus(0);
		order1.setTotalPrice(0);
				
		orderRepository.insert(order1);
		
		//オーダー商品（1つ目）を作る
		OrderItem orderItem1 = new OrderItem();
		orderItem1.setItemId(1);
		orderItem1.setOrderId(order1.getId());
		orderItem1.setQuantity(1);
		orderItem1.setSize("M");
		
		//オーダー商品（1つ目）を登録する
		OrderItem orderItemInsert1 = orderItemRepository.insert(orderItem1);
		
		//オーダー商品（2つ目）を作る
		OrderItem orderItem2 = new OrderItem();
		orderItem2.setItemId(2);
		orderItem2.setOrderId(order1.getId());
		orderItem2.setQuantity(1);
		orderItem2.setSize("M");
		
		//オーダー商品（2つ目）を登録する
		OrderItem orderItemInsert2 = orderItemRepository.insert(orderItem2);
		
		//一括削除前のテーブルに中身があるか確認する
		assertNotNull(orderItemRepository.load(orderItemInsert1.getId()));
		assertNotNull(orderItemRepository.load(orderItemInsert2.getId()));
		
		//オーダー商品を一括削除する
		shoppingCartService.allDeleteOrderItem(order1.getId());
		
		//リポジトリで作成したIDがあるか確認
		assertEquals(null, orderItemRepository.load(orderItemInsert1.getId()), "オーダー商品が削除出来ていません");
		assertEquals(null, orderItemRepository.load(orderItemInsert2.getId()), "オーダー商品が削除出来ていません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	
	@AfterEach
	@DisplayName("仮ユーザーを削除する")
	void tearDown() throws Exception {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", "test@gmail.com");
		template.update("DELETE FROM users WHERE email = :email", param);
		System.out.println("確認完了しました");
	}

}
