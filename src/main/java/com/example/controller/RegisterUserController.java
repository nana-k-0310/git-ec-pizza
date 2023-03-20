package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.RegisterUserForm;
import com.example.service.RegisterUserService;

/**
 * ユーザー情報を操作するコントローラー.
 * 
 * @author nanakono
 *
 */
@Controller
@RequestMapping("/register")
public class RegisterUserController {

	@Autowired
	private RegisterUserService service;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @param form フォーム
	 * @return ユーザー登録画面
	 */
	@GetMapping("")
	public String register(RegisterUserForm form) {

		return "materialize-version/register_user";
	}

	/**
	 * ユーザー情報を登録する
	 * 
	 * @param form  フォーム
	 * @param model モデル
	 * @return ログイン画面
	 * 
	 */
	@PostMapping("/registerUser")
	public String registerUser(@Validated RegisterUserForm form, BindingResult result, Model model) {
		// ** パスワード確認 *//
		if (!form.getConfirmationPassword().equals(form.getPassword()) || form.getConfirmationPassword().equals("")) {
			if (form.getConfirmationPassword().equals("")) {
				result.rejectValue("confirmationPassword", " ", "確認用パスワードを入力して下さい");
			} else {
				result.rejectValue("password", " ", "パスワードと確認用パスワードが一致していません");
			}
		}
		
		//** メールアドレス重複確認 *//
		if(!(service.findByEmail(form.getEmail()) == null)){
			result.rejectValue("email", " ", "メールアドレスが重複しています");
		}

		
		
		// ** エラー時は登録画面に遷移 *//
		if (result.hasErrors()) {
			return register(form);
		}

		// ** 登録し、ログイン画面に遷移 *//
		User user = new User();
		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPassword(form.getPassword());
		user.setZipcode(form.getZipcode());
		user.setAddress(form.getAddress());
		user.setTelephone(form.getTelephone());
		model.addAttribute(user);
		service.registerUser(user);
		return "materialize-version/login";
	}

}
