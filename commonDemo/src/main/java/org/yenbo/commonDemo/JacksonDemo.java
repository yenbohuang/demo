package org.yenbo.commonDemo;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonDemo {

	private static final Logger log = LoggerFactory.getLogger(JacksonDemo.class);
	
	public class Entity {
		private int intValue;
		private double doubleValue;
		private BigDecimal bigDecimalValue;
		
		public int getIntValue() {
			return intValue;
		}
		
		public void setIntValue(int intValue) {
			this.intValue = intValue;
		}
		
		public double getDoubleValue() {
			return doubleValue;
		}
		
		public void setDoubleValue(double doubleValue) {
			this.doubleValue = doubleValue;
		}
		
		public BigDecimal getBigDecimalValue() {
			return bigDecimalValue;
		}
		
		public void setBigDecimalValue(BigDecimal bigDecimalValue) {
			this.bigDecimalValue = bigDecimalValue;
		}
	}
	
	public static void main(String[] args) {
		
		new JacksonDemo().scientificNotation();
	}
	
	private void scientificNotation() {
		
		double value = 0.00000007396384458953345;
		
		Entity entity = new Entity();
		entity.setIntValue(12345);
		entity.setDoubleValue(value);
		entity.setBigDecimalValue(BigDecimal.valueOf(value));
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			log.info("Double value: {}", value);
			log.info("Double value: {}", Math.round(value * 1000000) / 1000000.0);
			log.info("Use scientific notation: {}", objectMapper.writeValueAsString(entity));
			
			log.info("Disable scientific notation: {}",
					objectMapper.writer()
					.with(SerializationFeature.INDENT_OUTPUT)
					.with(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
					.writeValueAsString(entity));
		} catch (JsonProcessingException ex) {
			log.error(ex.getMessage(), ex);
		}
	}
}
