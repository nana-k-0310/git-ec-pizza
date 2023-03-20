package com.example.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 登録するユーザー情報フォーム.
 * 
 * @author nanakono
 *
 */
public class RegisterUserForm {

	/** 顧客苗字 */
	@NotBlank(message="苗字は必須です")
	private String lastName;
	/** 顧客氏名 */
	@NotBlank(message="名前は必須です")
	private String firstName;
	/** Eメール */
	@NotBlank(message="メールアドレスは必須です")
	@Size(min=1, max=127, message="Eメールは1文字以上127文字以内で記載してください")
	@Email(message="Eメールの形式が不正です")
	private String email;
	/** パスワード */
	@NotBlank(message="パスワードは必須です")
	@Size(min=8, max=16, message="パスワードは8文字以上16文字以内で記載してください")
	private String password;
	/** 確認用パスワード */
	@NotBlank(message="確認用パスワードは必須です")
	private String confirmationPassword;
	/** 郵便番号 */
	@NotBlank(message="郵便番号は必須です")
	@Pattern(regexp="^[0-9]{3}-[0-9]{4}$", message="郵便番号形式にしてください")
	private String zipcode;
	/** 住所 */
	@NotBlank(message="住所は必須です")
	private String address;
	/** 電話番号 */
	@NotBlank(message="電話番号は必須です")
	@Pattern(regexp="^[0-9]{2,4}-[0-9]{3,4}-[0-9]{3,4}$", message="電話番号はxxxx-xxxx-xxxx形式にしてください")
	private String telephone;
	
	public String getName() {
		return getLastName() + getFirstName();
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmationPassword() {
		return confirmationPassword;
	}
	public void setConfirmationPassword(String confirmationPassword) {
		this.confirmationPassword = confirmationPassword;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	@Override
	public String toString() {
		return "RegisterUserForm [lastName=" + lastName + ", firstName=" + firstName + ", email=" + email
				+ ", password=" + password + ", confirmationPassword=" + confirmationPassword + ", zipcode=" + zipcode
				+ ", address=" + address + ", telephone=" + telephone + "]";
	}
	
	
	
}
