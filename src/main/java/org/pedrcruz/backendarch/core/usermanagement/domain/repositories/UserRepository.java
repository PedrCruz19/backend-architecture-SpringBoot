package org.pedrcruz.backendarch.core.usermanagement.domain.repositories;

import org.pedrcruz.backendarch.exceptions.NotFoundException;
import org.pedrcruz.backendarch.pagination.Page;
import org.pedrcruz.backendarch.core.usermanagement.application.SearchUsersQuery;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * @author Paulo Gandra de Sousa
 */
public interface UserRepository {

	<S extends User> List<S> saveAll(Iterable<S> entities);

	<S extends User> S save(S entity);

	Optional<User> findById(Long objectId);

	default User getById(final Long id) {
		final var maybeUser = findById(id);
		// throws 404 Not Found if the user does not exist or is not enabled
		return maybeUser.filter(User::isEnabled).orElseThrow(() -> new NotFoundException(User.class, id));
	}

	/**
	 * Get a user by ID and check its version matches the expected version
	 * @param id The user ID
	 * @param version The expected version
	 * @return The user if found and version matches
	 * @throws NotFoundException If user not found or not enabled
	 * @throws ResponseStatusException with PRECONDITION_FAILED if version doesn't match
	 */
	default User getByIdAndVersion(final Long id, final Long version) {
		final User user = getById(id);
		if (version != null && !version.equals(user.getVersion())) {
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
					"Resource has been modified by another user. Please refresh and try again.");
		}
		return user;
	}

	Optional<User> findByUsername(String username);

	List<User> searchUsers(Page page, SearchUsersQuery query);
}
