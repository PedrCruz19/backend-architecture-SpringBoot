package org.pedrcruz.backendarch.core.usermanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.api.ListResponse;
import org.pedrcruz.backendarch.core.usermanagement.application.*;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.Role;
import org.pedrcruz.backendarch.pagination.SearchRequest;
import org.pedrcruz.backendarch.util.api.AbstractResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Tag(name = "UserAdmin")
@RestController
@RequestMapping(path = "api/admin/user")
@RolesAllowed(Role.USER_ADMIN)
@RequiredArgsConstructor
public class UserAdminApi extends AbstractResource {

	private final UserService userService;
	private final UserApiViewMapper userViewMapper;

	@Operation(summary = "Create a new user")
	@PostMapping
	public UserView create(@RequestBody @Valid final CreateUserRequest request) {
		final var user = userService.create(request);
		return userViewMapper.toUserView(user);
	}

	@Operation(summary = "Update an existing user")
	@PutMapping("{id}")
	public UserView update(@PathVariable final Long id, @RequestBody @Valid final EditUserRequest request,
						   final WebRequest webRequest) {
		final String ifMatchValue = ensureIfMatchHeader(webRequest);
		final Long version = getVersionFromIfMatchHeader(ifMatchValue);
		final var user = userService.update(id, request, version);
		return userViewMapper.toUserView(user);
	}

	@Operation(summary = "Delete a user")
	@DeleteMapping("{id}")
	public UserView delete(@PathVariable final Long id, final WebRequest webRequest) {
		final String ifMatchValue = ensureIfMatchHeader(webRequest);
		final Long version = getVersionFromIfMatchHeader(ifMatchValue);
		final var user = userService.delete(id, version);
		return userViewMapper.toUserView(user);
	}

	@Operation(summary = "Get a user by id")
	@GetMapping("{id}")
	public UserView get(@PathVariable final Long id) {
		final var user = userService.getUser(id);
		return userViewMapper.toUserView(user);
	}

	@Operation(summary = "Search for users")
	@PostMapping("search")
	public ListResponse<UserView> search(@RequestBody final SearchRequest<SearchUsersQuery> request) {
		final var searchUsers = userService.searchUsers(request.getPage(), request.getQuery());
		return new ListResponse<>(userViewMapper.toUserView(searchUsers));
	}
}
