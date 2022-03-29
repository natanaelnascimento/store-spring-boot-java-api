package dev.natanael.store.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_order_item")
public class OrderItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	private ProductEntity product;

	@NotNull
	@Positive
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal price;

	@NotNull
	@Positive
	@Column(nullable = false)
	private Integer quantity;

	@PrePersist
	@PreUpdate
	void scaleConfig() {
		price = price.setScale(2, RoundingMode.HALF_UP);
	}

}