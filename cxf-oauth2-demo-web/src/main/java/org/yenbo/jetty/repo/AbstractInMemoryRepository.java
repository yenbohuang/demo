package org.yenbo.jetty.repo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.jetty.exception.InMemoryEntityException;

public abstract class AbstractInMemoryRepository <T, K> {

	private static final Logger log = LoggerFactory.getLogger(AbstractInMemoryRepository.class);
	
	private Map<K, T> map = new HashMap<>();
	
	private void validateKey(K key) {
		
		if (null == key || StringUtils.isBlank(key.toString())) {
			throw new IllegalArgumentException("key is null or blank.");
		}
	}
	
	private void validateEntity(T entity) {
		
		if (null == entity) {
			throw new IllegalArgumentException("entity is null.");
		}
	}
	
	public void save(T entity, K key) {
		
		validateEntity(entity);
		validateKey(key);
		log.debug("Before save: {} entities", map.size());
		
		if (map.containsKey(key)) {
			InMemoryEntityException exception = new InMemoryEntityException(
					"Existing entity found");
			exception.addContextValue("key", key);
			throw exception;
		}
		
		map.put(key, entity);
		log.debug("Save: {}, {} entities", key, map.size());
	}
	
	public T get(K key) {
		
		validateKey(key);
		
		T entity = map.get(key);
		
		if (null == entity) {
			log.debug("Not found: {}", key);
		} else {
			log.debug("Found: {}", key);
		}
		
		return entity;
	}
	
	public void delete(K key) {
		
		validateKey(key);
		log.debug("Before delete: {} entities.", map.size());
		
		if (null == map.remove(key)) {
			log.warn("Not found: {}", key);
		} else {
			log.debug("Delete: {}, {} entities.", key, map.size());
		}
	}
	
	public void update(T entity, K key) {
		
		validateEntity(entity);
		validateKey(key);
		
		if (map.containsKey(key)) {
			map.put(key, entity);
		} else {
			InMemoryEntityException exception = new InMemoryEntityException(
					"Entity does not exist.");
			exception.addContextValue("key", key);
			throw exception;
		}
	}
}
