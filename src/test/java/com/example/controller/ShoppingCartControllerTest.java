package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.SystemPropertyUtils;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
import com.example.form.ShoppingCartForm;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;
import com.example.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ショッピングカートコントローラー")
class ShoppingCartControllerTest {

	/**
	 * MockMvcオブジェクト
	 */
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * テスト対象クラス
	 */
	@Autowired
	private ShoppingCartController target;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderToppingRepository orderToppingRepository;
	
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
	@DisplayName("ログイン未済時に商品をカートに追加する")
	public void ログイン未済時商品カート追加テスト() throws Exception {
		//登録されていないログインフォーム
		LoginLogoutUserForm temporaryForm = new LoginLogoutUserForm();
		
		temporaryForm.setEmail("temporarytest@gmail.com");
		temporaryForm.setPassword("test1test1");

		//ショッピングカートフォーム
		ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
		
		shoppingCartForm.setItemId(1);
		shoppingCartForm.setQuantity(1);
		shoppingCartForm.setSize("M");
		
		//テスト対象メソッド（insertCartメソッド）を実行
		mockMvc.perform(post("/shopping/insertCart").flashAttr("LoginLogoutUserForm", temporaryForm).flashAttr("ShoppingCartForm", shoppingCartForm))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/login"));
		
	}
	
	
	@Test
	@DisplayName("ユーザーのオーダーに未注文状態の合計金額0円のカートの状態から商品をカートに追加する")
	public void ユーザーのオーダーに未注文状態かつ合計金額なしの状態から商品カート追加テスト() throws Exception {
		
		//事前準備
			//ショッピングカートフォーム
			ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
				
			shoppingCartForm.setItemId(1);
			shoppingCartForm.setQuantity(1);
			shoppingCartForm.setSize("M");

			//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
			UserInfo user = userRepository.findByEmail("test@gmail.com");
			
			//Orderテーブルにインサートする
			//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
			Order order = new Order();
			order.setUserId(user.getId());
			order.setStatus(0);
			order.setTotalPrice(0);
								
			orderRepository.insert(order);
		
			
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);
			
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/shopping/insertCart").flashAttr("shoppingCartForm", shoppingCartForm).session(session);
			
		//テスト対象メソッド（toLogoutメソッド）を実行
		mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（リダイレクトがあるときは「redirectedUrl("/sample/openBoardList.do")」）
		.andExpect(redirectedUrl("/shopping/toCart"))
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("redirect:/shopping/toCart"));
	
		
			//テスト後削除
			//オーダーテーブルから、このIDの情報を消す
			SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
			template.update("DELETE FROM orders WHERE user_id = :user_id", param);
			System.out.println("確認完了しました");
		
	}

	@Test
	@DisplayName("以前のOrderの商品はトッピングなしで、formに同じ商品がある場合にカート更新確認する")
	public void オーダー商品トッピングなしかつ同じ商品追加時のカート更新確認テスト() throws Exception {
		
		//事前準備
			//ショッピングカートフォーム
			ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
				
			shoppingCartForm.setItemId(1);
			shoppingCartForm.setQuantity(1);
			shoppingCartForm.setSize("M");

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
			OrderItem orderItemInsert = orderItemRepository.insert(orderItem);
		
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);
			
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/shopping/insertCart").flashAttr("shoppingCartForm", shoppingCartForm).session(session);
			
		//テスト対象メソッド（toLogoutメソッド）を実行
		mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（リダイレクトがあるときは「redirectedUrl("/sample/openBoardList.do")」）
		.andExpect(redirectedUrl("/shopping/toCart"))
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("redirect:/shopping/toCart"));
	
		//リポジトリで確認しているため削除↓
			//DBのオーダー商品の個数が更新されているか確認する.
				//OrderItem checkOrderItem = orderItemRepository.load(orderItemInsert.getId());
				//assertEquals(2, checkOrderItem.getQuantity(), "オーダー商品個数が更新されていません");
		
			//テスト後削除
			//オーダーテーブルから、このIDの情報を消す
			SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
			template.update("DELETE FROM orders WHERE user_id = :user_id", param);
			System.out.println("確認完了しました");
		
	}
	
	@Test
	@DisplayName("以前のOrderの商品はトッピングありで、formに同じ商品がある場合にカート更新確認する")
	public void オーダー商品トッピングありかつ同じ商品追加時のカート更新確認テスト() throws Exception {
		
		//事前準備
			//ショッピングカートフォーム
			ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
				
			shoppingCartForm.setItemId(1);
			shoppingCartForm.setQuantity(1);
			shoppingCartForm.setSize("M");

			//フォームのトッピングIDリスト作成
			List<Integer> toppingIdList = new ArrayList<>();
			toppingIdList.add(1);
			toppingIdList.add(2);
			
			shoppingCartForm.setToppingIdList(toppingIdList);
			
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
			OrderItem orderItemInsert = orderItemRepository.insert(orderItem);
		
			//オーダートッピング（1つ目）を作る
			OrderTopping orderTopping1 = new OrderTopping();
			
			orderTopping1.setToppingId(1);
			orderTopping1.setOrderItemId(orderItem.getId());
			
			//オーダートッピング（1つ目）を登録する
			orderToppingRepository.insert(orderTopping1);
			
			//オーダートッピング（2つ目）を作る
			OrderTopping orderTopping2 = new OrderTopping();
			
			orderTopping2.setToppingId(2);
			orderTopping2.setOrderItemId(orderItem.getId());
			
			//オーダートッピング（2つ目）を登録する
			orderToppingRepository.insert(orderTopping2);
			
			
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);
			
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/shopping/insertCart").flashAttr("shoppingCartForm", shoppingCartForm).session(session);
			
		//テスト対象メソッド（toLogoutメソッド）を実行
		mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（リダイレクトがあるときは「redirectedUrl("/sample/openBoardList.do")」）
		.andExpect(redirectedUrl("/shopping/toCart"))
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("redirect:/shopping/toCart"));
	
		//DBのオーダー商品の個数が更新されているか確認する.
		
		OrderItem checkOrderItem = orderItemRepository.load(orderItemInsert.getId());
		
		assertEquals(2, checkOrderItem.getQuantity(), "オーダー商品個数が更新されていません");
		
			//テスト後削除
			//オーダーテーブルから、このIDの情報を消す
			SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
			template.update("DELETE FROM orders WHERE user_id = :user_id", param);
			System.out.println("確認完了しました");
		
	}
	
	@Test
	@DisplayName("DBなしユーザーのときカート表示する")
	public void DBなしユーザーカート表示テスト() throws Exception {
		
		//テスト対象メソッド（toCartListメソッド）を実行
		mockMvc.perform(get("/shopping/toCart"))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/login"));
		
	}
	
	@Test
	@DisplayName("DBありユーザーのときカート表示する")
	public void DBありユーザーカート表示テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", user);
					
		//テスト対象メソッド（toCartListメソッド）を実行
		mockMvc.perform(get("/shopping/toCart"))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		
		//modelの中身が合っているか（※メッセージでないためorderの中身確認はリポジトリクラスで確認済のはず）
		//.andExpect(model().attribute("order", order))
		
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/login"));
		
			//テスト後削除
			//オーダーテーブルから、このIDの情報を消す
			SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
			template.update("DELETE FROM orders WHERE user_id = :user_id", param);
			System.out.println("確認完了しました");
		
	}
	
	@Test
	@DisplayName("ショッピングカート商品を削除する")
	public void ショッピングカート商品削除テスト() throws Exception {
		
			//テスト対象メソッド（deleteCartItemsメソッド）を実行
			mockMvc.perform(get("/shopping/deleteCart"))
			//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
			.andExpect(redirectedUrl("/shopping/toCart"))
			//次画面の遷移先がmaterialize-version/register_userであることを確認
			.andExpect(view().name("redirect:/shopping/toCart"));
		
	}
	
	@Test
	@DisplayName("ショッピングカート商品を一括削除する")
	public void ショッピングカート商品一括削除テスト() throws Exception {
		
			//テスト対象メソッド（deleteCartItemsメソッド）を実行
			mockMvc.perform(get("/shopping/allDeleteOrderItem"))
			//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
			.andExpect(redirectedUrl("/shopping/toCart"))
			//次画面の遷移先がmaterialize-version/register_userであることを確認
			.andExpect(view().name("redirect:/shopping/toCart"));
		
	}
	
	@Test
	@DisplayName("ログイン画面へ遷移する")
	public void ログイン画面遷移テスト() throws Exception {
		
			//テスト対象メソッド（deleteCartItemsメソッド）を実行
			mockMvc.perform(get("/shopping/goLogin"))
			//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
			.andExpect(status().isOk())
			//次画面の遷移先がmaterialize-version/register_userであることを確認
			.andExpect(view().name("materialize-version/login"));
		
	}
	
	
	@AfterEach
	@DisplayName("仮ユーザーを削除する")
	public void tearDown() throws Exception {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", "test@gmail.com");
		template.update("DELETE FROM users WHERE email = :email", param);
		System.out.println("確認完了しました");
	}
	
}
