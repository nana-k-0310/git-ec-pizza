package com.example.service;

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

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.UserInfo;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;
import com.example.repository.UserRepository;

@SpringBootTest
@DisplayName("商品詳細サービス")
class ShowItemDetailServiceTest {

	@Autowired
	private ShowItemDetailService showItemDetailService;
	
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
	@DisplayName("DBありの商品詳細情報を取得する")
	void DBあり商品詳細情報取得テスト() throws Exception {
		Item item1 = showItemDetailService.showItemDetail(1);
		assertEquals(1, item1.getId(), "最初の商品IDが登録されていません");
		assertEquals("じゃがバターベーコン", item1.getName(), "最初の商品名が登録されていません");
		assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", item1.getDescription(), "最初の商品説明が登録されていません");
		assertEquals(1490, item1.getPriceM(), "最初の商品のMサイズの金額が登録されていません");
		assertEquals(2570, item1.getPriceL(), "最初の商品のLサイズの金額が登録されていません");
		assertEquals("1.jpg", item1.getImagePath(), "最初の商品の画像が登録されていません");
		assertEquals(false, item1.getDeleted(), "最初の商品の削除対象が登録されていません");
		assertEquals(28, item1.getToppingList().size(), "最初の商品にトッピングリストが登録されていません");
		
	}
	
	@Test
	@DisplayName("DBなしの商品詳細情報を取得する")
	void DBなし商品詳細情報取得テスト() throws Exception {
		assertEquals(null, showItemDetailService.showItemDetail(30), "DBなしの商品詳細情報null確認失敗");
	}

	@Test
	@DisplayName("①DBありカートの未注文のカート情報を取得する")
	public void DBあり未注文カート情報取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
						
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order = new Order();
		order.setUserId(user.getId());
		order.setStatus(0);
		order.setTotalPrice(0);
						
		orderRepository.insert(order);
		
		Order getOrder = showItemDetailService.findByUserIdAndStatus(user.getId());
		
		//リポジトリで作成したIDがあるか確認
		assertNotNull(getOrder.getId());
		
		assertEquals(user.getId(), getOrder.getUserId(), "ユーザーIDが登録されていません");
		assertEquals(0, getOrder.getStatus(), "ステータスが登録されていません");
		assertEquals(0, getOrder.getTotalPrice(), "合計金額が正常でありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
		
	}
	
	@Test
	@DisplayName("②ユーザーはDBありでカートはDBなしで未注文のカート情報を取得する")
	public void DBありユーザーとDBなしカートで未注文カート情報取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
						
		Order getOrder = showItemDetailService.findByUserIdAndStatus(user.getId());
		
		assertEquals(null, getOrder, "ユーザーのオーダー情報が正常でありません");
		
	}
	
	@Test
	@DisplayName("③DBなしユーザーで未注文のカート情報を取得する")
	public void DBなしユーザーで未注文カート情報取得テスト() throws Exception {
		
		//DBにないユーザーを作成
		UserInfo user2 = new UserInfo();
		user2.setName("DBなしテスト");
		user2.setEmail("notdatetest@gmail.com");
		user2.setPassword("notdatetesttest");
		user2.setZipcode("123-4567");
		user2.setAddress("テスト住所");
		user2.setTelephone("00-1234-5678");
		
		Order getOrder2 = showItemDetailService.findByUserIdAndStatus(user2.getId());
		
		assertEquals(null, getOrder2, "ユーザーのオーダー情報が正常でありません");
		
	}
	
	@DisplayName("④DBありのオーダーIDから注文商品リストを取得する")
	@Test
	public void DBありオーダーIDから注文商品リスト取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
				
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
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
			orderItemRepository.insert(orderItem1);
		
			//オーダー商品（2つ目）を作る
			OrderItem orderItem2 = new OrderItem();
			orderItem2.setItemId(2);
			orderItem2.setOrderId(order1.getId());
			orderItem2.setQuantity(2);
			orderItem2.setSize("L");
			
			//オーダー商品（2つ目）を登録する
			orderItemRepository.insert(orderItem2);
								
				//オーダー商品（2つ目）の注文トッピング(1つ目)を作る
				OrderTopping orderTopping1 = new OrderTopping();
				orderTopping1.setToppingId(1);
				orderTopping1.setOrderItemId(orderItem2.getId());
		
				//オーダートッピング（1つ目）を登録する
				orderToppingRepository.insert(orderTopping1);
		
				//オーダー商品（2つ目）の注文トッピング(2つ目)を作る
				OrderTopping orderTopping2 = new OrderTopping();
				orderTopping2.setToppingId(2);
				orderTopping2.setOrderItemId(orderItem2.getId());
				
				//オーダートッピング（2つ目）を登録する
				orderToppingRepository.insert(orderTopping2);
			
		//オーダーIDから商品リストを渡す
		List<OrderItem> orderItemList = showItemDetailService.itemListByOrderId(order1.getId());
		
		OrderItem getOrderItem1 = orderItemList.get(0);
		OrderItem getOrderItem2 = orderItemList.get(1);
		
		//1つ目の注文商品の確認
		//アイテムがセットされているか確認
		assertEquals(1, getOrderItem1.getItem().getId(), "セットされたアイテムIDが正常でありません");
		assertEquals("じゃがバターベーコン", getOrderItem1.getItem().getName(), "セットされたアイテムの名前が正常でありません");
		assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", getOrderItem1.getItem().getDescription(), "セットされたアイテムの説明が正常でありません");
		assertEquals(1490, getOrderItem1.getItem().getPriceM(), "セットされたアイテムのサイズMの値段が正常でありません");
		assertEquals(2570, getOrderItem1.getItem().getPriceL(), "セットされたアイテムのサイズLの値段が正常でありません");
		assertEquals("1.jpg", getOrderItem1.getItem().getImagePath(), "セットされたアイテム画像が正常でありません");
		assertEquals(false, getOrderItem1.getItem().getDeleted(), "セットされたアイテムの削除情報が正常でありません");
		
		//オーダー情報確認
		assertNotNull(getOrderItem1.getId());
		assertEquals(1, getOrderItem1.getItemId(), "オーダーアイテムの商品IDが正常でありません");
		assertEquals(order1.getId(), getOrderItem1.getOrderId(), "オーダーアイテムのオーダーIDが正常でありません");
		assertEquals(1, getOrderItem1.getQuantity(), "オーダーアイテムの注文数が正常でありません");
		assertEquals("M", getOrderItem1.getSize(), "オーダーアイテムの注文サイズが正常でありません");
		assertEquals(0, getOrderItem1.getOrderToppingList().size(), "オーダーアイテムのトッピングリスト情報が正常でありません");
		
		//2つ目の注文商品の確認
		//アイテムがセットされているか確認
		assertEquals(2, getOrderItem2.getItem().getId(), "セットされたアイテムIDが正常でありません");
		assertEquals("アスパラ・ミート", getOrderItem2.getItem().getName(), "セットされたアイテムの名前が正常でありません");
		assertEquals("グリーンアスパラと相性の良いベーコンにいろどりのフレッシュトマトをトッピングし特製マヨソースでまとめた商品です", getOrderItem2.getItem().getDescription(), "セットされたアイテムの説明が正常でありません");
		assertEquals(1490, getOrderItem2.getItem().getPriceM(), "セットされたアイテムのサイズMの値段が正常でありません");
		assertEquals(2570, getOrderItem2.getItem().getPriceL(), "セットされたアイテムのサイズLの値段が正常でありません");
		assertEquals("2.jpg", getOrderItem2.getItem().getImagePath(), "セットされたアイテム画像が正常でありません");
		assertEquals(false, getOrderItem2.getItem().getDeleted(), "セットされたアイテムの削除情報が正常でありません");
		
		//セットされているトッピングリストを確認
		OrderTopping getOrderTopping1 = getOrderItem2.getOrderToppingList().get(0);
		OrderTopping getOrderTopping2 = getOrderItem2.getOrderToppingList().get(1);
		
		assertNotNull(getOrderTopping1.getId());
		assertEquals(1, getOrderTopping1.getToppingId(), "セットされた1つ目のトッピングIDが正常でありません");
		assertEquals(orderItem2.getId(), getOrderTopping1.getOrderItemId(), "セットされた1つ目のトッピング注文商品IDが正常でありません");
		
		assertNotNull(getOrderTopping2.getId());
		assertEquals(2, getOrderTopping2.getToppingId(), "セットされた2つ目のトッピングIDが正常でありません");
		assertEquals(orderItem2.getId(), getOrderTopping2.getOrderItemId(), "セットされた2つ目のトッピング注文商品IDが正常でありません");
		
		//オーダー情報確認
		assertNotNull(getOrderItem2.getId());
		assertEquals(2, getOrderItem2.getItemId(), "オーダーアイテムの商品IDが正常でありません");
		assertEquals(order1.getId(), getOrderItem2.getOrderId(), "オーダーアイテムのオーダーIDが正常でありません");
		assertEquals(1, getOrderItem1.getQuantity(), "オーダーアイテムの注文数が正常でありません");
		assertEquals("M", getOrderItem1.getSize(), "オーダーアイテムの注文サイズが正常でありません");
		assertEquals(0, getOrderItem1.getOrderToppingList().size(), "オーダーアイテムのトッピングリスト情報が正常でありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}
	
	@DisplayName("⑤DBありのオーダーIDとDBなしのオーダーアイテムで注文商品リストを取得する")
	@Test
	public void DBありオーダーIDとDBなしオーダーアイテムから注文商品リスト取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
				
		//Orderテーブルにインサートする
		//オーダーテーブルに同じユーザーのオーダー（カート）がない場合
		Order order1 = new Order();
		order1.setUserId(user.getId());
		order1.setStatus(0);
		order1.setTotalPrice(0);
				
		orderRepository.insert(order1);
		
		List<OrderItem> getOrderItemList = showItemDetailService.itemListByOrderId(order1.getId());
		
		assertEquals(null, getOrderItemList, "オーダーアイテムリストの中身が正常でありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}
	
	@DisplayName("⑥DBなしのオーダーIDで注文商品リストを取得する")
	@Test
	public void DBなしオーダーIDから注文商品リスト取得テスト() throws Exception {
		
		//ユーザーリポジトリから登録済みの仮ユーザー情報を取得する.（ログインする）
		UserInfo user = userRepository.findByEmail("test@gmail.com");
				
		assertEquals(null, showItemDetailService.itemListByOrderId(1000), "オーダーアイテムリストの中身が正常でありません");
		
		//オーダーテーブルから、このIDの情報を消す
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user.getId());
		template.update("DELETE FROM orders WHERE user_id = :user_id", param);
		System.out.println("確認完了しました");
	}
	
	
	//updateCount(Integer newQuantity, Integer id) 
	@DisplayName("⑦DBありのオーダー商品IDから注文商品数を更新する")
	@Test
	public void DBありオーダー商品IDから注文商品数更新テスト() throws Exception {
		
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
			OrderItem getOrderItem = orderItemRepository.insert(orderItem1);
		
			//オーダー商品の注文数を更新する
			showItemDetailService.updateCount(5, getOrderItem.getId());
			
			//更新したオーダー商品を取得する
			List<OrderItem> getOrderItemList = orderItemRepository.sameOrderLoad(order1.getId());
			
			assertEquals(5, getOrderItemList.get(0).getQuantity(), "オーダーアイテムの商品数が正常でありません");
		
			assertEquals(getOrderItem.getId(), getOrderItemList.get(0).getId(), "オーダーアイテムのIDが正常でありません");
			assertEquals(1, getOrderItemList.get(0).getItemId(), "オーダーアイテムの商品IDが正常でありません");
			assertEquals(order1.getId(), getOrderItemList.get(0).getOrderId(), "オーダーアイテムのオーダーIDが正常でありません");
			assertEquals("M", getOrderItemList.get(0).getSize(), "オーダーアイテムのサイズが正常でありません");
			
		
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
