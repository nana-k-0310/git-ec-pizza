package com.example.repository;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Order;

/**
 * 注文履歴商品リポジトリ.
 * 
 * @author nanakono
 *
 */
@Repository
public class OrderHistoryRepository {
	
	/**
	 * 注文テーブルから注文履歴リストを作成する.
	 * 
	 */

	private static final ResultSetExtractor<List<Order>> ORDERHISTORY_RESULT_SET_EXTRACTOR = (rs) -> {
		
		//Orderの空のリストを作成。「LinkedList()」は空リンク作成
				List<Order> orderHistoryList = new LinkedList<Order>();
				
				//前の行の注文ID、注文商品IDを愛日しておく変数（longはintでもいいが容量対策のためlongにしてある）
				//これはbefore～を定義しているようなもの
				long beforeOrderId = 0;
				
				while(rs.next()) {
					
				
				//現在検索されているID
				int nowOrderId = rs.getInt("id");
				
				//注文
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
		
				//注文リストにこの注文を加える
				orderHistoryList.add(order);
				
				}
				
				return orderHistoryList;
	};
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * 注文履歴情報を検索します.（ユーザーIDから検索注文済みのもののみ検索)）
	 * 
	 * @param userId　ユーザーID
	 * @return
	 */
	
	public List<Order> findHistoryByUserIdAndStatus(Integer userId) {
		
		String sql = "SELECT o.id, o.user_id, o.status, o.total_price, o.order_date, o.destination_name, o.destination_email, o.destination_zipcode, o.destination_address, o.destination_tel, o.delivery_time, o.payment_method "
				+ "FROM orders AS o "
				+ "WHERE o.user_id = :userId and o.status != 0 ORDER BY o.id;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Order> orderList = template.query(sql, param, ORDERHISTORY_RESULT_SET_EXTRACTOR);
		if(orderList.size() == 0) {
			return null;
		}
		return orderList;
	}		
	
}
