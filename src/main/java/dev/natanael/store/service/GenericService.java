package dev.natanael.store.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService<T, ID> {

	public Optional<T> findById(ID id);

	public Page<T> findAll(Pageable pageable);

	public Iterable<T> findAllById(Iterable<ID> ids);
	
	public T create(T entity);

	public void update(T entity);
	
	public void delete(T entity);

}
