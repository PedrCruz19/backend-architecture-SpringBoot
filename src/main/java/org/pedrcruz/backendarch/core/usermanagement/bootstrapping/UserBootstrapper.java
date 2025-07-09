package org.pedrcruz.backendarch.core.usermanagement.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.Role;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.pedrcruz.backendarch.core.usermanagement.domain.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring will load and execute all components that implement the interface
 * CommandLineRunner on startup, so we will use that as a way to bootstrap some
 * data for testing purposes.
 * <p>
 * To enable this bootstrapping make sure you activate the spring
 * profile "bootstrap" in application.properties
 *
 * @author Paulo Gandra de Sousa
 *
 */
@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@Order(10)
public class UserBootstrapper implements CommandLineRunner {

	private final UserRepository userRepo;

	private final PasswordEncoder encoder;

	@Override
	@Transactional
	public void run(final String... args) throws Exception {
		// admin
		if (userRepo.findByUsername("u1@mail.com").isEmpty()) {
			final var u1 = new User("u1@mail.com", encoder.encode("Password1"));
			u1.addAuthority(new Role(Role.USER_ADMIN));
			userRepo.save(u1);
		}

		// user
		if (userRepo.findByUsername("mary@mail.com").isEmpty()) {
			final var u2 = new User("mary@mail.com", encoder.encode("myMy123!"));
			u2.addAuthority(new Role(Role.CUSTOMER));
			userRepo.save(u2);
		}

	}
}
