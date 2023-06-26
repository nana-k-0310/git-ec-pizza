package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.UserInfo;
import com.example.form.OrderForm;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import com.example.service.OrderConfirmService;
import com.example.service.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("オーダー履歴コントローラー")
class OrderHistoryControllerTest {

	/**
	 * MockMvcオブジェクト
	 */
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * テスト対象クラス
	 */
	@Autowired
	private OrderHistoryController target;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderConfirmService orderConfirmService;
	
	@Autowired
	private OrderService orderService;
	
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
		
		UserInfo user1 = new UserInfo();
		user1.setName("テスト名");
		user1.setEmail("test@gmail.com");
		user1.setPassword("testtest");
		user1.setZipcode("123-4567");
		user1.setAddress("テスト住所");
		user1.setTelephone("00-1234-5678");
		userRepository.save(user1);
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
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
			
		//オーダー商品を登録する
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
			
		
	}
	
	/**
	 * テスト 
	 */
	
	@Test
	@DisplayName("注文履歴画面へ遷移する（userあり）")
	public void userありの注文履歴画面遷移テスト() throws Exception{
	
		//事前準備
			//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
				UserInfo user = userRepository.findByEmail("test@gmail.com");
				
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orderHistory").session(session);
		
		//テスト対象メソッド（showOrderHistoryメソッド）を実行
		mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/order_historyであることを確認
		.andExpect(view().name("materialize-version/order_history"));
								
	}
	
	@Test
	@DisplayName("注文履歴画面へ遷移する（userなし）")
	public void userなしの注文履歴画面遷移テスト() throws Exception{
	
		//テスト対象メソッド（showOrderHistoryメソッド）を実行
		mockMvc.perform(get("/orderHistory"))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/order_historyであることを確認
		.andExpect(view().name("materialize-version/login"));
								
	}
	
	@Test
	@DisplayName("ログイン画面へ遷移する")
	public void ログイン画面遷移テスト() throws Exception{
		
		//テスト対象メソッド（goLoginメソッド）を実行
		mockMvc.perform(get("/orderHistory/goLogin"))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/order_historyであることを確認
		.andExpect(view().name("materialize-version/login"));
		
	}
	
	@AfterEach
	@DisplayName("仮オーダーと仮ユーザーを削除する")
	public void tearDown() throws Exception {
		
		//仮オーダーを削除
		//オーダーテーブルから、このIDの情報を消す
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		SqlParameterSource param1 = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param1);
		
		//仮ユーザーを削除
		SqlParameterSource param2 = new MapSqlParameterSource().addValue("email", "test@gmail.com");
		template.update("DELETE FROM users WHERE email = :email", param2);
		
		System.out.println("確認完了しました");
	}
	
	
}
