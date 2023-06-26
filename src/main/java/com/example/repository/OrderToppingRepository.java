package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.OrderTopping;

/**
 * 注文トッピング関連リポジトリ.
 * 
 * @author nanakono
 *
 */
@Repository
public class OrderToppingRepository {
	
	private static final RowMapper<OrderTopping> ORDERTOPPING_ROW_MAPPER = new BeanPropertyRowMapper<>(OrderTopping.class); 
		
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * 注文トッピングを登録します.
	 * 
	 * @param orderTopping 注文したトッピング
	 * @return　インサートした注文トッピング情報
	 */
	synchronized public OrderTopping insert(OrderTopping orderTopping) {
		
		//インサート処理
		String sql = "INSERT INTO order_toppings(topping_id,order_item_id) VALUES(:toppingId, :orderItemId);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderTopping);
		
		//後でorderItemのidを使うためキーにして返す（それがこのkeyHolder）
		//テストでidを使う時にもkeyHolderを使い、自動採番のIDを使えるようにする
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = { "id" };
		
		template.update(sql, param, keyHolder, keyColumnNames);
		
		orderTopping.setId(keyHolder.getKey().intValue());
		
//		template.update(sql, param);
		return orderTopping;
	}
	
	/**
	 * order_item_idからトッピングリスト取得
	 * 
	 * @param orderItemId　注文商品ID
	 * @return　注文トッピングリスト
	 */
	
	public List<OrderTopping> findOrderItemId(Integer orderItemId){
		try {
		String findSql = "SELECT id, topping_id, order_item_id FROM order_toppings WHERE order_item_id = :order_item_id;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("order_item_id", orderItemId);
		
		List<OrderTopping> orderToppingList = template.query(findSql, param, ORDERTOPPING_ROW_MAPPER);
		
		return orderToppingList;
		
		} catch (EmptyResultDataAccessException e) {
			System.out.println("例外が発生しました。");
	        System.out.println(e);
	 
	        return null;
	        
		}
	}
		
		
	
	

}
