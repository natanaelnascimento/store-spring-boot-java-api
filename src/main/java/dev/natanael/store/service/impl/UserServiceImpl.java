package dev.natanael.store.service.impl;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.repository.UserRepository;
import dev.natanael.store.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Override
	public Optional<UserEntity> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public Page<UserEntity> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public Iterable<UserEntity> findAllById(Iterable<Long> ids) {
		return userRepository.findAllById(ids);
	}

	@Override
	public UserEntity create(UserEntity entity) {
		entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		return userRepository.save(entity);
	}

	@Override
	public void update(UserEntity entity) {
		UserEntity savedUser = userRepository.findById(entity.getId())
				.orElseThrow(EntityNotFoundException::new);
		entity.setPassword(savedUser.getPassword());
		userRepository.save(entity);
	}

	@Override
	public void delete(UserEntity entity) {
		userRepository.delete(entity);
	}

	@Override
	public Page<UserEntity> findByName(String name, Pageable pageable) {
		return userRepository.findByNameIgnoreCaseContaining(name, pageable);
	}

	@Override
	public Optional<UserEntity> findByUsername(String username) {
		return userRepository.findByUsernameIgnoreCase(username);
	}

}