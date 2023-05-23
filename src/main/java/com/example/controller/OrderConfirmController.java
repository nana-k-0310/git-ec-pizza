package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.domain.Order;
import com.example.domain.UserInfo;
import com.example.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderConfirmController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private HttpSession session;
	
//	@PostMapping("/orderConfirm")

//		public String orderConfirm(Model model, UserInfo userInfo) {
			
//			UserInfo user = (UserInfo) session.getAttribute("user");
//			Order orderInfo = (Order) session.getAttribute("order");
			
//			orderConfirmService.getOrderId(orderInfo.getId());
		
//			model.addAttribute("order", orderInfo);
//			model.addAttribute(user);
			
//			return "materialize-version/order_confirm";		
	
//	}

	
	//ここから↓コメントにしてます。
	//エラー出たらコメント消す。
	
//	@GetMapping("/orderConfirm")
//	public String orderConfirm(Model model) {
//		UserInfo user = (UserInfo) session.getAttribute("user");
//		System.out.println("userは" + user + "です");
		
//		Order orderInfo = shoppingCartService.showCart(user.getId());
//		model.addAttribute("order", orderInfo);
		
//		System.out.println("order情報は" + orderInfo + "です");
		
//		return "materialize-version/order_confirm";
//	}

}
