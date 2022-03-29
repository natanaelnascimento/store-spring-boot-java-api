
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

import dev.natanael.store.model.dto.ProductCreationDTO;
import dev.natanael.store.model.dto.ProductResponseDTO;
import dev.natanael.store.model.entity.ProductEntity;
import dev.natanael.store.service.ProductService;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
		Optional<ProductEntity> productEntity = productService.findById(id);
		return ResponseEntity.ok(modelMapper.map(productEntity.orElseThrow(EntityNotFoundException::new), ProductResponseDTO.class));
	}

	@GetMapping
	public Page<ProductResponseDTO> find(@RequestParam(name = "name", required = false) String name,
			@PageableDefault(sort = {"name"}) Pageable pageable) {
		Page<ProductEntity> productEntities = name != null
				? productService.findByName(name, pageable)
				: productService.findAll(pageable);
		System.out.println();
		return productEntities.map(p -> modelMapper.map(p, ProductResponseDTO.class));
	}

	@PostMapping
	public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductCreationDTO productDTO) {
		ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);
		productEntity = productService.create(productEntity);
		return ResponseEntity.ok(modelMapper.map(productEntity, ProductResponseDTO.class));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProductCreationDTO productDTO) {
		ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);
		productEntity.setId(id);
		productService.update(productEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> delete(@PathVariable Long id) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(id);
		productService.delete(productEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
