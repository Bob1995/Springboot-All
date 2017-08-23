package com.akshay.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.akshay.model.User;



@Repository
public class UserDaoImpl implements UserDao{
	@Autowired
	private  JdbcTemplate jdbcTemplate;

	@Override
	public  List<User> getAllUsers() {
		System.out.println("hello");
		String query = "select * from users";
		List<User> users  = jdbcTemplate.query(query,
				new BeanPropertyRowMapper<User>(User.class));
		return users;

	}

	@Override
	public void insertUser(User user) {
		String query = "insert into users values ('"+user.firstName+"','"+user.lastName+"',"+user.age+","+user.id+")";
		jdbcTemplate.update(query);

	}

	@Override
	public void updateUser(User user) {	

		String query = "update users set firstname='"+user.firstName+"',lastname='"+user.lastName+"',age="+user.age+" where id="+user.id+"";
		jdbcTemplate.update(query);

	}

	@Override
	public void deleteUser(int id) {

		String query = "delete from users where id="+id+"";
		jdbcTemplate.update(query);


	}




}
