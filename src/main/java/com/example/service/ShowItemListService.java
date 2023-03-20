package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.repository.ItemRepository;

/**
 * 商品情報を表示するサービスクラス.
 * 
 * @author nanakono
 *
 */
@Service
@Transactional
public class ShowItemListService {
	
	@Autowired
	private ItemRepository repository;
	
	/**
	 * 商品全件一覧情報を取得する.
	 * 
	 * @return　商品情報全件リスト
	 */
	public List<Item> findAll(){
		List<Item> itemList = repository.findAll();
		return itemList;
	}
	
	/**
	 * 検索されたワードを含む名前の商品情報を取得する.
	 * 
	 * @param name　検索されたワード
	 * @return1	　　　検索された商品情報
	 */
	public List<Item> findByName(String name){
		List<Item> itemList = repository.findByName(name);
		return itemList;
	}

}
