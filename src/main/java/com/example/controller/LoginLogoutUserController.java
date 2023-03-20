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
import com.example.form.LoginLogoutUserForm;
import com.example.service.LoginLogoutUserService;

import ch.qos.logback.core.net.SyslogOutputStream;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザーのログインを操作するコントローラー.
 * 
 * @author nanakono
 *
 */
@Controller
@RequestMapping("/login")
public class LoginLogoutUserController {

	@Autowired
	private LoginLogoutUserService service;
	@Autowired
	private HttpSession session;

	
	/**
	 * ログイン画面に遷移する.
	 * 
	 * @param form　フォーム
	 * @return　ログイン画面
	 */
	@GetMapping("")
	public String login(LoginLogoutUserForm form) {
		System.out.println("こんにちは");
		return "materialize-version/login";
	}
	
	/**
	 * ログインする.
	 * 
	 * @param form
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping("/toLogin")
	public String toLogin(LoginLogoutUserForm form, Model model) {

		User user = service.login(form);
		
		if(user == null) {
			model.addAttribute("errorMessage", "メールまたはパスワードが間違っています");
			return login(form);
		}
		
		session.setAttribute("userName", user.getName());
			return "redirect:/";
		}
	}
	
	

