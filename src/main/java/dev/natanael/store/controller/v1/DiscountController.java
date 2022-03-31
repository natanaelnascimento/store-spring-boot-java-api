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

import dev.natanael.store.model.dto.DiscountCreationDTO;
import dev.natanael.store.model.dto.DiscountResponseDTO;
import dev.natanael.store.model.entity.DiscountEntity;
import dev.natanael.store.service.DiscountService;

@RestController
@RequestMapping("/v1/discounts")
public class DiscountController {

	@Autowired
	private DiscountService discountService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/{id}")
	public ResponseEntity<DiscountResponseDTO> findById(@PathVariable Long id) {
		Optional<DiscountEntity> discountEntity = discountService.findById(id);
		return ResponseEntity.ok(modelMapper.map(discountEntity.orElseThrow(EntityNotFoundException::new), DiscountResponseDTO.class));
	}

	@GetMapping("/byInstallments/{installments}")
	public ResponseEntity<DiscountResponseDTO> findById(@PathVariable Integer installments) {
		Optional<DiscountEntity> discountEntity = discountService.findByInstallments(installments);
		return ResponseEntity.ok(modelMapper.map(discountEntity.orElseThrow(EntityNotFoundException::new), DiscountResponseDTO.class));
	}

	@GetMapping
	public Page<DiscountResponseDTO> find(@PageableDefault(sort = {"description"}) Pageable pageable) {
		Page<DiscountEntity> discountEntities = discountService.findAll(pageable);
		return discountEntities.map(c -> modelMapper.map(c, DiscountResponseDTO.class));
	}

	@PostMapping
	public ResponseEntity<DiscountResponseDTO> create(@Valid @RequestBody DiscountCreationDTO discountDTO) {
		DiscountEntity discountEntity = modelMapper.map(discountDTO, DiscountEntity.class);
		discountEntity = discountService.create(discountEntity);
		return ResponseEntity.ok(modelMapper.map(discountEntity, DiscountResponseDTO.class));
	}

	@PutMapping("/{id}")
	public ResponseEntity<DiscountResponseDTO> update(@PathVariable Long id, @Valid @RequestBody DiscountCreationDTO discountDTO) {
		DiscountEntity discountEntity = modelMapper.map(discountDTO, DiscountEntity.class);
		discountEntity.setId(id);
		discountService.update(discountEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<DiscountResponseDTO> delete(@PathVariable Long id) {
		DiscountEntity discountEntity = new DiscountEntity();
		discountEntity.setId(id);
		discountService.delete(discountEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
