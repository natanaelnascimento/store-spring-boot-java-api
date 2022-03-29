package dev.natanael.store.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_user_session")
public class UserSessionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false, length = 255)
	private String accessTokenSecret;

	@Column(nullable = false)
	private String refreshTokenSecret;

	@NotNull
	@ManyToOne
	private UserEntity user;

	@NotNull
	@PastOrPresent
	@Column(nullable = false)
	private LocalDateTime dateTime;

}