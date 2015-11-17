package org.yenbo.commonDemo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DemoResponse {

	@Expose
	@SerializedName("method")
	private String methodName;
	
	@Expose
	@SerializedName("class")
	private String className;
	
	@Expose
	@SerializedName("time")
	private String time;
	
	@Expose
	@SerializedName("param")
	private String param;
	
	public DemoResponse() {
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
