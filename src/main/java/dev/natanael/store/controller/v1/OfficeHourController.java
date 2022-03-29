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
import org.springframework.web.bind.annotation.RestController;

import dev.natanael.store.model.dto.OfficeHourCreationDTO;
import dev.natanael.store.model.dto.OfficeHourResponseDTO;
import dev.natanael.store.model.entity.OfficeHourEntity;
import dev.natanael.store.service.OfficeHourService;

@RestController
@RequestMapping("/v1/officeHours")
public class OfficeHourController {

	@Autowired
	private OfficeHourService officeHourService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/{id}")
	public ResponseEntity<OfficeHourResponseDTO> findById(@PathVariable Long id) {
		Optional<OfficeHourEntity> officeHourEntity = officeHourService.findById(id);
		return ResponseEntity.ok(modelMapper.map(officeHourEntity.orElseThrow(EntityNotFoundException::new), OfficeHourResponseDTO.class));
	}

	@GetMapping("/byDayOfWeek/{dayOfWeek}")
	public Page<OfficeHourResponseDTO> findById(@PathVariable Integer dayOfWeek,
			@PageableDefault(page = 1, size = 10, sort = {"startTime"}) Pageable pageable) {
		Page<OfficeHourEntity> officeHourEntities = officeHourService.findByDayOfWeek(dayOfWeek, pageable);
		return officeHourEntities.map(c -> modelMapper.map(c, OfficeHourResponseDTO.class));
	}

	@GetMapping
	public Page<OfficeHourResponseDTO> find(@PageableDefault(sort = {"name"}) Pageable pageable) {
		Page<OfficeHourEntity> officeHourEntities = officeHourService.findAll(pageable);
		return officeHourEntities.map(c -> modelMapper.map(c, OfficeHourResponseDTO.class));
	}

	@PostMapping
	public ResponseEntity<OfficeHourResponseDTO> create(@Valid @RequestBody OfficeHourCreationDTO officeHourDTO) {
		OfficeHourEntity officeHourEntity = modelMapper.map(officeHourDTO, OfficeHourEntity.class);
		officeHourEntity = officeHourService.create(officeHourEntity);
		return ResponseEntity.ok(modelMapper.map(officeHourEntity, OfficeHourResponseDTO.class));
	}

	@PutMapping("/{id}")
	public ResponseEntity<OfficeHourResponseDTO> update(@PathVariable Long id, @Valid @RequestBody OfficeHourCreationDTO officeHourDTO) {
		OfficeHourEntity officeHourEntity = modelMapper.map(officeHourDTO, OfficeHourEntity.class);
		officeHourEntity.setId(id);
		officeHourService.update(officeHourEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<OfficeHourResponseDTO> delete(@PathVariable Long id) {
		OfficeHourEntity officeHourEntity = new OfficeHourEntity();
		officeHourEntity.setId(id);
		officeHourService.delete(officeHourEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
