package org.pedrcruz.backendarch.core.usermanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pedrcruz.backendarch.api.dto.PagedResponse;
import org.pedrcruz.backendarch.core.usermanagement.api.dto.CreateUserRequest;
import org.pedrcruz.backendarch.core.usermanagement.api.dto.UpdateUserRequest;
import org.pedrcruz.backendarch.core.usermanagement.api.dto.UserResponse;
import org.pedrcruz.backendarch.core.usermanagement.api.mapper.UserMapper;
import org.pedrcruz.backendarch.core.usermanagement.application.SearchUsersQuery;
import org.pedrcruz.backendarch.core.usermanagement.application.UserService;
import org.pedrcruz.backendarch.pagination.Page;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "User Management", description = "Operations related to user management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserResource {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody final CreateUserRequest request) {

        log.info("Creating new user with username: {}", request.getUsername());

        final var applicationRequest = userMapper.toApplicationRequest(request);
        final var user = userService.create(applicationRequest);
        final var response = userMapper.toResponse(user);

        log.info("User created successfully with ID: {}", user.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(org.pedrcruz.backendarch.api.dto.ApiResponse.created(response, "User created successfully"));
    }

    @Operation(summary = "Get all users", description = "Retrieve all users with optional filtering and pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Cacheable(value = "users", key = "#page.toString() + '_' + (#query != null ? #query.toString() : 'all')")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<UserResponse>>> getUsers(
            @Parameter(description = "Search query") final SearchUsersQuery query,
            @Parameter(description = "Pagination parameters") final Page page) {

        log.debug("Retrieving users with query: {} and page: {}", query, page);

        final var users = userService.searchUsers(page, query);
        final var userResponses = users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());

        final var pagedResponse = PagedResponse.of(
                userResponses,
                page != null ? page.getNumber() : 0,
                page != null ? page.getLimit() : userResponses.size(),
                userResponses.size()
        );

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Users retrieved successfully")
        );
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Cacheable(value = "users", key = "#id")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<UserResponse>> getUserById(
            @Parameter(description = "User ID") @PathVariable final Long id) {

        log.debug("Retrieving user with ID: {}", id);

        final var user = userService.getUser(id);
        final var response = userMapper.toResponse(user);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(response, "User retrieved successfully")
        );
    }

    @Operation(summary = "Update user", description = "Update an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<UserResponse>> updateUser(
            @Parameter(description = "User ID") @PathVariable final Long id,
            @Valid @RequestBody final UpdateUserRequest request) {

        log.info("Updating user with ID: {}", id);

        final var applicationRequest = userMapper.toApplicationEditRequest(request);
        final var user = userService.update(id, applicationRequest);
        final var response = userMapper.toResponse(user);

        log.info("User updated successfully with ID: {}", id);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(response, "User updated successfully")
        );
    }

    @Operation(summary = "Delete user", description = "Soft delete a user (anonymize and disable)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<Void>> deleteUser(
            @Parameter(description = "User ID") @PathVariable final Long id) {

        log.info("Deleting user with ID: {}", id);

        userService.delete(id);

        log.info("User deleted successfully with ID: {}", id);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(null, "User deleted successfully")
        );
    }

    @Operation(summary = "Check if username exists", description = "Check if a username is already taken")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Username availability checked"),
        @ApiResponse(responseCode = "400", description = "Invalid username")
    })
    @GetMapping("/check-username")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<Boolean>> checkUsernameExists(
            @Parameter(description = "Username to check") @RequestParam final String username) {

        log.debug("Checking if username exists: {}", username);

        final var exists = userService.usernameExists(username);
        final var message = exists ? "Username is already taken" : "Username is available";

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(exists, message)
        );
    }

    @Operation(summary = "Get current user profile", description = "Get the profile of the currently authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Current user profile retrieved"),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @GetMapping("/profile")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<UserResponse>> getCurrentUserProfile(
            @Parameter(hidden = true) final org.springframework.security.core.Authentication authentication) {

        log.debug("Retrieving current user profile");

        final var currentUser = (org.pedrcruz.backendarch.core.usermanagement.domain.model.User) authentication.getPrincipal();
        final var response = userMapper.toResponse(currentUser);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(response, "Current user profile retrieved successfully")
        );
    }
}
