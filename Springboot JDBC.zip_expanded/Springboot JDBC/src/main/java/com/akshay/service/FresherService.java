package com.akshay.service;


import org.springframework.stereotype.Component;

import com.akshay.model.Fresher;





@Component
public interface FresherService {
	
	
	public Iterable<Fresher> getAllFreshers();	
	public  void insertFresher(Fresher fresher) ;
	public  void updateFresher(Fresher fresher);
	public  void deleteFresher(int id);
	
	

}
