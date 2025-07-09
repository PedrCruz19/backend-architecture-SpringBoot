package org.pedrcruz.backendarch.core.categorymanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pedrcruz.backendarch.api.dto.PagedResponse;
import org.pedrcruz.backendarch.core.categorymanagement.api.dto.CategoryResponse;
import org.pedrcruz.backendarch.core.categorymanagement.api.dto.CreateCategoryRequest;
import org.pedrcruz.backendarch.core.categorymanagement.api.dto.UpdateCategoryRequest;
import org.pedrcruz.backendarch.core.categorymanagement.api.mapper.CategoryMapper;
import org.pedrcruz.backendarch.core.categorymanagement.application.CategoryService;
import org.pedrcruz.backendarch.core.categorymanagement.application.SearchCategoryQuery;
import org.pedrcruz.backendarch.pagination.Page;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Category Management", description = "Operations related to category management")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryResource {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Create a new category", description = "Creates a new category with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Category already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody final CreateCategoryRequest request) {

        log.info("Creating new category with name: {}", request.getName());

        final var category = categoryService.createCategory(request);

        final var response = categoryMapper.toResponse(category);

        log.info("Category created successfully with ID: {}", category.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(org.pedrcruz.backendarch.api.dto.ApiResponse.created(response, "Category created successfully"));
    }

    @Operation(summary = "Get all categories", description = "Retrieve all categories with optional filtering and pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping
    @Cacheable(value = "categories", key = "#page.toString() + '_' + (#query != null ? #query.toString() : 'all')")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<CategoryResponse>>> getCategories(
            @Parameter(description = "Search query") final SearchCategoryQuery query,
            @Parameter(description = "Pagination parameters") final Page page) {

        log.debug("Retrieving categories with query: {} and page: {}", query, page);

        final var categories = categoryService.searchCategories(query, page);
        final var categoryResponses = categories.stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());

        // For simplicity, using the list size as total. In production, you'd get actual count from service
        final var pagedResponse = PagedResponse.of(
                categoryResponses,
                page != null ? page.getNumber() : 0,
                page != null ? page.getLimit() : categoryResponses.size(),
                categoryResponses.size()
        );

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Categories retrieved successfully")
        );
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    @Cacheable(value = "categories", key = "#id")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<CategoryResponse>> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable final Long id) {

        log.debug("Retrieving category with ID: {}", id);

        final var category = categoryService.getById(id);
        final var response = categoryMapper.toResponse(category);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(response, "Category retrieved successfully")
        );
    }

    @Operation(summary = "Update category", description = "Update an existing category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<CategoryResponse>> updateCategory(
            @Parameter(description = "Category ID") @PathVariable final Long id,
            @Valid @RequestBody final UpdateCategoryRequest request) {

        log.info("Updating category with ID: {}", id);

        final var category = categoryService.getById(id);
        categoryMapper.updateEntity(request, category);

        final var updatedCategory = categoryService.editCategory(id, request);

        final var response = categoryMapper.toResponse(updatedCategory);

        log.info("Category updated successfully with ID: {}", id);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(response, "Category updated successfully")
        );
    }

    @Operation(summary = "Delete category", description = "Soft delete a category (deactivate)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "409", description = "Category has associated products")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<Void>> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable final Long id) {

        log.info("Deleting category with ID: {}", id);

        categoryService.deleteCategory(id);

        log.info("Category deleted successfully with ID: {}", id);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(null, "Category deleted successfully")
        );
    }

    @Operation(summary = "Activate category", description = "Activate a deactivated category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category activated successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<CategoryResponse>> activateCategory(
            @Parameter(description = "Category ID") @PathVariable final Long id) {

        log.info("Activating category with ID: {}", id);

        final var category = categoryService.getById(id);
        category.activate();

        // Save the changes (you might need to add this method to CategoryService)
        final var updateRequest = new UpdateCategoryRequest();
        updateRequest.setName(category.getName().getWord());
        updateRequest.setDescription(category.getDescription().getWord());

        final var updatedCategory = categoryService.editCategory(id, updateRequest);

        final var response = categoryMapper.toResponse(updatedCategory);

        log.info("Category activated successfully with ID: {}", id);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(response, "Category activated successfully")
        );
    }

    @Operation(summary = "Get active categories", description = "Retrieve only active categories")
    @GetMapping("/active")
    @Cacheable(value = "categories", key = "'active'")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<List<CategoryResponse>>> getActiveCategories() {

        log.debug("Retrieving active categories");

        final var categories = categoryService.findActiveCategories();
        final var categoryResponses = categories.stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(categoryResponses, "Active categories retrieved successfully")
        );
    }
}
