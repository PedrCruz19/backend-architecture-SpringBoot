package org.pedrcruz.backendarch.core.usermanagement.application;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.exceptions.ConflictException;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.pedrcruz.backendarch.core.usermanagement.domain.repositories.UserRepository;
import org.pedrcruz.backendarch.pagination.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepo;
	private final EditUserMapper userEditMapper;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public User create(final CreateUserRequest request) {
		if (userRepo.findByUsername(request.getUsername()).isPresent()) {
			throw new ConflictException("Username already exists!");
		}
		if (!request.getPassword().equals(request.getRePassword())) {
			throw new ValidationException("Passwords don't match!");
		}

		final var user = userEditMapper.create(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		return userRepo.save(user);
	}

	@Transactional
	public User update(final Long id, final EditUserRequest request) {
		final var user = userRepo.getById(id);
		userEditMapper.update(request, user);

		return userRepo.save(user);
	}

	@Transactional
	public User update(final Long id, final EditUserRequest request, final Long version) {
			final var user = userRepo.getByIdAndVersion(id, version);
			userEditMapper.update(request, user);
			return userRepo.save(user);
	}

	@Transactional
	public User upsert(final CreateUserRequest request) {
		final var optionalUser = userRepo.findByUsername(request.getUsername());

		if (optionalUser.isEmpty()) {
			return create(request);
		}
		final var updateUserRequest = new EditUserRequest(request.getFullName(), request.getAuthorities());
		return update(optionalUser.get().getId(), updateUserRequest);
	}

	@Transactional
	public User delete(final Long id) {
		final var user = userRepo.getById(id);
		user.anonymizeAndDisable();
		return userRepo.save(user);
	}

	@Transactional
	public User delete(final Long id, final Long version) {
			final var user = userRepo.getByIdAndVersion(id, version);
			user.anonymizeAndDisable();
			return userRepo.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
	}

	public boolean usernameExists(final String username) {
		return userRepo.findByUsername(username).isPresent();
	}

	public User getUser(final Long id) {
		return userRepo.getById(id);
	}

	public List<User> searchUsers(Page page, SearchUsersQuery query) {
		if (page == null) {
			page = new Page(1, 10);
		}
		if (query == null) {
			query = new SearchUsersQuery("", "");
		}
		return userRepo.searchUsers(page, query);
	}
}
