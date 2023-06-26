package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.validation.BindingResult;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.UserInfo;
import com.example.form.OrderForm;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("オーダーコントローラー")
class OrderControllerTest {

	/**
	 * MockMvcオブジェクト
	 */
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * テスト対象クラス
	 */
	@Autowired
	private OrderController target;
	
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
	
	/**
	 * テスト 
	 */
	
	@Test
	@DisplayName("登録画面へ遷移する")
	public void 登録画面遷移テスト() throws Exception{
		
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
	
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);
		
//		OrderForm orderForm = new OrderForm();
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orderCon/toOrderConfirm").session(session);
		
		//テスト対象メソッド（toLogoutメソッド）を実行
		mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		//次画面の遷移先が/materialize-version/order_confirmであることを確認
		.andExpect(view().name("/materialize-version/order_confirm"));
				
			//テスト後削除
			//オーダーテーブルから、このIDの情報を消す
			SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
			template.update("DELETE FROM orders WHERE user_id = :user_id", param);
			System.out.println("確認完了しました");
	
		}

	@Test
	@DisplayName("利用者情報を登録する")
	public void 利用者情報登録テスト() throws Exception{
	
		//事前準備
		
			//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
			UserInfo user = userRepository.findByEmail("test@gmail.com");
				
			//Orderテーブルにインサートする
			//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
				Order order = new Order();
				order.setUserId(user.getId());
				order.setStatus(0);
				order.setTotalPrice(0);				
			
			//オーダーを登録する
				orderRepository.insert(order);
			
			//オーダー商品を作る
				OrderItem orderItem = new OrderItem();
				orderItem.setItemId(1);
				orderItem.setOrderId(order.getId());
				orderItem.setQuantity(1);
				orderItem.setSize("M");
				
			//オーダー商品を登録する
				orderItemRepository.insert(orderItem);
		
			//ショッピングカートフォーム
			OrderForm orderForm = new OrderForm();
			
			//オーダーフォームの中身を入れる
			
			orderForm.setDestinationName(user.getName());
			orderForm.setDestinationEmail(user.getEmail());
			orderForm.setDestinationZipcode(user.getZipcode());
			orderForm.setDestinationAddress(user.getAddress());
			orderForm.setDestinationTel(user.getTelephone());
			orderForm.setDeliveryTime("14");
			orderForm.setPaymentMethod(1);
			
			String orderFormId = String.valueOf(order.getId());
			orderForm.setId(orderFormId);
			
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
			
				
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);		
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
		//テスト対象メソッド（toLogoutメソッド）を実行
		mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		//次画面の遷移先が/materialize-version/order_confirmであることを確認
		.andExpect(view().name("/materialize-version/order_finished"));
				
			//テスト後削除
			//オーダーテーブルから、このIDの情報を消す
			SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
			template.update("DELETE FROM orders WHERE user_id = :user_id", param);
			System.out.println("確認完了しました");
		
	}
	
	@Test
	@DisplayName("利用者情報を登録（メール未入力）する")
	public void メール未入力の利用者情報登録テスト() throws Exception{
	
		//事前準備
		
			//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
			UserInfo user = userRepository.findByEmail("test@gmail.com");
				
			//Orderテーブルにインサートする
			//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
				Order order = new Order();
				order.setUserId(user.getId());
				order.setStatus(0);
				order.setTotalPrice(0);				
			
			//オーダーを登録する
				orderRepository.insert(order);
			
			//オーダー商品を作る
				OrderItem orderItem = new OrderItem();
				orderItem.setItemId(1);
				orderItem.setOrderId(order.getId());
				orderItem.setQuantity(1);
				orderItem.setSize("M");
				
			//オーダー商品を登録する
				orderItemRepository.insert(orderItem);
		
			//ショッピングカートフォーム
			OrderForm orderForm = new OrderForm();
			
			//オーダーフォームの中身を入れる
			
			orderForm.setDestinationName(user.getName());
			orderForm.setDestinationEmail("");
			orderForm.setDestinationZipcode(user.getZipcode());
			orderForm.setDestinationAddress(user.getAddress());
			orderForm.setDestinationTel(user.getTelephone());
			orderForm.setDeliveryTime("14");
			orderForm.setPaymentMethod(1);
			
			String orderFormId = String.valueOf(order.getId());
			orderForm.setId(orderFormId);
			
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
			
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);		
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
		//テスト対象メソッド（toLogoutメソッド）を実行
		ResultActions results = mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		// postFormにエラーが存在すること
		.andExpect(model().attributeHasErrors("orderForm"))
		//次画面の遷移先が/materialize-version/order_confirmであることを確認
		.andExpect(view().name("/materialize-version/order_confirm"));
		
		BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "orderForm");
        String message = bindResult.getFieldError().getDefaultMessage();
        assertEquals("メールアドレスを入力して下さい", message);
		
			//テスト後削除
			//オーダーテーブルから、このIDの情報を消す
			SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
			template.update("DELETE FROM orders WHERE user_id = :user_id", param);
			System.out.println("確認完了しました");
		
	}
	
	@Test
	@DisplayName("利用者情報を登録（配達日未入力）する")
	public void 配達日未入力の利用者情報登録テスト() throws Exception{
	
		//事前準備
		
			//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
			UserInfo user = userRepository.findByEmail("test@gmail.com");
				
			//Orderテーブルにインサートする
			//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
				Order order = new Order();
				order.setUserId(user.getId());
				order.setStatus(0);
				order.setTotalPrice(0);				
			
			//オーダーを登録する
				orderRepository.insert(order);
			
			//オーダー商品を作る
				OrderItem orderItem = new OrderItem();
				orderItem.setItemId(1);
				orderItem.setOrderId(order.getId());
				orderItem.setQuantity(1);
				orderItem.setSize("M");
				
			//オーダー商品を登録する
				orderItemRepository.insert(orderItem);
		
			//ショッピングカートフォーム
			OrderForm orderForm = new OrderForm();
			
			//オーダーフォームの中身を入れる
			
			orderForm.setDestinationName(user.getName());
			orderForm.setDestinationEmail("test@gmail.com");
			orderForm.setDestinationZipcode(user.getZipcode());
			orderForm.setDestinationAddress(user.getAddress());
			orderForm.setDestinationTel(user.getTelephone());
			orderForm.setDeliveryTime("14");
			orderForm.setPaymentMethod(1);
			
			String orderFormId = String.valueOf(order.getId());
			orderForm.setId(orderFormId);
			
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);		
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
		//テスト対象メソッド（toLogoutメソッド）を実行
		ResultActions results = mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		// postFormにエラーが存在すること
		.andExpect(model().attributeHasErrors("orderForm"))
		//次画面の遷移先が/materialize-version/order_confirmであることを確認
		.andExpect(view().name("/materialize-version/order_confirm"));
		
		BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "orderForm");
        String message = bindResult.getFieldError().getDefaultMessage();
        assertEquals("配達日を入力して下さい", message);
		
			//テスト後削除
			//オーダーテーブルから、このIDの情報を消す
			SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
			template.update("DELETE FROM orders WHERE user_id = :user_id", param);
			System.out.println("確認完了しました");
		
	}
	
	@Test
	@DisplayName("利用者情報を登録（配達時間が3時間後でない場合）する")
	public void 配達時間が利用者情報登録テスト() throws Exception{
	
		//事前準備
		
			//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
			UserInfo user = userRepository.findByEmail("test@gmail.com");
				
			//Orderテーブルにインサートする
			//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
				Order order = new Order();
				order.setUserId(user.getId());
				order.setStatus(0);
				order.setTotalPrice(0);				
			
			//オーダーを登録する
				orderRepository.insert(order);
			
			//オーダー商品を作る
				OrderItem orderItem = new OrderItem();
				orderItem.setItemId(1);
				orderItem.setOrderId(order.getId());
				orderItem.setQuantity(1);
				orderItem.setSize("M");
				
			//オーダー商品を登録する
				orderItemRepository.insert(orderItem);
		
			//ショッピングカートフォーム
			OrderForm orderForm = new OrderForm();
			
			//オーダーフォームの中身を入れる
			
			orderForm.setDestinationName(user.getName());
			orderForm.setDestinationEmail("test@gmail.com");
			orderForm.setDestinationZipcode(user.getZipcode());
			orderForm.setDestinationAddress(user.getAddress());
			orderForm.setDestinationTel(user.getTelephone());
			
			orderForm.setPaymentMethod(1);
			
			String orderFormId = String.valueOf(order.getId());
			orderForm.setId(orderFormId);
			
			//DeliveryDateをセットする
				//配達日時をafterTodayに入れる（todayの1時間後）
				java.sql.Date today = new java.sql.Date(new Date().getTime());
				Calendar afterToday = Calendar.getInstance();
				afterToday.setTime(today);
				afterToday.add(Calendar.HOUR, 1);
				
				//CalendarクラスからDateクラスに変換
				Date afterDate = new Date();
				afterDate = afterToday.getTime();
				
				int hour = afterToday.get(Calendar.HOUR_OF_DAY);
				
				//String型にする
				String strHour = String.valueOf(hour);
				orderForm.setDeliveryTime(strHour);
				
				Timestamp deliveryDateTimestamp = new Timestamp(afterDate.getTime());
				//ここのフォーマットは「-」で「"yyyy-MM-dd"」としなければ日にちと時間合わせたときにフォーマット統一出来ない
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String deliveryDate = sdf.format(deliveryDateTimestamp);
				
				orderForm.setDeliveryDate(deliveryDate);
				
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);		
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
		//テスト対象メソッド（toLogoutメソッド）を実行
		ResultActions results = mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		// postFormにエラーが存在すること
		.andExpect(model().attributeHasErrors("orderForm"))
		//次画面の遷移先が/materialize-version/order_confirmであることを確認
		.andExpect(view().name("/materialize-version/order_confirm"));
		
		BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "orderForm");
        String message = bindResult.getFieldError().getDefaultMessage();
        assertEquals("今から3時間後の日時を入力して下さい", message);
		
			//テスト後削除
			//オーダーテーブルから、このIDの情報を消す
			SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
			template.update("DELETE FROM orders WHERE user_id = :user_id", param);
			System.out.println("確認完了しました");
		
	}
	
	//バリデーションチェックエラー
		@Test
		@DisplayName("利用者情報を登録（宛先氏名バリデーションエラー）する")
		public void 宛先氏名バリデーションエラーの利用者情報登録テスト() throws Exception{
	
			//事前準備
		
				//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
				UserInfo user = userRepository.findByEmail("test@gmail.com");
				
				//Orderテーブルにインサートする
				//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
					Order order = new Order();
					order.setUserId(user.getId());
					order.setStatus(0);
					order.setTotalPrice(0);				
			
					//オーダーを登録する
					orderRepository.insert(order);
			
					//オーダー商品を作る
					OrderItem orderItem = new OrderItem();
					orderItem.setItemId(1);
					orderItem.setOrderId(order.getId());
					orderItem.setQuantity(1);
					orderItem.setSize("M");
				
					//オーダー商品を登録する
					orderItemRepository.insert(orderItem);
		
					//ショッピングカートフォーム
					OrderForm orderForm = new OrderForm();
			
					//オーダーフォームの中身を入れる
			
					orderForm.setDestinationName("");
					orderForm.setDestinationEmail("test@gmail.com");
					orderForm.setDestinationZipcode(user.getZipcode());
					orderForm.setDestinationAddress("テスト住所");
					orderForm.setDestinationTel(user.getTelephone());
					orderForm.setDeliveryTime("14");
					orderForm.setPaymentMethod(1);
			
					String orderFormId = String.valueOf(order.getId());
					orderForm.setId(orderFormId);
			
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
			
					//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
					MockHttpSession session = new MockHttpSession();
					session.setAttribute("user", user);		
		
					MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
					//テスト対象メソッド（toLogoutメソッド）を実行
					ResultActions results = mockMvc.perform(requestBuilder)
							//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
							.andExpect(status().isOk())
							// postFormにエラーが存在すること
							.andExpect(model().attributeHasErrors("orderForm"))
							//次画面の遷移先が/materialize-version/order_confirmであることを確認
							.andExpect(view().name("/materialize-version/order_confirm"));
		
					BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "orderForm");
					String message = bindResult.getFieldError().getDefaultMessage();
					assertEquals("名前は必須です", message);
		
					//テスト後削除
					//オーダーテーブルから、このIDの情報を消す
					SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
					template.update("DELETE FROM orders WHERE user_id = :user_id", param);
					System.out.println("確認完了しました");
		
		}
		
		@Test
		@DisplayName("利用者情報を登録（メール形式不正バリデーションエラー）する")
		public void メール形式不正バリデーションエラーの利用者情報登録テスト() throws Exception{
	
			//事前準備
		
				//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
				UserInfo user = userRepository.findByEmail("test@gmail.com");
				
				//Orderテーブルにインサートする
				//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
					Order order = new Order();
					order.setUserId(user.getId());
					order.setStatus(0);
					order.setTotalPrice(0);				
			
					//オーダーを登録する
					orderRepository.insert(order);
			
					//オーダー商品を作る
					OrderItem orderItem = new OrderItem();
					orderItem.setItemId(1);
					orderItem.setOrderId(order.getId());
					orderItem.setQuantity(1);
					orderItem.setSize("M");
				
					//オーダー商品を登録する
					orderItemRepository.insert(orderItem);
		
					//ショッピングカートフォーム
					OrderForm orderForm = new OrderForm();
			
					//オーダーフォームの中身を入れる
			
					orderForm.setDestinationName("テスト名");
					orderForm.setDestinationEmail("testgmail.com");
					orderForm.setDestinationZipcode(user.getZipcode());
					orderForm.setDestinationAddress("テスト住所");
					orderForm.setDestinationTel(user.getTelephone());
					orderForm.setDeliveryTime("14");
					orderForm.setPaymentMethod(1);
			
					String orderFormId = String.valueOf(order.getId());
					orderForm.setId(orderFormId);
			
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
			
					//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
					MockHttpSession session = new MockHttpSession();
					session.setAttribute("user", user);		
		
					MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
					//テスト対象メソッド（toLogoutメソッド）を実行
					ResultActions results = mockMvc.perform(requestBuilder)
							//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
							.andExpect(status().isOk())
							// postFormにエラーが存在すること
							.andExpect(model().attributeHasErrors("orderForm"))
							//次画面の遷移先が/materialize-version/order_confirmであることを確認
							.andExpect(view().name("/materialize-version/order_confirm"));
		
					BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "orderForm");
					String message = bindResult.getFieldError().getDefaultMessage();
					assertEquals("Eメールの形式が不正です", message);
		
					//テスト後削除
					//オーダーテーブルから、このIDの情報を消す
					SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
					template.update("DELETE FROM orders WHERE user_id = :user_id", param);
					System.out.println("確認完了しました");
		
		}
		
		@Test
		@DisplayName("利用者情報を登録（郵便番号形式不正バリデーションエラー）する")
		public void 郵便番号形式不正バリデーションエラーの利用者情報登録テスト() throws Exception{
	
			//事前準備
		
				//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
				UserInfo user = userRepository.findByEmail("test@gmail.com");
				
				//Orderテーブルにインサートする
				//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
					Order order = new Order();
					order.setUserId(user.getId());
					order.setStatus(0);
					order.setTotalPrice(0);				
			
					//オーダーを登録する
					orderRepository.insert(order);
			
					//オーダー商品を作る
					OrderItem orderItem = new OrderItem();
					orderItem.setItemId(1);
					orderItem.setOrderId(order.getId());
					orderItem.setQuantity(1);
					orderItem.setSize("M");
				
					//オーダー商品を登録する
					orderItemRepository.insert(orderItem);
		
					//ショッピングカートフォーム
					OrderForm orderForm = new OrderForm();
			
					//オーダーフォームの中身を入れる
			
					orderForm.setDestinationName("テスト名");
					orderForm.setDestinationEmail("test@gmail.com");
					orderForm.setDestinationZipcode("123-456");
					orderForm.setDestinationAddress("テスト住所");
					orderForm.setDestinationTel(user.getTelephone());
					orderForm.setDeliveryTime("14");
					orderForm.setPaymentMethod(1);
			
					String orderFormId = String.valueOf(order.getId());
					orderForm.setId(orderFormId);
			
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
			
					//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
					MockHttpSession session = new MockHttpSession();
					session.setAttribute("user", user);		
		
					MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
					//テスト対象メソッド（toLogoutメソッド）を実行
					ResultActions results = mockMvc.perform(requestBuilder)
							//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
							.andExpect(status().isOk())
							// postFormにエラーが存在すること
							.andExpect(model().attributeHasErrors("orderForm"))
							//次画面の遷移先が/materialize-version/order_confirmであることを確認
							.andExpect(view().name("/materialize-version/order_confirm"));
		
					BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "orderForm");
					String message = bindResult.getFieldError().getDefaultMessage();
					assertEquals("郵便番号形式にしてください", message);
		
					//テスト後削除
					//オーダーテーブルから、このIDの情報を消す
					SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
					template.update("DELETE FROM orders WHERE user_id = :user_id", param);
					System.out.println("確認完了しました");
		
		}
	
		@Test
		@DisplayName("利用者情報を登録（住所未入力バリデーションエラー）する")
		public void 住所未入力バリデーションエラーの利用者情報登録テスト() throws Exception{
	
			//事前準備
		
				//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
				UserInfo user = userRepository.findByEmail("test@gmail.com");
				
				//Orderテーブルにインサートする
				//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
					Order order = new Order();
					order.setUserId(user.getId());
					order.setStatus(0);
					order.setTotalPrice(0);				
			
					//オーダーを登録する
					orderRepository.insert(order);
			
					//オーダー商品を作る
					OrderItem orderItem = new OrderItem();
					orderItem.setItemId(1);
					orderItem.setOrderId(order.getId());
					orderItem.setQuantity(1);
					orderItem.setSize("M");
				
					//オーダー商品を登録する
					orderItemRepository.insert(orderItem);
		
					//ショッピングカートフォーム
					OrderForm orderForm = new OrderForm();
			
					//オーダーフォームの中身を入れる
			
					orderForm.setDestinationName("テスト名");
					orderForm.setDestinationEmail("test@gmail.com");
					orderForm.setDestinationZipcode("123-4567");
					orderForm.setDestinationAddress("");
					orderForm.setDestinationTel(user.getTelephone());
					orderForm.setDeliveryTime("14");
					orderForm.setPaymentMethod(1);
			
					String orderFormId = String.valueOf(order.getId());
					orderForm.setId(orderFormId);
			
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
			
					//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
					MockHttpSession session = new MockHttpSession();
					session.setAttribute("user", user);		
		
					MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
					//テスト対象メソッド（toLogoutメソッド）を実行
					ResultActions results = mockMvc.perform(requestBuilder)
							//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
							.andExpect(status().isOk())
							// postFormにエラーが存在すること
							.andExpect(model().attributeHasErrors("orderForm"))
							//次画面の遷移先が/materialize-version/order_confirmであることを確認
							.andExpect(view().name("/materialize-version/order_confirm"));
		
					BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "orderForm");
					String message = bindResult.getFieldError().getDefaultMessage();
					assertEquals("住所を入力して下さい", message);
		
					//テスト後削除
					//オーダーテーブルから、このIDの情報を消す
					SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
					template.update("DELETE FROM orders WHERE user_id = :user_id", param);
					System.out.println("確認完了しました");
		
		}
		
		@Test
		@DisplayName("利用者情報を登録（宛先電話番号形式バリデーションエラー）する")
		public void 宛先電話番号形式バリデーションエラーの利用者情報登録テスト() throws Exception{
	
			//事前準備
		
				//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
				UserInfo user = userRepository.findByEmail("test@gmail.com");
				
				//Orderテーブルにインサートする
				//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
					Order order = new Order();
					order.setUserId(user.getId());
					order.setStatus(0);
					order.setTotalPrice(0);				
			
					//オーダーを登録する
					orderRepository.insert(order);
			
					//オーダー商品を作る
					OrderItem orderItem = new OrderItem();
					orderItem.setItemId(1);
					orderItem.setOrderId(order.getId());
					orderItem.setQuantity(1);
					orderItem.setSize("M");
				
					//オーダー商品を登録する
					orderItemRepository.insert(orderItem);
		
					//ショッピングカートフォーム
					OrderForm orderForm = new OrderForm();
			
					//オーダーフォームの中身を入れる
			
					orderForm.setDestinationName("テスト名");
					orderForm.setDestinationEmail("test@gmail.com");
					orderForm.setDestinationZipcode("123-4567");
					orderForm.setDestinationAddress("テスト住所");
					orderForm.setDestinationTel("0-1234-5678");
					orderForm.setDeliveryTime("14");
					orderForm.setPaymentMethod(1);
			
					String orderFormId = String.valueOf(order.getId());
					orderForm.setId(orderFormId);
			
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
			
					//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
					MockHttpSession session = new MockHttpSession();
					session.setAttribute("user", user);		
		
					MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
					//テスト対象メソッド（toLogoutメソッド）を実行
					ResultActions results = mockMvc.perform(requestBuilder)
							//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
							.andExpect(status().isOk())
							// postFormにエラーが存在すること
							.andExpect(model().attributeHasErrors("orderForm"))
							//次画面の遷移先が/materialize-version/order_confirmであることを確認
							.andExpect(view().name("/materialize-version/order_confirm"));
		
					BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "orderForm");
					String message = bindResult.getFieldError().getDefaultMessage();
					assertEquals("電話番号はxxxx-xxxx-xxxxの形式で入力して下さい", message);
		
					//テスト後削除
					//オーダーテーブルから、このIDの情報を消す
					SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
					template.update("DELETE FROM orders WHERE user_id = :user_id", param);
					System.out.println("確認完了しました");
		
		}
	
		@Test
		@DisplayName("利用者情報を登録（配達時間未入力バリデーションエラー）する")
		public void 配達時間未入力バリデーションエラーの利用者情報登録テスト() throws Exception{
	
			//事前準備
		
				//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
				UserInfo user = userRepository.findByEmail("test@gmail.com");
				
				//Orderテーブルにインサートする
				//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
					Order order = new Order();
					order.setUserId(user.getId());
					order.setStatus(0);
					order.setTotalPrice(0);				
			
					//オーダーを登録する
					orderRepository.insert(order);
			
					//オーダー商品を作る
					OrderItem orderItem = new OrderItem();
					orderItem.setItemId(1);
					orderItem.setOrderId(order.getId());
					orderItem.setQuantity(1);
					orderItem.setSize("M");
				
					//オーダー商品を登録する
					orderItemRepository.insert(orderItem);
		
					//ショッピングカートフォーム
					OrderForm orderForm = new OrderForm();
			
					//オーダーフォームの中身を入れる
			
					orderForm.setDestinationName("テスト名");
					orderForm.setDestinationEmail(user.getEmail());
					orderForm.setDestinationZipcode(user.getZipcode());
					orderForm.setDestinationAddress("テスト住所");
					orderForm.setDestinationTel(user.getTelephone());
					orderForm.setDeliveryTime("");
					orderForm.setPaymentMethod(1);
			
					String orderFormId = String.valueOf(order.getId());
					orderForm.setId(orderFormId);
			
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
			
					//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
					MockHttpSession session = new MockHttpSession();
					session.setAttribute("user", user);		
		
					MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/orderCon/order").flashAttr("orderForm", orderForm).session(session);
		
					//テスト対象メソッド（toLogoutメソッド）を実行
					ResultActions results = mockMvc.perform(requestBuilder)
							//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
							.andExpect(status().isOk())
							// postFormにエラーが存在すること
							.andExpect(model().attributeHasErrors("orderForm"))
							//次画面の遷移先が/materialize-version/order_confirmであることを確認
							.andExpect(view().name("/materialize-version/order_confirm"));
		
					BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "orderForm");
					String message = bindResult.getFieldError().getDefaultMessage();
					assertEquals("配達時間を入力して下さい", message);
		
					//テスト後削除
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
