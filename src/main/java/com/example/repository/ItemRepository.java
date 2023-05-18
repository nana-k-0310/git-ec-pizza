package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;

/**
 * itemテーブルを操作するリポジトリ.
 * 
 * @author nanakono
 *
 */
@Repository
public class ItemRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * Itemオブジェクトを生成するローマッパー.
	 */

	private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setDescription(rs.getString("description"));
		item.setPriceM(rs.getInt("price_m"));
		item.setPriceL(rs.getInt("price_l"));
		item.setImagePath(rs.getString("image_path"));
		item.setDeleted(rs.getBoolean("deleted"));
		return item;
	};
	
	/**
	 * 主キー検索を行います.
	 * 
	 * @param id　ID
	 * @return 商品情報
	 */
	public Item load(Integer id) {
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items WHERE id = :id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
		return item;
	}
	
	/** OrderItemのitemIdからitem取得/
	 * 
	 */
	
	public Item itemLoad(Integer itemId) {
		
		System.out.println("itemIDは" + itemId);
		
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items WHERE id = id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", itemId);
		
		//これ↓
		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
		
		return item;
	}
	
	
	/**
	 * 全件検索を行います.
	 * 
	 * @return　商品全件情報
	 */
	public List<Item> findAll(String order){
		if("high".equals(order)) {
			String sql = "SELECT id,name,description,price_m,price_l,image_path, deleted FROM items ORDER BY price_m DESC, id;";
			List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
			return itemList;
		} else if("low".equals(order)) {
			String sql = "SELECT id,name,description,price_m,price_l,image_path, deleted FROM items ORDER BY price_m, id;";
			List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
			return itemList;
		} else {
		String sql = "SELECT id,name,description,price_m,price_l,image_path, deleted FROM items ORDER BY id;";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
		}
	}
	
	/**
	 * 名前の曖昧検索を行います.
	 * 
	 * @param name　名前に含まれるワード
	 * @return	　　　検索された商品情報
	 */
	public List<Item> findByName(String name, String order){
		if("high".equals(order)) {
			String sql = "SELECT id,name,description,price_m,price_l,image_path, deleted FROM items WHERE name ilike :name ORDER BY price_m DESC, id;";
			SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
			List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
			return itemList;
		} else if("low".equals(order)) {
			String sql = "SELECT id,name,description,price_m,price_l,image_path, deleted FROM items WHERE name ilike :name ORDER BY price_m, id;";
			SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
			List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
			return itemList;
		} else {
		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items WHERE name ilike :name ORDER BY id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
		}
	}
}
