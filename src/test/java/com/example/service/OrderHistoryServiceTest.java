package com.example.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.util.SystemPropertyUtils;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.UserInfo;
import com.example.form.OrderForm;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;

@SpringBootTest
@DisplayName("注文履歴表示サービズ")
class OrderHistoryServiceTest {

	@Autowired
	private RegisterUserService registerUserService;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderConfirmService orderConfirmService;
	
	@Autowired
	private OrderHistoryService orderHistoryService;
	
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

	@Test
	@DisplayName("ユーザーIDから注文履歴情報を取得する（DBあり）")
	void DBありの注文履歴情報取得テスト() throws Exception {
	
			//事前準備
			//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
			UserInfo user = userRepository.findByEmail("kono@sample.co.jp");
				
			//Orderテーブルにインサートする
			//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
			Order order = new Order();
			order.setUserId(user.getId());
			order.setStatus(0);
			order.setTotalPrice(0);
		
			orderRepository.insert(order);
		
			//オーダー商品を作る
			OrderItem orderItem = new OrderItem();
			orderItem.setItemId(1);
			orderItem.setOrderId(order.getId());
			orderItem.setQuantity(1);
			orderItem.setSize("M");
		
			orderItemRepository.insert(orderItem);
		
			Order getOrder = orderConfirmService.getOrderByUserId(user.getId());
		
			//ショッピングカートフォーム
			OrderForm orderForm = new OrderForm();
		
			//オーダーフォームの中身を入れる
		
			String strId = String.valueOf(getOrder.getId());
			orderForm.setId(strId);
		
			orderForm.setDestinationName(user.getName());
			orderForm.setDestinationEmail(user.getEmail());
			orderForm.setDestinationZipcode(user.getZipcode());
			orderForm.setDestinationAddress(user.getAddress());
			orderForm.setDestinationTel(user.getTelephone());
			orderForm.setDeliveryTime("14");
			orderForm.setPaymentMethod(1);
		
			//DeliveryDateをセットする
			//formに配達日時を入れる
			java.sql.Date today = new java.sql.Date(new Date().getTime());
			Calendar afterToday = Calendar.getInstance();
			afterToday.setTime(today);
			afterToday.add(Calendar.MONTH, 1);
		
			//CalendarクラスからDateクラスに変換
			Date afterDate = new Date();
			afterDate = afterToday.getTime();
			Timestamp deliveryDateTimestamp = new Timestamp(afterDate.getTime());
			//ここのフォーマットは「-」で「"yyyy-MM-dd"」としなければ日にちと時間合わせたときにフォーマット統一出来ない
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String deliveryDate = sdf.format(deliveryDateTimestamp);
		
			orderForm.setDeliveryDate(deliveryDate);
		
			//利用者情報登録
			Order registerOrder = orderService.order(orderForm);
		
			//注文確認
			Order confirmOrder = orderRepository.findByUserIdAndStatus(user.getId(), 1);
		
			System.out.println("注文した利用者情報は" + confirmOrder);
		
		//テスト
			
		List<Order> orderList = orderHistoryService.showOrderHistory(user.getId());
			
		for(Order testOrder : orderList) {
			
			assertEquals(registerOrder.getId(), testOrder.getId(), "IDが違います");
			assertEquals(registerOrder.getUserId(), testOrder.getUserId(), "ユーザーIDが違います");
			assertEquals(registerOrder.getStatus(), testOrder.getStatus(), "ステータスが違います");
			assertEquals(registerOrder.getTotalPrice(), testOrder.getTotalPrice(), "合計金額が違います");
			
			//OrderDateはOrderServiceでセット後更新していないが、「testOrder.getOrderDate()」と「registerOrder.getOrderDate()」はイコールにならない
			
			System.out.println("testOrder.getOrderDate()は" + testOrder.getOrderDate());
			System.out.println("registerOrder.getOrderDate()は" + registerOrder.getOrderDate());
			
			System.out.println("testOrderは" + testOrder);
			System.out.println("registerOrderは" + registerOrder);
			
//			String strDate = date.toString();
			String registerStrDate = registerOrder.getOrderDate().toString();
			String testStrDate = testOrder.getOrderDate().toString();
			
			assertEquals(registerStrDate, testStrDate, "注文日が違います");
//			assertEquals(registerOrder.getOrderDate(), testOrder.getOrderDate(), "注文日が違います");
			
			assertEquals(registerOrder.getDestinationName(), testOrder.getDestinationName(), "注文者名が違います");
			assertEquals(registerOrder.getDestinationEmail(), testOrder.getDestinationEmail(), "注文者メールアドレスが違います");
			assertEquals(registerOrder.getDestinationZipcode(), testOrder.getDestinationZipcode(), "注文者郵便番号が違います");
			assertEquals(registerOrder.getDestinationAddress(), testOrder.getDestinationAddress(), "注文者住所が違います");
			assertEquals(registerOrder.getDestinationTel(), testOrder.getDestinationTel(), "注文者電話番号が違います");
			assertEquals(registerOrder.getPaymentMethod(), testOrder.getPaymentMethod(), "支払方法が違います");
			assertEquals(registerOrder.getUser(), testOrder.getUser(), "ユーザーが違います");
			
			for(OrderItem registerOrderItem : registerOrder.getOrderItemList()) {
				for(OrderItem testOrderItem : testOrder.getOrderItemList()) {
					 
			assertEquals(registerOrderItem.getId(), testOrderItem.getId(), "オーダー商品のIDが違います");
			assertEquals(registerOrderItem.getItemId(), testOrderItem.getItemId(), "オーダー商品の商品IDが違います");
			assertEquals(registerOrderItem.getOrderId(), testOrderItem.getOrderId(), "オーダー商品のオーダーIDが違います");
			assertEquals(registerOrderItem.getQuantity(), testOrderItem.getQuantity(), "オーダー商品の注文数が違います");
			assertEquals(registerOrderItem.getSize(), testOrderItem.getSize(), "オーダー商品のサイズが違います");
			assertEquals(0, testOrderItem.getOrderToppingList().size(), "オーダー商品のオーダートッピングリストが違います");
			
			
			assertEquals(1, testOrderItem.getItem().getId(), "商品のIDが違います");
			assertEquals("じゃがバターベーコン", testOrderItem.getItem().getName(), "商品名が違います");
			assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", testOrderItem.getItem().getDescription(), "商品の説明が違います");
			assertEquals(1490, testOrderItem.getItem().getPriceM(), "商品Mの金額が違います");
			assertEquals(2570, testOrderItem.getItem().getPriceL(), "商品Lの金額が違います");
			assertEquals("1.jpg", testOrderItem.getItem().getImagePath(), "商品の画像が違います");
			assertEquals(false, testOrderItem.getItem().getDeleted(), "商品の削除情報が違います");
			assertEquals(null, testOrderItem.getItem().getToppingList(), "商品のトッピングリストが違います");
			assertEquals(null, testOrderItem.getItem().getToppingIdList(), "商品のトッピングIDリストが違います");
			
				}
				
			}
			
		}
		
		//テスト後削除
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}	
	
	@Test
	@DisplayName("ユーザーIDから注文履歴情報を取得する（DBなし）")
	void DBなしの注文履歴情報取得テスト() throws Exception {
	
			//事前準備
			//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
			UserInfo user = userRepository.findByEmail("kono@sample.co.jp");
				
			//Orderテーブルにインサートする
			//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
			Order order = new Order();
			order.setUserId(user.getId());
			order.setStatus(0);
			order.setTotalPrice(0);
		
			orderRepository.insert(order);
		
			//オーダー商品を作る
			OrderItem orderItem = new OrderItem();
			orderItem.setItemId(1);
			orderItem.setOrderId(order.getId());
			orderItem.setQuantity(1);
			orderItem.setSize("M");
		
			orderItemRepository.insert(orderItem);
		
			orderConfirmService.getOrderByUserId(user.getId());
		
		//テスト
			
		List<Order> orderList = orderHistoryService.showOrderHistory(user.getId());
			
			assertEquals(null, orderList, "注文履歴が正常でありません");
			
		//テスト後削除
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}	
	
	@AfterEach
	@DisplayName("仮ユーザーを削除する")
	void tearDown() throws Exception {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", "kono@sample.co.jp");
		template.update("DELETE FROM users WHERE email = :email", param);
		System.out.println("確認完了しました");
	}
	
}
