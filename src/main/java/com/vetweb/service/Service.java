package com.vetweb.service;

import java.util.List;

public interface Service<E> {

	List<E> all();
	
	void add(E entity);
	
	void remove(Long entity);
	
	E get(Long id);
	
}
