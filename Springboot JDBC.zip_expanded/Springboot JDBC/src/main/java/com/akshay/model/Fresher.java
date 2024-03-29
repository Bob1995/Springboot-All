package com.akshay.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.stereotype.Component;

@Component
@Entity
public class Fresher {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int fresher_id;

	String fresher_name;
	String fresher_email;
	String fresher_remark;

	@OneToMany(mappedBy = "fresher", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	Set<Address> address;

	public Fresher() {
		super();
	}

	public Fresher(int fresher_id, String fresher_name, String fresher_email, String fresher_remark,
			Set<Address> address) {
		super();
		this.fresher_id = fresher_id;
		this.fresher_name = fresher_name;
		this.fresher_email = fresher_email;
		this.fresher_remark = fresher_remark;
		this.address = address;
	}

	public int getFresher_id() {
		return fresher_id;
	}

	public void setFresher_id(int fresher_id) {
		this.fresher_id = fresher_id;
	}

	public String getFresher_name() {
		return fresher_name;
	}

	public void setFresher_name(String fresher_name) {
		this.fresher_name = fresher_name;
	}

	public String getFresher_email() {
		return fresher_email;
	}

	public void setFresher_email(String fresher_email) {
		this.fresher_email = fresher_email;
	}

	public String getFresher_remark() {
		return fresher_remark;
	}

	public void setFresher_remark(String fresher_remark) {
		this.fresher_remark = fresher_remark;
	}

	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}

}
