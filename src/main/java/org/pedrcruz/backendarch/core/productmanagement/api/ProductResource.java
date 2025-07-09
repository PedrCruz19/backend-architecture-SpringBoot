package org.pedrcruz.backendarch.core.productmanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pedrcruz.backendarch.api.dto.PagedResponse;
import org.pedrcruz.backendarch.core.productmanagement.api.dto.ProductResponse;
import org.pedrcruz.backendarch.core.productmanagement.api.dto.CreateProductRequest;
import org.pedrcruz.backendarch.core.productmanagement.api.dto.UpdateProductRequest;
import org.pedrcruz.backendarch.core.productmanagement.api.mapper.ProductMapper;
import org.pedrcruz.backendarch.core.productmanagement.application.ProductService;
import org.pedrcruz.backendarch.core.productmanagement.application.SearchProductQuery;
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

@Tag(name = "Product Management", description = "Operations related to product management")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProductResource {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Create a new product", description = "Creates a new product with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Product already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody final CreateProductRequest request) {

        log.info("Creating new product with name: {}", request.getName());

        final var product = productService.createProduct(request);

        final var response = productMapper.toResponse(product);

        log.info("Product created successfully with ID: {}", product.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(org.pedrcruz.backendarch.api.dto.ApiResponse.created(response, "Product created successfully"));
    }

    @Operation(summary = "Get all products", description = "Retrieve all products with optional filtering and pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping
    @Cacheable(value = "products", key = "#page.toString() + '_' + (#query != null ? #query.toString() : 'all')")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<ProductResponse>>> getProducts(
            @Parameter(description = "Search query") final SearchProductQuery query,
            @Parameter(description = "Pagination parameters") final Page page) {

        log.debug("Retrieving products with query: {} and page: {}", query, page);

        final var products = productService.searchProducts(query, page);
        final var productResponses = products.stream()
                .map(productMapper::toResponse)
                .toList();

        final var pagedResponse = PagedResponse.of(
                productResponses,
                page != null ? page.getNumber() : 0,
                page != null ? page.getLimit() : productResponses.size(),
                productResponses.size()
        );

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Products retrieved successfully")
        );
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    @Cacheable(value = "products", key = "#id")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "Product ID") @PathVariable final Long id) {

        log.debug("Retrieving product with ID: {}", id);

        final var product = productService.getById(id);
        final var response = productMapper.toResponse(product);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(response, "Product retrieved successfully")
        );
    }

    @Operation(summary = "Update product", description = "Update an existing product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<ProductResponse>> updateProduct(
            @Parameter(description = "Product ID") @PathVariable final Long id,
            @Valid @RequestBody final UpdateProductRequest request) {

        log.info("Updating product with ID: {}", id);

        final var createRequest = new CreateProductRequest();
        createRequest.setName(request.getName());
        createRequest.setDescription(request.getDescription());
        createRequest.setPrice(request.getPrice());
        createRequest.setCategoryId(request.getCategoryId());
        createRequest.setImageUrl(request.getImageUrl());
        createRequest.setStockQuantity(request.getStockQuantity());

        final var product = productService.editProduct(id, createRequest);

        final var response = productMapper.toResponse(product);

        log.info("Product updated successfully with ID: {}", id);

        return ResponseEntity.ok(org.pedrcruz.backendarch.api.dto.ApiResponse.success(response, "Product updated successfully"));
    }

    @Operation(summary = "Delete product", description = "Soft delete a product (deactivate)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable final Long id) {

        log.info("Deleting product with ID: {}", id);

        productService.deleteProduct(id);

        log.info("Product deleted successfully with ID: {}", id);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(null, "Product deleted successfully")
        );
    }

    @Operation(summary = "Update product stock", description = "Update the stock quantity of a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<ProductResponse>> updateStock(
            @Parameter(description = "Product ID") @PathVariable final Long id,
            @Parameter(description = "New stock quantity") @RequestParam final int quantity) {

        log.info("Updating stock for product ID: {} to quantity: {}", id, quantity);

        final var product = productService.updateStock(id, quantity);
        final var response = productMapper.toResponse(product);

        log.info("Stock updated successfully for product ID: {}", id);

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(response, "Stock updated successfully")
        );
    }

    @Operation(summary = "Get active products", description = "Retrieve only active products")
    @GetMapping("/active")
    @Cacheable(value = "products", key = "'active'")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<List<ProductResponse>>> getActiveProducts() {

        log.debug("Retrieving active products");

        final var products = productService.findActiveProducts();
        final var productResponses = products.stream()
                .map(productMapper::toResponse)
                .toList();

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(productResponses, "Active products retrieved successfully")
        );
    }

    @Operation(summary = "Get products by category", description = "Retrieve products by category ID")
    @GetMapping("/category/{categoryId}")
    @Cacheable(value = "products", key = "'category_' + #categoryId")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @Parameter(description = "Category ID") @PathVariable final Long categoryId) {

        log.debug("Retrieving products for category ID: {}", categoryId);

        final var products = productService.findAll().stream()
                .filter(p -> p.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());

        final var productResponses = products.stream()
                .map(productMapper::toResponse)
                .toList();

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(productResponses, "Products retrieved successfully")
        );
    }

    @Operation(summary = "Get low stock products", description = "Retrieve products with low stock levels")
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<List<ProductResponse>>> getLowStockProducts() {

        log.debug("Retrieving low stock products");

        final var products = productService.findAll().stream()
                .filter(p -> p.getStockQuantity() < 10) // Low stock threshold
                .collect(Collectors.toList());

        final var productResponses = products.stream()
                .map(productMapper::toResponse)
                .toList();

        return ResponseEntity.ok(
                org.pedrcruz.backendarch.api.dto.ApiResponse.success(productResponses, "Low stock products retrieved successfully")
        );
    }
}
