package com.example.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
import com.example.form.ShoppingCartForm;
import com.example.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;

/**
 * ショッピングカート関連コントローラ.
 * 
 * @author nanakono
 *
 */
@Controller
@RequestMapping("/shopping")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private HttpSession session;
	
	/**
	 * 商品をカートに追加する.
	 * 
	 * @param form　ショッピングカートフォーム
	 * @param model　モデル
	 * @param userinfo
	 * @return　toCartへリダイレクト
	 */
	@PostMapping("/insertCart")
	public String insertCart(ShoppingCartForm form, LoginLogoutUserForm userForm,  Model model, UserInfo userinfo) {
		
		
		UserInfo insertUser = (UserInfo)session.getAttribute("user");
		
		if(Objects.isNull(insertUser)) {
			return goLogin(userForm);
		}
		
		shoppingCartService.insertCart(form, insertUser.getId());
		
		return "redirect:/shopping/toCart";
	}
	
	
	
	
	
	/**
	 * カートリストを表示.
	 * 
	 * @param model モデル
	 * @param userInfo　ユーザーオブジェクト
	 * @return　カートリスト
	 */
	@GetMapping("/toCart")
	public String toCartList(Model model, UserInfo userInfo) {
		UserInfo displayUserInfo = (UserInfo) session.getAttribute("user");
		
		Order order = shoppingCartService.showCart(displayUserInfo.getId());
		
		model.addAttribute("order", order);
		
		
		
		return "materialize-version/cart_list";
	}
	
	/**
	 * カート商品削除.
	 * 
	 * @param orderItemId　注文商品ID
	 * @return　toCartへリダイレクト
	 */
	@GetMapping("/deleteCart")
	public String deleteCartItems(Integer orderItemId) {
		
		shoppingCartService.deleteByOrderId(orderItemId);
		
		return "redirect:/shopping/toCart";
		
	}

	/**
	 * カート商品を一括削除する.
	 * 
	 * @param orderId
	 * @return
	 */
	@GetMapping("/allDeleteOrderItem")
	public String allDeleteOrderItem(Integer orderId) {
		shoppingCartService.allDeleteOrderItem(orderId);
		return "redirect:/shopping/toCart";
	}
	
	/**
	 * ユーザー情報がない場合、ログイン画面に遷移する.
	 * 
	 * @param userForm ユーザーフォーム
	 * @return　ログイン画面
	 */
	@GetMapping("/goLogin")
	public String goLogin(LoginLogoutUserForm userForm) {
		return "materialize-version/login";
		
		
	}
}
