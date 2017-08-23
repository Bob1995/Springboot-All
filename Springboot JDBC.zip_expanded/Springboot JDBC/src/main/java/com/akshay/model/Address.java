package com.akshay.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer Address_id;
	
	String City;
	String State;
	String Country;

	@ManyToOne
	//@JoinColumn(name = "fresher_id")
	Fresher fresher;
	
	public Integer getAddress_id() {
		return Address_id;
	}

	public void setAddress_id(Integer address_id) {
		Address_id = address_id;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public Fresher getFresher() {
		return fresher;
	}

	public void setFresher(Fresher fresher) {
		this.fresher = fresher;
	}

}
