package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

/**
 * ユーザー情報を操作するリポジトリ.
 * 
 * @author nanakono
 *
 */
@Repository
public class UserRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * Userオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		return user;
	};

	/**
	 * ユーザー情報を登録する.
	 * 
	 * @param user ユーザー情報
	 */
	public void save(User user) {
		String sql = "INSERT INTO users (name,email,password,zipcode,address,telephone) VALUES (:name, :email, :password, :zipcode, :address, :telephone);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		template.update(sql, param);
	}

	/**
	 * メールアドレスからユーザー情報を取得する.
	 * 
	 * @param email　メールアドレス
	 * @return　ユーザー情報
	 */
	public User findByEmail(String email) {
	//	System.out.println(email);
		String sql = "SELECT id,name,email,password,zipcode,address,telephone FROM users WHERE email = :email;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}
	
	/**
	 * パスワードとメールアドレスからユーザー情報を取得する.
	 * 
	 * @param password　パスワード
	 * @param email　メールアドレス
	 * @return　ユーザー情報
	 */
	public User fincByPasswordAndEmail(String password, String email) {
		String sql = "SELECT id,name,email,password,zipcode,address,telephone FROM users WHERE password = :password and email = :email;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("password", password).addValue("email", email);
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if(userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}
	

}
