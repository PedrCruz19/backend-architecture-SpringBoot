package org.pedrcruz.backendarch.core.usermanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.exceptions.NotFoundException;
import org.pedrcruz.backendarch.pagination.Page;
import org.pedrcruz.backendarch.core.usermanagement.application.SearchUsersQuery;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.pedrcruz.backendarch.core.usermanagement.domain.repositories.UserRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Repository
@CacheConfig(cacheNames = "users")
public interface SpringDataUserRepository extends UserRepository, UserRepoCustom, CrudRepository<User, Long> {

	@Override
	@CacheEvict(allEntries = true)
	<S extends User> List<S> saveAll(Iterable<S> entities);

	@Override
	@Caching(evict = { @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
			@CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
	<S extends User> S save(S entity);

	/**
	 * findById searches a specific user and returns an optional
	 */
	@Override
	@Cacheable
	Optional<User> findById(Long objectId);

	/**
	 * getById explicitly loads a user or throws an exception if the user does not
	 * exist or the account is not enabled
	 *
	 * @param id
	 * @return the user
	 */
	@Override
	@Cacheable
	default User getById(final Long id) {
		final var maybeUser = findById(id);
		// throws 404 Not Found if the user does not exist or is not enabled
		return maybeUser.filter(User::isEnabled).orElseThrow(() -> new NotFoundException(User.class, id));
	}

	@Override
	@Cacheable
	Optional<User> findByUsername(String username);

	/**
	 * Get a user by ID and check its version matches the expected version
	 * @param id The user ID
	 * @param version The expected version
	 * @return The user if found and version matches
	 * @throws NotFoundException If user not found or not enabled
	 * @throws ResponseStatusException with PRECONDITION_FAILED if version doesn't match
	 */
	@Override
	@Cacheable
	default User getByIdAndVersion(final Long id, final Long version) {
		final User user = getById(id);
		if (version != null && !version.equals(user.getVersion())) {
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
					"Resource has been modified by another user. Please refresh and try again.");
		}
		return user;
	}
}

/**
 * Custom interface to add custom methods to spring repository.
 *
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.custom-implementations">...</a>
 *
 *
 */
interface UserRepoCustom {

	List<User> searchUsers(Page page, SearchUsersQuery query);
}

/**
 * use JPA Criteria API to build the custom query
 *
 * @see <a href="https://www.baeldung.com/hibernate-criteria-queries">...</a>
 *
 */
@RequiredArgsConstructor
class UserRepoCustomImpl implements UserRepoCustom {

	// get the underlying JPA Entity Manager via spring thru constructor dependency
	// injection
	private final EntityManager em;

	@Override
	public List<User> searchUsers(final Page page, final SearchUsersQuery query) {

		final var cb = em.getCriteriaBuilder();
		final CriteriaQuery<User> cq = cb.createQuery(User.class);
		final Root<User> root = cq.from(User.class);
		cq.select(root);

		final List<Predicate> where = new ArrayList<>();
		if (StringUtils.hasText(query.getUsername())) {
			where.add(cb.equal(root.get("username"), query.getUsername()));
		}
		if (StringUtils.hasText(query.getFullName())) {
			where.add(cb.like(root.get("fullName"), "%" + query.getFullName() + "%"));
		}

		// search using OR
		if (!where.isEmpty()) {
			cq.where(cb.or(where.toArray(new Predicate[0])));
		}

		cq.orderBy(cb.desc(root.get("createdAt")));

		final TypedQuery<User> q = em.createQuery(cq);
		q.setFirstResult((page.getNumber() - 1) * page.getLimit());
		q.setMaxResults(page.getLimit());

		return q.getResultList();
	}
}
