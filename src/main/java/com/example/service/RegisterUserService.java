package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

/**
 * ユーザー情報を「」するサービス.
 * 
 * @author nanakono
 *
 */
@Service
@Transactional
public class RegisterUserService {

	@Autowired
	private UserRepository repository;
	
	/**
	 * ユーザー情報を登録する.
	 * 
	 * @param user　ユーザー情報
	 * 
	 */
	public void registerUser(User user) {
		repository.save(user);
	}
	
	
	/**
	 * メールアドレスからユーザー情報を取得する.
	 * 
	 * @param email メールアドレス
	 * @return　ユーザー情報
	 */
	public User findByEmail(String email) {
		User user = repository.findByEmail(email);
		return user;
	}
	
}
