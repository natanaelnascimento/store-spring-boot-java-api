package dev.natanael.store.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_product")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Length(min = 2, max = 100)
	@Column(nullable = false, length = 100)
	private String name;

	@NotBlank
	@Length(min = 5, max = 255)
	@Column(nullable = false, length = 255)
	private String description;

	@NotNull
	@Positive
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal price;

	@PrePersist
	@PreUpdate
	void scaleConfig() {
		price = price.setScale(2, RoundingMode.HALF_UP);
	}

}