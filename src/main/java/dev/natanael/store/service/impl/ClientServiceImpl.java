package dev.natanael.store.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.natanael.store.model.entity.ClientEntity;
import dev.natanael.store.repository.ClientRepository;
import dev.natanael.store.service.ClientService;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Override
	public Optional<ClientEntity> findById(Long id) {
		return clientRepository.findById(id);
	}

	@Override
	public Page<ClientEntity> findAll(Pageable pageable) {
		return clientRepository.findAll(pageable);
	}

	@Override
	public Iterable<ClientEntity> findAllById(Iterable<Long> ids) {
		return clientRepository.findAllById(ids);
	}

	@Override
	public ClientEntity create(ClientEntity entity) {
		return clientRepository.save(entity);
	}

	@Override
	public void update(ClientEntity entity) {
		clientRepository.save(entity);
	}

	@Override
	public void delete(ClientEntity entity) {
		clientRepository.delete(entity);
	}

	@Override
	public Page<ClientEntity> findByName(String name, Pageable pageable) {
		return clientRepository.findByNameIgnoreCaseContaining(name, pageable);
	}

}
