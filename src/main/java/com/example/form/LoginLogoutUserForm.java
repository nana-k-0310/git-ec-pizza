package com.example.form;

import jakarta.validation.constraints.NotBlank;

/**
 * ログインログアウトするフォームクラス.
 * 
 * @author nanakono
 *
 */
public class LoginLogoutUserForm {
	
	/** パスワード */
	@NotBlank(message="パスワードを入力して下さい")
	private String password;
	/** メールアドレス */
	@NotBlank(message="メールアドレスを入力して下さい")
	private String email;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "LoginLogoutUserForm [password=" + password + ", email=" + email + "]";
	}

}
