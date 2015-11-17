package org.yenbo.commonDemo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

	@Expose
	@SerializedName("id")
	private int id;
	
	@Expose
	@SerializedName("name")
	private String name;
	
	@Expose
	@SerializedName("queryTime")
	private String queryTime;
	
	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getQueryTime() {
		return queryTime;
	}
	
	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}
}
