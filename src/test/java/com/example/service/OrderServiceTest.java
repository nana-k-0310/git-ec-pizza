package com.example.service;

import static org.junit.jupiter.api.Assertions.*;

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
import com.example.repository.ItemRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;

@SpringBootTest
@DisplayName("オーダーサービズ")
class OrderServiceTest {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private RegisterUserService registerUserService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderConfirmService orderConfirmService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
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
	@DisplayName("注文情報登録する")
	void 注文情報登録テスト() throws Exception {
			//事前準備
			//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
				UserInfo user = userRepository.findByEmail("kono@sample.co.jp");
		
			//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
				Order order = new Order();
				order.setUserId(user.getId());
				order.setStatus(0);
				order.setTotalPrice(0);
				
				Order insertOrder = orderRepository.insert(order);
			
				//オーダー商品を作る
				OrderItem orderItem = new OrderItem();
				orderItem.setItemId(1);
				orderItem.setOrderId(order.getId());
				orderItem.setQuantity(1);
				orderItem.setSize("M");
				
				//オーダー商品を登録する
				OrderItem orderItemInsert = orderItemRepository.insert(orderItem);
					
				
			//注文情報フォームを作成する
				OrderForm orderForm = new OrderForm();
		
				orderForm.setDestinationName(user.getName());
				orderForm.setDestinationEmail(user.getEmail());
				orderForm.setDestinationZipcode(user.getZipcode());
				orderForm.setDestinationAddress(user.getAddress());
				orderForm.setDestinationTel(user.getTelephone());
		
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
				orderForm.setDeliveryTime("18");
		
			//formに注文状況と支払方法を入れる
				orderForm.setStatus(1);
				orderForm.setPaymentMethod(1);
		
			//注文確認のhtmlで<input type="hidden" name="id" th:value="${order.id}">とある
			//formでのnameはキーのような役割もする.formのidにはorderのidが入る
				//int型をString型へ
				//String sample = String.valueOf(int);
				String strId = String.valueOf(insertOrder.getId());
				orderForm.setId(strId);	
		
//		ユーザーIDからオーダーを取得する.
//		Order getOｒder = orderConfirmService.getOrderByUserId(user.getId());
		
		//テスト
		Order updateOrder = orderService.order(orderForm);
		
		assertEquals(insertOrder.getId(), updateOrder.getId(), "IDが違います");
		assertEquals(order.getUserId(), updateOrder.getUserId(), "ユーザーIDが違います");
		assertEquals(orderForm.getStatus(), updateOrder.getStatus(), "ステータスが違います");
		
		
		Item getItem = itemRepository.itemLoad(orderItemInsert.getItemId());
		System.out.println("ここのgetItemは" + getItem);
		int getTotalPrice = 0; 
		getTotalPrice += getItem.getPriceM() * 1.1;
		
		assertEquals(getTotalPrice, updateOrder.getTotalPrice(), "金額が違います");
		
		//注文日は書いてある内容が合っているか調べるため、Date型の日にちまでを取り出し比べる
		SimpleDateFormat getSdf = new SimpleDateFormat("yyyy'-'MM'-'dd");
		assertEquals(getSdf.format(today), getSdf.format(updateOrder.getOrderDate()), "注文日が違います");
		
		assertEquals(orderForm.getDestinationName(), updateOrder.getDestinationName(), "注文者名が違います");
		assertEquals(orderForm.getDestinationEmail(), updateOrder.getDestinationEmail(), "注文者メールアドレスが違います");
		assertEquals(orderForm.getDestinationZipcode(), updateOrder.getDestinationZipcode(), "注文者メールアドレスが違います");
		assertEquals(orderForm.getDestinationAddress(), updateOrder.getDestinationAddress(), "注文者住所が違います");
		assertEquals(orderForm.getDestinationTel(), updateOrder.getDestinationTel(), "注文者電話番号が違います");
		
		final String yyyyMMddhh = orderForm.getDeliveryDate() + "-" + orderForm.getDeliveryTime();
		
		System.out.println("テストのyyyyMMddhhは" + yyyyMMddhh);
		
		Date deliveryTime = new SimpleDateFormat("yyyy-MM-dd-hh").parse(yyyyMMddhh);
		Timestamp getDeliveryDateTimestamp = new Timestamp(deliveryTime.getTime());
		assertEquals(getDeliveryDateTimestamp, updateOrder.getDeliveryTime(), "配達日時が違います");
		
		assertEquals(orderForm.getPaymentMethod(), updateOrder.getPaymentMethod(), "支払方法が違います");
		
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
