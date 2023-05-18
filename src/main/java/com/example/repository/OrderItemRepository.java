package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.OrderItem;

/**
 * 注文商品関連リポジトリ.
 * 
 * @author nanakono
 *
 */
@Repository
public class OrderItemRepository {

	private static final RowMapper<OrderItem> ORDERITEM_ROW_MAPPER = new BeanPropertyRowMapper<>(OrderItem.class);
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	
	/**
	 * 注文商品を登録します.
	 * 
	 * @param orderItem 注文商品
	 * @return　インサートした注文商品
	 */
	synchronized public OrderItem insert(OrderItem orderItem) {
		
		System.out.println(""
				+ "オーダーアイテムは" + orderItem + "です");
		
		String insertSql = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES(:itemId, :orderId, :quantity, :size);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);
		//後でorderItemのidを使うためキーにして返す（それがこのkeyHolder）
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = { "id" };
		
		template.update(insertSql, param, keyHolder, keyColumnNames);
		
	
		orderItem.setId(keyHolder.getKey().intValue());
		
		return orderItem;
	}
	
	/**
	 * 注文商品を検索します.
	 * 
	 * @param id　注文商品ID
	 * @return　注文商品
	 */
	public OrderItem load(Integer id) {
		
		String loadSql = "SELECT id, item_id, order_id, quantity, size FROM order_items WHERE id = :id ;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		
		System.out.println("ここまでok１");
		
		List<OrderItem> orderItemList = template.query(loadSql, param, ORDERITEM_ROW_MAPPER);
		if(orderItemList.size() == 0) {
			return null;
		}
		
		System.out.println("ここまでok2");
		
		return orderItemList.get(0);
	}
	
	/**
	 * 同じオーダーIDから商品リストを渡す.
	 * 
	 * @param orderId 注文Id
	 * @return　注文商品リスト
	 */
	public List<OrderItem> sameOrderLoad(Integer orderId) {
		
		String loadSql = "SELECT id, item_id, order_id, quantity, size FROM order_items WHERE order_id = :order_id;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("order_id", orderId);
		
		List<OrderItem> orderItemList = template.query(loadSql, param, ORDERITEM_ROW_MAPPER);
		
		if(orderItemList.size() == 0) {
			return null;
		}
		
		return orderItemList;
		
	}
	
	/** 同じ商品の場合個数を更新する*/
	
	public void updateCount(Integer newQuantity, Integer id) {
		String updateSql = "UPDATE order_items SET quantity = :quantity WHERE id = :id";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("quantity", newQuantity).addValue("id", id);
		
		template.update(updateSql, param);
		
		System.out.println("ここまではokです！");
	}
	
	/**
	 * 注文商品を削除する.
	 * 
	 * @param id　注文商品id
	 */
	public void deleteByOrderId(Integer orderItemId) {
		String deleteSql = "DELETE FROm order_items WHERE id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", orderItemId);
		template.update(deleteSql, param);
	}
	
	/**
	 * 商品を一括削除する.
	 * 
	 * @param id 注文商品id
	 */
	public void allDeleteOrderItem(Integer orderItemOrderId){
		String allDeleteSql = "DELETE FROM order_items WHERE order_id=:orderId;";
		System.out.println("削除するIDは" + orderItemOrderId + "です");
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId", orderItemOrderId);
		template.update(allDeleteSql, param);
	}
	
	
	
}
