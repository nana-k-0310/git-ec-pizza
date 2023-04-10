package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * 
 * 商品トッピング情報を操作するリポジトリ.
 * 
 */
import com.example.domain.Topping;

@Repository
public class ToppingRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	/*
	 * トッピングリストを生成するローマッパー.*
	 * 
	 */
	private static final RowMapper<Topping> TOPPING_ROW_MAPPER = (rs, i) -> {
		Topping topping = new Topping();
		topping.setId(rs.getInt("id"));
		topping.setName(rs.getString("name"));
		topping.setPriceM(rs.getInt("price_m"));
		topping.setPriceL(rs.getInt("price_l"));
		return topping;
};

/**
 *　トッピングの全件検索
 * 
 * @return　トッピング全件情報
 */
	public List<Topping> findAll(){
		String sql = "SELECT id,name,price_m,price_l FROM toppings ORDER BY id";
		List<Topping> toppingList = template.query(sql, TOPPING_ROW_MAPPER);
		return toppingList;
	}
	
/**
 * 主キー検索を行います.
 * 
 * @param id　検索したい主キーの値
 * @return　検索された商品情報（検索されなかった場合は非検査例外が発生します）
 */
	public Topping load(Integer id) {
		String sql = "SELECT id,name,price_m,price_l FROM toppings WHERE id = :id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Topping topping = template.queryForObject(sql, param, TOPPING_ROW_MAPPER);
		return topping;
	}
		
}
