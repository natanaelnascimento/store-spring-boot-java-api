
package dev.natanael.store.controller.v1;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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

import dev.natanael.store.model.dto.OrderCreationDTO;
import dev.natanael.store.model.dto.OrderResponseDTO;
import dev.natanael.store.model.entity.ClientEntity;
import dev.natanael.store.model.entity.OrderEntity;
import dev.natanael.store.model.entity.ProductEntity;
import dev.natanael.store.model.entity.UserEntity;
import dev.natanael.store.service.OrderService;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> findById(@PathVariable Long id) {
		Optional<OrderEntity> orderEntity = orderService.findById(id);
		return ResponseEntity.ok(modelMapper.map(orderEntity.orElseThrow(EntityNotFoundException::new), OrderResponseDTO.class));
	}

	@GetMapping
	public Page<OrderResponseDTO> find(@PageableDefault(sort = {"dateTime"}, direction = Direction.DESC) Pageable pageable) {
		Page<OrderEntity> orderEntities = orderService.findAll(pageable);
		return orderEntities.map(o -> modelMapper.map(o, OrderResponseDTO.class));
	}

	@GetMapping("/byClient/{id}")
	public Page<OrderResponseDTO> findByClient(@PathVariable Long id, 
			@PageableDefault(sort = {"dateTime"}, direction = Direction.DESC) Pageable pageable) {
		ClientEntity clientEntity = new ClientEntity();
		clientEntity.setId(id);
		Page<OrderEntity> orderEntities = orderService.findByClient(clientEntity, pageable);
		return orderEntities.map(o -> modelMapper.map(o, OrderResponseDTO.class));
	}

	@GetMapping("/byUser/{id}")
	public Page<OrderResponseDTO> findByUser(@PathVariable Long id, 
			@PageableDefault(sort = {"dateTime"}, direction = Direction.DESC) Pageable pageable) {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(id);
		Page<OrderEntity> orderEntities = orderService.findByUser(userEntity, pageable);
		return orderEntities.map(o -> modelMapper.map(o, OrderResponseDTO.class));
	}

	@GetMapping("/byProduct/{id}")
	public Page<OrderResponseDTO> findByProduct(@PathVariable Long id, 
			@PageableDefault(sort = {"dateTime"}, direction = Direction.DESC) Pageable pageable) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(id);
		Page<OrderEntity> orderEntities = orderService.findByProduct(productEntity, pageable);
		return orderEntities.map(o -> modelMapper.map(o, OrderResponseDTO.class));
	}

	@PostMapping
	public ResponseEntity<OrderResponseDTO> create(@Valid @RequestBody OrderCreationDTO orderDTO) {
		OrderEntity orderEntity = modelMapper.map(orderDTO, OrderEntity.class);
		orderEntity.setId(null);
		orderEntity.getItems().forEach(o -> {o.setId(null);});
		orderEntity = orderService.create(orderEntity);
		return ResponseEntity.ok(modelMapper.map(orderEntity, OrderResponseDTO.class));
	}

	@PutMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> update(@PathVariable Long id, @Valid @RequestBody OrderCreationDTO orderDTO) {
		OrderEntity orderEntity = modelMapper.map(orderDTO, OrderEntity.class);
		orderEntity.setId(id);
		orderEntity.getItems().forEach(o -> {o.setId(null);});
		orderService.update(orderEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> delete(@PathVariable Long id) {
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(id);
		orderService.delete(orderEntity);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
