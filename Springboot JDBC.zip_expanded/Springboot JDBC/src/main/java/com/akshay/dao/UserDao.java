package com.akshay.dao;

import java.util.List;


import org.springframework.stereotype.Component;

import com.akshay.model.User;



@Component
public interface UserDao {

	public  List<User>getAllUsers();
	public  void insertUser(User user);
	public  void updateUser(User user);
	public  void deleteUser(int id);	

}
