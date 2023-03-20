package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.repository.ItemRepository;
import com.example.repository.ToppingRepository;

/**
 * 商品詳細情報を操作するサービス.
 * 
 * @author nanakono
 *
 */
@Service
@Transactional
public class ShowItemDetailService {

	@Autowired
	public ItemRepository itemRepository;
	
	@Autowired
	public ToppingRepository toppingRepository;
	
	/**
	 * 指定された商品の詳細情報をトッピングリストと合わせて取得する.
	 * 
	 * @param itemId　指定された商品ID
	 * @return　指定された商品詳細情報とトッピングリスト
	 */
	public Item showItemDetail(Integer id) {
		Item item = itemRepository.load(id);
		System.out.println(item);
		item.setToppingList(toppingRepository.findAll());
		return item;
	}
	
	
}
