package dev.natanael.store.controller.v1;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.natanael.store.model.dto.PasswordChangeDTO;
import dev.natanael.store.model.dto.UserCreationDTO;
import dev.natanael.store.model.dto.UserResponseDTO;
import dev.natanael.store.model.dto.UserUpdateDTO;
import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
		Optional<UserEntity> userEntity = userService.findById(id);
		return ResponseEntity.ok(modelMapper.map(userEntity.orElseThrow(EntityNotFoundException::new), UserResponseDTO.class));
	}

	@GetMapping
	public Page<UserResponseDTO> find(@RequestParam(name = "name", required = false) String name,
			@PageableDefault(sort = {"name"}) Pageable pageable) {
		Page<UserEntity> userEntities = name != null
				? userService.findByName(name, pageable)
				: userService.findAll(pageable);
		return userEntities.map(u -> modelMapper.map(u, UserResponseDTO.class));
	}

	@PostMapping
	public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreationDTO userDTO) {
		UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
		userEntity = userService.create(userEntity);
		return ResponseEntity.ok(modelMapper.map(userEntity, UserResponseDTO.class));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userDTO) {
		UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
		userEntity.setId(id);
		userService.update(userEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/password")
	public ResponseEntity<UserResponseDTO> changePassword(@RequestBody PasswordChangeDTO userDTO) {
		userService.changePassword(userDTO.getCurrentPassword(), userDTO.getNewPassword());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<UserResponseDTO> delete(@PathVariable Long id) {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(id);
		userService.delete(userEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
