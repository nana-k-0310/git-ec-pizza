package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
import com.example.service.LoginLogoutUserService;

import ch.qos.logback.core.net.SyslogOutputStream;
import jakarta.servlet.RequestDispatcher;
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

		UserInfo user = service.login(form);
		
		System.out.println("userは" + user);
		
		if(user == null) {
			model.addAttribute("errorMessage", "メールまたはパスワードが間違っています");
			return login(form);
		}
		
		session.setAttribute("user", user);
		
			return "redirect:/";
		
	}
	
	@GetMapping("/toLogout")
	public String toLogout(LoginLogoutUserForm form) {
		
		// Sessionを破棄
//        session.invalidate();
		
		System.out.println("userは" + session.getAttribute("user") + "です");
		
		  session.removeAttribute("user");
		  
		System.out.println("解除後のuserは" + session.getAttribute("user") + "です");
        
//        RequestDispatcher rd = getServletContext().getRequestDispatcher("/user");
//        rd.forward(request, response);
	
		return login(form);
	}	
}
	
	

