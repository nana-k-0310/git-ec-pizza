package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
import com.example.repository.UserRepository;

/**
 * 顧客情報を操作するサービス.
 * 
 * @author nanakono
 *
 */
@Service
@Transactional
public class LoginLogoutUserService {

	@Autowired
	private UserRepository repository;

	/**
	 * 顧客のログインを行います.
	 * 
	 * @param form フォーム
	 * @return 該当するユーザー情報を返します。従業員が存在しない場合はnullを返します.
	 */
	public UserInfo login(LoginLogoutUserForm form) {
		UserInfo user = repository.findByEmail(form.getEmail());
		
		if(user != null) {
		
		if (!(form.getPassword().equals(user.getPassword()))) {
			return null;
		}
		
		}
		
		return user;
		
	}

}
