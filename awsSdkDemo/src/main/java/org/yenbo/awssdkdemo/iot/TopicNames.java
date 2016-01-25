package org.yenbo.awssdkdemo.iot;

import org.yenbo.awssdkdemo.PropertyReader;

public class TopicNames {

	private static String getShadow(String pattern) {
		return String.format("$aws/things/%s/shadow/" + pattern,
				PropertyReader.getInstance().getParam("iot.thingName"));
	}
	
	public static String getShadowUpdate() {		
		return getShadow("update");
	}
	
	public static String getShadowUpdateAccepted() {
		return getShadow("update/accepted");
	}
	
	public static String getShadowUpdateRejected() {
		return getShadow("update/rejected");
	}
	
	public static String getShadowUpdateDelta() {
		return getShadow("update/delta");
	}
	
	public static String getShadowGet() {
		return getShadow("get");
	}
	
	public static String getShadowGetAccepted() {
		return getShadow("get/accepted");
	}
	
	public static String getShadowGetRejected() {
		return getShadow("get/rejected");
	}
	
	public static String getShadowDelete() {
		return getShadow("delete");
	}
	
	public static String getShadowDeleteAccepted() {
		return getShadow("delete/accepted");
	}
	
	public static String getShadowDeleteRejected() {
		return getShadow("delete/rejected");
	}
}
