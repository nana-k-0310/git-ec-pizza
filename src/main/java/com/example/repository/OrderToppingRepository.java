package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
		template.update(sql, param);
		return orderTopping;
	}
		
		
	
	

}
