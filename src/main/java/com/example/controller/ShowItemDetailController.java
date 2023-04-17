package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.ShowItemDetailService;

/**
 * 商品詳細情報を表示するコントローラー.
 * 
 * @author nanakono
 *
 */
@Controller
@RequestMapping("/showItem")
public class ShowItemDetailController {
	
	@Autowired
	private ShowItemDetailService itemService;
	
	/**
	 * 商品詳細ページに遷移する.
	 * 
	 * @param itemId　商品ID
	 * @param model　モデル
	 * @return　商品詳細ページ
	 */
	@GetMapping("/toItemDetail")
	public String showItemDetail(Integer id, Model model) {
		Item item = itemService.showItemDetail(id);
		model.addAttribute("item", item);
		
		return "materialize-version/item_detail";
	}
}
