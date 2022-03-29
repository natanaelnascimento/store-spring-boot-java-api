package dev.natanael.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.natanael.store.model.entity.ClientEntity;

public interface ClientService extends GenericService<ClientEntity, Long> {
	
	public Page<ClientEntity> findByName(String name, Pageable pageable);

}