package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.ShowItemListService;

@Controller
@RequestMapping("")
public class showItemListController {

	@Autowired
	private ShowItemListService service;

	/**
	 * 商品一覧画面に遷移.
	 * 
	 * @param model モデル
	 * @return 商品一覧画面
	 */
	@GetMapping("")
	public String showItemList(String order, Model model) {
		List<Item> itemList = service.findAll(order);
		model.addAttribute("itemList", itemList);
		return "materialize-version/item_list";
	}

	/**
	 * 名前の一覧検索.
	 * 
	 * @param name　商品名
	 * @param model　モデル
	 * @return　一覧画面へ遷移
	 */
	@GetMapping("/findByName")
	public String findByName(String name, String order, Model model) {
		if(name.equals("")) {
			model.addAttribute("result", "検索結果が0件の為、全件検索します");
			return showItemList(order, model);
		}
		List<Item> itemList = service.findByName(name);
		if(itemList.size() == 0) {
			model.addAttribute("result", "検索結果が0件の為、全件検索します" );
			return showItemList(order, model);
		} else {
		model.addAttribute("itemList", itemList);
		return "materialize-version/item_list";
		}
		
	}

}
