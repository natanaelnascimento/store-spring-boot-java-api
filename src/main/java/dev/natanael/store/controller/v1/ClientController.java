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

import dev.natanael.store.model.dto.ClientCreationDTO;
import dev.natanael.store.model.dto.ClientResponseDTO;
import dev.natanael.store.model.entity.ClientEntity;
import dev.natanael.store.service.ClientService;

@RestController
@RequestMapping("/v1/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/{id}")
	public ResponseEntity<ClientResponseDTO> findById(@PathVariable Long id) {
		Optional<ClientEntity> clientEntity = clientService.findById(id);
		return ResponseEntity.ok(modelMapper.map(clientEntity.orElseThrow(EntityNotFoundException::new), ClientResponseDTO.class));
	}

	@GetMapping
	public Page<ClientResponseDTO> find(@RequestParam(name = "name", required = false) String name,
			@PageableDefault(sort = {"name"}) Pageable pageable) {
		Page<ClientEntity> clientEntities = name != null
				? clientService.findByName(name, pageable)
				: clientService.findAll(pageable);
		return clientEntities.map(c -> modelMapper.map(c, ClientResponseDTO.class));
	}

	@PostMapping
	public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientCreationDTO clientDTO) {
		ClientEntity clientEntity = modelMapper.map(clientDTO, ClientEntity.class);
		clientEntity = clientService.create(clientEntity);
		return ResponseEntity.ok(modelMapper.map(clientEntity, ClientResponseDTO.class));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClientResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ClientCreationDTO clientDTO) {
		ClientEntity clientEntity = modelMapper.map(clientDTO, ClientEntity.class);
		clientEntity.setId(id);
		clientService.update(clientEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ClientResponseDTO> delete(@PathVariable Long id) {
		ClientEntity clientEntity = new ClientEntity();
		clientEntity.setId(id);
		clientService.delete(clientEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
