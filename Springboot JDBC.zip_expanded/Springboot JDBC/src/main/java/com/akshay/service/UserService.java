package com.akshay.service;

import java.util.List;

import com.akshay.model.User;

public interface UserService {

	public  List<User>getAllUsers();
	public  void insertUser(User user);
	public  void updateUser(User user);
	public  void deleteUser(int id);	
	
}
