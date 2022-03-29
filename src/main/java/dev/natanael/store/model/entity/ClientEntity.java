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
@Table(name = "tb_client")
public class ClientEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Length(min = 2, max = 100)
	@Column(nullable = false, length = 100)
	private String name;

	@NotBlank
	@Length(min = 2, max = 100)
	@Column(nullable = false, length = 100)
	private String address;

	@NotNull
	@Positive
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal creditLimit;

	@NotNull
	@Positive
	@Column(nullable = false)
	private Integer installmentsLimit;

	@PrePersist
	@PreUpdate
	void scaleConfig() {
		creditLimit = creditLimit.setScale(2, RoundingMode.HALF_UP);
	}

}