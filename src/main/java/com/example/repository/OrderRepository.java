package com.example.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;

/**
 * 注文商品関連レポジトリ.
 * 
 * @author nanakonokono
 *
 */
@Repository
public class OrderRepository {
	
	/**
	 * 
	 * 注文、注文商品、注文トッピングテーブルを結合したものから注文リストを作成する.
	 * 注文オブジェクト内には注文商品リストを、注文商品には注文トッピングリストを格納する.
	 */
	
	//ローマッパーも含め下のResultSetExtractorはSELECT文でしか使わない
	private static final ResultSetExtractor<List<Order>> ORDER_RESULT_SET_EXTRACTOR = (rs) -> {
		//注文が入るorderList 注文商品が入るorderItemList 注文トッピングが入るorderToppingListを作成
		
		//Orderの空のリストを作成。「LinkedList()」は空リンク作成
		List<Order> orderList = new LinkedList<Order>();
		List<OrderItem> orderItemList = null;
		List<OrderTopping> orderToppingList = null;
		
		//前の行の注文ID、注文商品IDを愛日しておく変数（longはintでもいいが容量対策のためlongにしてある）
		//これはbefore～を定義しているようなもの
		long beforeOrderId = 0;
		long beforeOrderItemId = 0;
		
		while(rs.next()) {
		
			//現在検索されているID
			int nowOrderId = rs.getInt("id");
			
			//注文
			//先程３つの表（orderList、orderItemList、orderToppingList）の中に、
			//それぞれ入力した中身を持つ「order、orderItem、orderTopping」を入れる
			if(nowOrderId != beforeOrderId) {
				Order order = new Order();
				order.setId(nowOrderId);
				order.setUserId(rs.getInt("user_id"));
				order.setTotalPrice(rs.getInt("total_price"));
				order.setOrderDate(rs.getDate("order_date"));			
				order.setDestinationName(rs.getString("destination_name"));
				order.setDestinationEmail(rs.getString("destination_email"));
				order.setDestinationZipcode(rs.getString("destination_zipcode"));
				order.setDestinationAddress(rs.getString("destination_address"));
				order.setDestinationTel(rs.getString("destination_tel"));
				order.setDeliveryTime(rs.getTimestamp("delivery_time"));
				order.setPaymentMethod(rs.getInt("payment_method"));
				
				//商品注文リストの新しいリスト作成
				orderItemList = new ArrayList<OrderItem>();
				//商品注文リストに今回作成した今回分の商品注文リストを加える
				order.setOrderItemList(orderItemList);
				
				//注文リストにこの注文を加える
				orderList.add(order);
			}
	
			//「oi」とは「order item」のこと」
			int nowOrderItemId = rs.getInt("oi_id");
			//注文商品
			if(beforeOrderItemId != nowOrderItemId) {
				OrderItem orderItem = new OrderItem();
				orderItem.setId(rs.getInt("oi_id"));
				orderItem.setItemId(rs.getInt("oi_item_id"));
				orderItem.setOrderId(rs.getInt("oi_order_id"));
				orderItem.setQuantity(rs.getInt("oi_quantity"));
				orderItem.setSize(rs.getString("oi_size"));
				
				//商品オブジェクトに格納
				Item item = new Item();
				item.setId(rs.getInt("i_id"));
				item.setName(rs.getString("i_name"));
				item.setDescription(rs.getString("i_description"));
				item.setPriceM(rs.getInt("i_price_m"));
				item.setPriceL(rs.getInt("i_price_l"));
				item.setImagePath(rs.getString("i_image_path"));
				
				orderItem.setItem(item);
				orderToppingList = new ArrayList<OrderTopping>();
				orderItem.setOrderToppingList(orderToppingList);
				orderItemList.add(orderItem);
			}
			
				//注文トッピング
				//「ot」は「オーダートッピング」、「t」は「トッピング」
				if(rs.getInt("ot_id") != 0) {
					OrderTopping orderTopping = new OrderTopping();
					orderTopping.setId(rs.getInt("ot_id"));
					orderTopping.setToppingId(rs.getInt("ot_topping_id"));
					orderTopping.setOrderItemId(rs.getInt("ot_order_item_id"));
					
					//トッピングオブジェクトに格納
					Topping topping = new Topping();
					topping.setId(rs.getInt("t_id"));
					topping.setName(rs.getString("t_name"));
					topping.setPriceM(rs.getInt("t_price_m"));
					topping.setPriceL(rs.getInt("t_price_l"));
					
					orderTopping.setTopping(topping);
					orderToppingList.add(orderTopping);
				}
			
				beforeOrderId = nowOrderId;
				beforeOrderItemId = nowOrderItemId;
		}
		return orderList;
		
	};
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * 注文情報を登録します.
	 * 
	 * @param order　注文商品
	 * @return　インサートした注文商品
	 */
	public Order insert(Order order) {
		
		String insertSql = "INSERT INTO orders(user_id, status, total_price, order_date, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method)"
				+ " VALUES(:userId, :status, :totalPrice, :orderDate, :destinationName, :destinationEmail, :destinationZipcode, :destinationAddress, :destinationTel, :deliveryTime, :paymentMethod);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		
		//OrderのIdを使うのでキーにして返す（keyHolder使用）
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = { "id" };
		
		template.update(insertSql, param, keyHolder, keyColumnNames);
		
		order.setId(keyHolder.getKey().intValue());
		System.out.println(keyHolder.getKey() + "が割り当てられました");
		
	return order;
	}
	
	/**
	 * 注文情報を更新します.
	 * 
	 * @param order 注文情報
	 */
	
	public void update(Order order ) {
		String updateSql = "UPDATE orders SET status =:status, total_price=:totalPrice, order_date=:orderDate, destination_name=:destinationName, destination_email=:destinationEmail, destination_zipcode=:destinationZipcode, destination_address=:destinationAddress, destination_tel=:destinationTel, delivery_time=:deliveryTime, payment_method=:paymentMethod WHERE user_id=:userId and id = :id;";
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		template.update(updateSql, param);
	}
	
	
	/**
	 * 注文情報を検索します.（IDから検索)
	 * 
	 * @param id ID
	 * @return　注文検索結果
	 */
	
	public Order load(Integer id) {
		String loadSql = "SELECT o.id, o.user_id, o.status, o.total_price, o.order_date, o.destination_name, o.destination_email, o.destination_zipcode, o.destination_address, o.destination_tel, o.delivery_time, o.payment_method, "
				+ "oi.id AS oi_id, oi.item_id AS oi_item_id, oi.order_id AS oi_order_id, oi.quantity AS oi_quantity, oi.size AS oi_size, "
				+ "ot.id AS ot_id, ot.topping_id AS ot_topping_id, ot.order_item_id AS ot_order_item_id, "
				+ "i.id AS i_id, i.name AS i_name, i.description AS i_description, i.price_m AS i_price_m, i.price_l AS i_price_l, i.image_path AS i_image_path, "
				+ "t.id AS t_id, t.name AS t_name, t.price_m AS t_price_m, t.price_l AS t_price_l "
				+ "FROM orders AS o LEFT JOIN order_items AS oi ON o.id = oi.order_id "
				+ "LEFT JOIN order_toppings AS ot ON oi.id = ot.order_item_id "
				+ "LEFT JOIN items AS i ON i.id = oi.item_id "
				+ "LEFT JOIN toppings AS t ON t.id = ot.topping_id WHERE o.id = :id order by o.id,oi.id,ot.id,i.id,t.id;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Order> orderList = template.query(loadSql, param, ORDER_RESULT_SET_EXTRACTOR);
		if(orderList.size() == 0) {
			return null;
		}
		return orderList.get(0);
	}
	
	
	/**
	 * 注文状態を検索します.（ユーザーIDと状態から検索)）
	 * 
	 * @param userId　ユーザーID
	 * @param status　状態
	 * @return
	 */
	
	/** Order、OrderItem、OrderTopping、Item、 Topping全ての表を繋げる */
	public Order findByUserIdAndStatus(Integer userId, Integer status) {
		
		String sql = "SELECT o.id, o.user_id, o.status, o.total_price, o.order_date, o.destination_name, o.destination_email, o.destination_zipcode, o.destination_address, o.destination_tel, o.delivery_time, o.payment_method ,"
				+ "oi.id AS oi_id, oi.item_id AS oi_item_id, oi.order_id AS oi_order_id, oi.quantity AS oi_quantity, oi.size AS oi_size, "
				+ "ot.id AS ot_id, ot.topping_id AS ot_topping_id, ot.order_item_id AS ot_order_item_id ,"
				+ "i.id AS i_id, i.name AS i_name, i.description AS i_description, i.price_m AS i_price_m, i.price_l AS i_price_l, i.image_path AS i_image_path, i.deleted AS i_deleted ,"
				+ "t.id AS t_id, t.name AS t_name, t.price_m AS t_price_m, t.price_l AS t_price_l "
				+ "FROM orders AS o LEFT JOIN order_items AS oi ON o.id = oi.order_id "
				+ "LEFT JOIN order_toppings AS ot ON oi.id = ot.order_item_id "
				+ "LEFT JOIN items AS i ON i.id = oi.item_id "
				+ "LEFT JOIN toppings AS t ON t.id = ot.topping_id WHERE o.user_id = :userId and o.status = :status ORDER BY oi.id desc;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);
		List<Order> orderList = template.query(sql, param, ORDER_RESULT_SET_EXTRACTOR);
		if(orderList.size() == 0) {
			return null;
		}
		return orderList.get(0);
	}
	
	

	
	
}
