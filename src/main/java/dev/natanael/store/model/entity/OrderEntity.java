package dev.natanael.store.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_order")
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	private ClientEntity client;

	@NotNull
	@ManyToOne
	private UserEntity user;

	@NotEmpty 
	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	private List<OrderItemEntity> items;

	@NotNull
	@PastOrPresent
	@Column(nullable = false)
	private LocalDateTime dateTime;

	@NotNull
	@PositiveOrZero
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal discount;

	@NotNull
	@Positive
	@Column(nullable = false)
	private Integer installments;

	@PrePersist
	@PreUpdate
	void scaleConfig() {
		discount = discount.setScale(2, RoundingMode.HALF_UP);
	}

}