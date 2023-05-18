package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.common.ToppingId;
import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
import com.example.form.ShoppingCartForm;
import com.example.repository.OrderRepository;
import com.example.service.ShoppingCartService;
import com.example.service.ShowItemDetailService;

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
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ShowItemDetailService showItemDetailService;
	
	
	/**
	 * 商品をカートに追加する.
	 * 
	 * @param form　ショッピングカートフォーム
	 * @param model　モデル
	 * @param userinfo ユーザー情報
	 * @return　toCartへリダイレクト
	 */
	@PostMapping("/insertCart")
	public String insertCart(ShoppingCartForm form, LoginLogoutUserForm userForm,  Model model, UserInfo userinfo) {
		
		/**ログイン時にセッションスコープでログイン情報を登録してあるので取り出す*/
		UserInfo insertUser = (UserInfo)session.getAttribute("user");
		
		/**もしログインしていなかったらログイン画面へ遷移する*/
		if(Objects.isNull(insertUser)) {
			return goLogin(userForm);
		}
		
		/**ユーザーのIDを取り出す*/
		Integer insertUserId = insertUser.getId();
		
		/**ユーザーIDから同じユーザーでカートに入っている商品がないか確認する。あったらそのオーダーを返し、ない場合はnullを返す*/
		Order nowOrderStatus = showItemDetailService.findByUserIdAndStatus(insertUserId);
		
		/**オーダー情報からIDを取り、そのオーダーを商品情報リストを返す*/
		/** isNullはnullの時に値を返し、そうでなければfalseを返す*/
		
		if(!(Objects.isNull(nowOrderStatus)) && !(nowOrderStatus.getCalcTotalPrice() == 0)){
		
			/**オーダーのIDから商品リストを受け取る */
			List<OrderItem> orderItemList = showItemDetailService.itemListByOrderId(nowOrderStatus.getId());

		
				for(OrderItem orderItem : orderItemList) {
			
					/**もしトッピングがあればトッピングIDリストを作成する */
					
					ToppingId toppingIdList = new ToppingId();
				
					List<Integer> toppingIdInsertList = toppingIdList.addId();
					
					
						for(OrderTopping orderTopping : orderItem.getOrderToppingList()) {
			
							toppingIdInsertList.add(orderTopping.getToppingId());
				
						}
						
						
				System.out.println("formのトッピングリストは" + form.getToppingIdList());
				System.out.println("前に登録してある同じ商品のトッピングリストは" + toppingIdInsertList);
				System.out.println("今回の注文商品のトッピングリストと前の登録商品のトッピングりすとは同じか？同じ→" + form.getToppingIdList().equals(toppingIdInsertList));
			
				if(form.getItemId().equals(orderItem.getItemId()) && form.getSize().equals(orderItem.getSize()) && form.getToppingIdList().equals(toppingIdInsertList)) {
				
				orderItem.setQuantity(orderItem.getQuantity() + form.getQuantity());
				showItemDetailService.updateCount(orderItem.getQuantity(), orderItem.getId());
				
				return "redirect:/shopping/toCart";
				}
		
		}
				
	} //*←以前0円でないオーダーがあるときのifの括弧*//
		
		/**　重なる商品がない場合はインサートする */
		
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
	public String toCartList(Model model, LoginLogoutUserForm userForm, UserInfo userInfo) {
		UserInfo displayUserInfo = (UserInfo) session.getAttribute("user");
		
		if(Objects.isNull(displayUserInfo)) {
			return goLogin(userForm);
		}
		
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
