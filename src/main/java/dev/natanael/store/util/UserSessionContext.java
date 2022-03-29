package dev.natanael.store.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.natanael.store.model.entity.UserSessionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public class UserSessionContext {

	private Optional<UserSessionEntity> userSession = Optional.empty();

	public void setUserSession(UserSessionEntity userSession) {
		this.userSession = userSession != null ? Optional.of(userSession) : Optional.empty();
	}

}
