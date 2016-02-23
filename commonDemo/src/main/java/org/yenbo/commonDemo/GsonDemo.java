package org.yenbo.commonDemo;

import java.time.ZonedDateTime;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.commonDemo.response.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class GsonDemo {

	private static final Logger log = LoggerFactory.getLogger(GsonDemo.class);
	
	private static Gson gson = new Gson();
	
	public static void main(String[] args) {
		
		hashMapToJson();
		serilizeObject();
		deserilizeObject();
	}

	private static void hashMapToJson() {
		
		HashMap<String, Object> props = new HashMap<>();
		props.put("time", ZonedDateTime.now().toString());
		props.put("millis", ZonedDateTime.now().toEpochSecond());
		
		log.info(gson.toJson(props));
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("innerObj", gson.toJsonTree(props));
		
		log.info(gson.toJson(jsonObject));
	}
	
	private static void serilizeObject() {
		
		User user = new User();
		user.setId(1);
		
		log.info(gson.toJson(user));
		log.info(new GsonBuilder().serializeNulls().create().toJson(user));
	}
	
	private static void deserilizeObject() {
		
		User user = gson.fromJson("{\"id\":1,\"name\":\"my name\",\"queryTime\":\"my time\"}",
				User.class);
		
		log.info("id={}, name={}, queryTime={}", user.getId(), user.getName(),
				user.getQueryTime());
	}
}
