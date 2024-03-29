package com.akshay.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Project {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int Project_id;
	String Project_name;
	String Project_manager;

	public Project() {
		super();
	}

	public Project(int project_id, String project_name, String project_manager) {
		super();
		Project_id = project_id;
		Project_name = project_name;
		Project_manager = project_manager;
	}

	public int getProject_id() {
		return Project_id;
	}

	public void setProject_id(int project_id) {
		Project_id = project_id;
	}

	public String getProject_name() {
		return Project_name;
	}

	public void setProject_name(String project_name) {
		Project_name = project_name;
	}

	public String getProject_manager() {
		return Project_manager;
	}

	public void setProject_manager(String project_manager) {
		Project_manager = project_manager;
	}

}
