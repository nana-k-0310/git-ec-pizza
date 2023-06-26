package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
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

	@DisplayName("注文情報登録")
	@Test
	void 注文情報登録テスト() throws Exception{
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order1 = new Order();
		order1.setUserId(user.getId());
		order1.setStatus(0);
		order1.setTotalPrice(0);
		
		Order orderInsert1 = orderRepository.insert(order1);
		
		//リポジトリで作成したIDがあるか確認
		assertNotNull(orderInsert1.getId());
		
		assertEquals(user.getId(), orderInsert1.getUserId(), "ユーザーIDの登録がありません");
		assertEquals(0, orderInsert1.getStatus(), "ステータスの登録がありません");
		assertEquals(0, orderInsert1.getTotalPrice(), "合計金額の登録がありません");
		assertEquals(null, orderInsert1.getOrderDate(), "注文日がnullでありません");
		assertEquals(null, orderInsert1.getDestinationName(), "注文者名がnullでありません");
		assertEquals(null, orderInsert1.getDestinationEmail(), "注文者メールアドレスがnullでありません");
		assertEquals(null, orderInsert1.getDestinationZipcode(), "注文者郵便番号がnullでありません");
		assertEquals(null, orderInsert1.getDestinationAddress(), "注文者住所がnullでありません");
		assertEquals(null, orderInsert1.getDestinationTel(), "注文者電話番号がnullでありません");
		assertEquals(null, orderInsert1.getDeliveryTime(), "配達時間がnullでありません");
		assertEquals(null, orderInsert1.getPaymentMethod(), "支払方法がnullでありません");
		
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("注文情報を更新する")
	@Test
	public void 注文情報更新テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user4 = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order4 = new Order();
		order4.setUserId(user4.getId());
		order4.setStatus(0);
		order4.setTotalPrice(0);
				
		orderRepository.insert(order4);
		
		//オーダー商品を作る
		OrderItem orderItem4 = new OrderItem();
		orderItem4.setItemId(1);
		orderItem4.setOrderId(order4.getId());
		orderItem4.setQuantity(1);
		orderItem4.setSize("M");
				
		//オーダー商品を登録する
		orderItemRepository.insert(orderItem4);
				
		//OrderのOrderItemListにセットするためにオーダー商品を入れたリスト作成
		ArrayList<OrderItem> orderItemList = new ArrayList<>();
		orderItemList.add(orderItem4);
		
		order4.setOrderItemList(orderItemList);
		
		//OrderItemのOrderToppingListにセットするためオーダートッピングを入れた（ここではなし）リスト作成
		ArrayList<OrderTopping> orderToppingList = new ArrayList<>();
		
		orderItem4.setOrderToppingList(orderToppingList);
		
		//showItemDetailControllerではitemをセッション済.
		//OrderRepositoryのfindByUserIdAndStatusメソッドで全てのテーブルを繋げているのでitem情報も取り出せる.
		//order4のままだとorderのみで他のテーブルを繋げていないため繋げたものを呼び出す
		
		Order findOrder4 = orderRepository.findByUserIdAndStatus(user4.getId(), 0);
		
		
		//更新するオーダー情報を作成する
		Order updateOrder = new Order();
		
		updateOrder.setId(findOrder4.getId());
		updateOrder.setUserId(findOrder4.getUserId());
		
		
		//注文日設定する
		java.sql.Date today = new java.sql.Date(new Date().getTime());
		updateOrder.setOrderDate(today);
		
		//配達日を設定する
		Calendar afterToday = Calendar.getInstance();
		afterToday.setTime(today);
		
		afterToday.add(Calendar.MONTH, 1);
		
		//CalendarクラスからDateクラスに変換
        Date afterDate = new Date();
        
        afterDate = afterToday.getTime();
		
		
		Timestamp deliveryDateTimestamp = new Timestamp(afterDate.getTime());
		updateOrder.setDeliveryTime(deliveryDateTimestamp);
		
		
		//その他情報を設定する
		updateOrder.setStatus(1);
		updateOrder.setTotalPrice(findOrder4.getCalcTotalPrice());
		updateOrder.setDestinationName("テスト名");
		updateOrder.setDestinationEmail("test@gmail.com");
		updateOrder.setDestinationZipcode("123-4567");
		updateOrder.setDestinationAddress("テスト住所");
		updateOrder.setDestinationTel("00-1234-5678");
		updateOrder.setPaymentMethod(1);
		
		
		//Order情報を更新する.
		
		Order getOrder = orderRepository.update(updateOrder);
		
		assertEquals(updateOrder.getId(), getOrder.getId(), "オーダーのIDが正常でありません");
		assertEquals(updateOrder.getUserId(), getOrder.getUserId(), "オーダーのユーザーIDが正常でありません");
		assertEquals(updateOrder.getStatus(), getOrder.getStatus(), "オーダーのステータスが正常でありません");
		assertEquals(updateOrder.getTotalPrice(), getOrder.getTotalPrice(), "オーダーの合計金額が正常でありません");
		assertEquals(updateOrder.getOrderDate(), getOrder.getOrderDate(), "オーダーの注文日が正常でありません");
		assertEquals(updateOrder.getDestinationName(), getOrder.getDestinationName(), "オーダーの名前が正常でありません");
		assertEquals(updateOrder.getDestinationEmail(), getOrder.getDestinationEmail(), "オーダーのメールアドレスが正常でありません");
		assertEquals(updateOrder.getDestinationZipcode(), getOrder.getDestinationZipcode(), "オーダーの郵便番号が正常でありません");
		assertEquals(updateOrder.getDestinationAddress(), getOrder.getDestinationAddress(), "オーダーの住所が正常でありません");
		assertEquals(updateOrder.getDestinationTel(), getOrder.getDestinationTel(), "オーダーの電話番号が正常でありません");
		assertEquals(updateOrder.getDeliveryTime(), getOrder.getDeliveryTime(), "オーダーの配達日時が正常でありません");
		assertEquals(updateOrder.getPaymentMethod(), getOrder.getPaymentMethod(), "オーダーの注文方法が正常でありません");
		
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user4.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("DBあり注文状態検索（オーダーのIDから）")
	@Test
	public void DBあり注文状態をオーダーのIDから検索取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
				//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
				Order order = new Order();
				order.setUserId(user.getId());
				order.setStatus(0);
				order.setTotalPrice(0);
						
				Order firstGetOrder = orderRepository.insert(order);
				
				//オーダー商品を作る
				OrderItem orderItem = new OrderItem();
				orderItem.setItemId(1);
				orderItem.setOrderId(firstGetOrder.getId());
				orderItem.setQuantity(1);
				orderItem.setSize("M");
						
				//オーダー商品を登録する
				orderItemRepository.insert(orderItem);
				
				
				Order getOrder = orderRepository.load(order.getId());
				
				assertEquals(firstGetOrder.getId(), getOrder.getId(), "オーダーのIDが正常でありません");
				assertEquals(user.getId(), getOrder.getUserId(), "オーダーのユーザーIDが正常でありません");
				assertEquals(0, getOrder.getStatus(), "オーダーのステータスが正常でありません");
				assertEquals(0, getOrder.getTotalPrice(), "オーダーの合計金額が正常でありません");
				assertEquals(null, getOrder.getOrderDate(), "オーダーの注文日が正常でありません");
				assertEquals(null, getOrder.getDestinationName(), "オーダーの名前が正常でありません");
				assertEquals(null, getOrder.getDestinationEmail(), "オーダーのメールアドレスが正常でありません");
				assertEquals(null, getOrder.getDestinationZipcode(), "オーダーの郵便番号が正常でありません");
				assertEquals(null, getOrder.getDestinationAddress(), "オーダーの住所が正常でありません");
				assertEquals(null, getOrder.getDestinationTel(), "オーダーの電話番号が正常でありません");
				assertEquals(null, getOrder.getDeliveryTime(), "オーダーの配達日時が正常でありません");
				assertEquals(0, getOrder.getPaymentMethod(), "オーダーの注文方法が正常でありません");
				
				for(OrderItem getOrderItem : getOrder.getOrderItemList()) {
					
					assertEquals(orderItem.getItemId(), getOrderItem.getItemId(), "オーダー商品の商品IDが正常でありません");
					assertEquals(firstGetOrder.getId(), getOrderItem.getOrderId(), "オーダー商品のオーダーIDが正常でありません");
					assertEquals(orderItem.getQuantity(), getOrderItem.getQuantity(), "オーダー商品の数量が正常でありません");
					assertEquals(orderItem.getSize(), getOrderItem.getSize(), "オーダー商品のサイズが正常でありません");
					
				}
				
				
				//オーダーテーブルから、このIDの情報を消す
				SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
				template.update("DELETE FROM orders WHERE user_id = :user_id", param);
				System.out.println("確認完了しました");
	
	}
	
	@DisplayName("DBなし注文状態検索（オーダーのIDから）")
	@Test
	public void DBなし注文状態をオーダーのIDから検索取得テスト() throws Exception {
		
		Order order = orderRepository.load(100000);
		assertEquals(null, order, "DBなしオーダーの取得が正常でありません");
		
	}
	
	@DisplayName("DBあり注文状態検索（ユーザーIDと状態から）")
	@Test
	public void DBあり注文状態をIDと状態から検索取得テスト() throws Exception {
		
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
		
		//Orderテーブルから検索する
		
		Order orderGet2 = orderRepository.findByUserIdAndStatus(user2.getId(), 0);
		
		System.out.println("order2は" + orderInsert2);
		
		//リポジトリで作成したIDがあるか確認
		assertNotNull(orderGet2.getId());
		
		assertEquals(user2.getId(), orderGet2.getUserId(), "ユーザーIDの登録がありません");
		assertEquals(0, orderGet2.getStatus(), "ステータスの登録がありません");
		assertEquals(0, orderGet2.getTotalPrice(), "合計金額の登録がありません");
		assertEquals(null, orderGet2.getOrderDate(), "注文日がnullでありません");
		assertEquals(null, orderGet2.getDestinationName(), "注文者名がnullでありません");
		assertEquals(null, orderGet2.getDestinationEmail(), "注文者メールアドレスがnullでありません");
		assertEquals(null, orderGet2.getDestinationZipcode(), "注文者郵便番号がnullでありません");
		assertEquals(null, orderGet2.getDestinationAddress(), "注文者住所がnullでありません");
		assertEquals(null, orderGet2.getDestinationTel(), "注文者電話番号がnullでありません");
		assertEquals(null, orderGet2.getDeliveryTime(), "配達時間がnullでありません");
		
		//支払方法（PaymentMethod）はIntegerなのでnullでなく0になる
		System.out.println("受け取るPaymentMethodは" + orderGet2.getPaymentMethod());
		assertEquals(0, orderGet2.getPaymentMethod(), "支払方法がnullでありません");
		
		
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user2.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("statusがDBなしuserIdがDBありの注文状態検索（ユーザーIDと状態から）")
	@Test
	public void statusDBなし注文状態をIDと状態から検索取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user3 = userRepository.findByEmail("test@gmail.com");
		
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order3 = new Order();
		order3.setUserId(user3.getId());
		order3.setStatus(1);
		order3.setTotalPrice(0);
				
		orderRepository.insert(order3);
		
		//Orderテーブルから検索する
		
		Order orderGet3 = orderRepository.findByUserIdAndStatus(user3.getId(), 0);
		
		//リポジトリで作成したIDがあるか確認
//		assertNotNull(orderGet2.getId());
		
		assertEquals(null, orderGet3, "受け取ったオーダーがnullでありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user3.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@DisplayName("DBなしの注文状態検索（ユーザーIDと状態から）")
	@Test
	public void DBなし注文状態検索取得テスト() throws Exception {
		
		//Orderテーブルから検索する
		
		Order orderGet4 = orderRepository.findByUserIdAndStatus(1000, 0);
		
		assertEquals(null, orderGet4, "受けったオーダーがnullでありません");
		
	}
	
	@DisplayName("注文済DBあり注文履歴情報検索（ユーザーIDから）")
	@Test
	public void 注文済DBあり注文履歴情報検索取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
			UserInfo user = userRepository.findByEmail("test@gmail.com");
				
			//Orderテーブルにインサートする
			//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
				Order order = new Order();
				order.setUserId(user.getId());
				order.setStatus(1);
				order.setTotalPrice(0);
								
				Order firstGetOrder = orderRepository.insert(order);
						
				//オーダー商品を作る
				OrderItem orderItem = new OrderItem();
				orderItem.setItemId(1);
				orderItem.setOrderId(firstGetOrder.getId());
				orderItem.setQuantity(1);
				orderItem.setSize("M");
								
				//オーダー商品を登録する
				orderItemRepository.insert(orderItem);
				
				List<Order> orderList = orderRepository.findHistoryByUserIdAndStatus(user.getId());
				
				for(Order getOrder : orderList) {
					
				assertEquals(firstGetOrder.getId(), getOrder.getId(), "オーダーのIDが正常でありません");
				assertEquals(user.getId(), getOrder.getUserId(), "オーダーのユーザーIDが正常でありません");
				assertEquals(1, getOrder.getStatus(), "オーダーのステータスが正常でありません");
				assertEquals(0, getOrder.getTotalPrice(), "オーダーの合計金額が正常でありません");
				assertEquals(null, getOrder.getOrderDate(), "オーダーの注文日が正常でありません");
				assertEquals(null, getOrder.getDestinationName(), "オーダーの名前が正常でありません");
				assertEquals(null, getOrder.getDestinationEmail(), "オーダーのメールアドレスが正常でありません");
				assertEquals(null, getOrder.getDestinationZipcode(), "オーダーの郵便番号が正常でありません");
				assertEquals(null, getOrder.getDestinationAddress(), "オーダーの住所が正常でありません");
				assertEquals(null, getOrder.getDestinationTel(), "オーダーの電話番号が正常でありません");
				assertEquals(null, getOrder.getDeliveryTime(), "オーダーの配達日時が正常でありません");
				assertEquals(0, getOrder.getPaymentMethod(), "オーダーの注文方法が正常でありません");
				
				for(OrderItem getOrderItem : getOrder.getOrderItemList()) {
					
					assertEquals(orderItem.getItemId(), getOrderItem.getItemId(), "オーダー商品の商品IDが正常でありません");
					assertEquals(firstGetOrder.getId(), getOrderItem.getOrderId(), "オーダー商品のオーダーIDが正常でありません");
					assertEquals(orderItem.getQuantity(), getOrderItem.getQuantity(), "オーダー商品の数量が正常でありません");
					assertEquals(orderItem.getSize(), getOrderItem.getSize(), "オーダー商品のサイズが正常でありません");
					
				}
				
				}
				
				//オーダーテーブルから、このIDの情報を消す
				SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
				template.update("DELETE FROM orders WHERE user_id = :user_id", param);
				System.out.println("確認完了しました");
	
				
	}
	
	@DisplayName("注文済DBなし注文履歴情報検索（ユーザーIDから）")
	@Test
	public void 注文済DBなし注文履歴情報検索取得テスト() throws Exception {
	
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
			
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
			Order order = new Order();
			order.setUserId(user.getId());
			order.setStatus(0);
			order.setTotalPrice(0);
							
			Order firstGetOrder = orderRepository.insert(order);
					
			//オーダー商品を作る
			OrderItem orderItem = new OrderItem();
			orderItem.setItemId(1);
			orderItem.setOrderId(firstGetOrder.getId());
			orderItem.setQuantity(1);
			orderItem.setSize("M");
							
			//オーダー商品を登録する
			orderItemRepository.insert(orderItem);
			
			List<Order> orderList = orderRepository.findHistoryByUserIdAndStatus(user.getId());
		
			assertEquals(null, orderList, "DBなし注文履歴情報の取得が正常でありません");
			
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
