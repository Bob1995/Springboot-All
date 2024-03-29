package com.akshay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akshay.dao.UserDao;
import com.akshay.model.User;


@Service
public class UserServiceImpl  implements UserService{

	@Autowired
	UserDao userdao;
	
	@Override
	public List<User> getAllUsers() {
	
		System.out.println("hello userservice...");
		List<User>users=userdao.getAllUsers();
		return users;
		
		
	}

	@Override
	public void insertUser(User user) {
		userdao.insertUser(user);
	}

	@Override
	public void updateUser(User user) {
		userdao.updateUser(user);
		
	}

	@Override
	public void deleteUser(int id) {
		userdao.deleteUser(id);
		
	}



	
}



	


