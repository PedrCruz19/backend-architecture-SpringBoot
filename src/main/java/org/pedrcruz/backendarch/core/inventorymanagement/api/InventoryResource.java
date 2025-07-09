package org.pedrcruz.backendarch.core.inventorymanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.api.dto.PagedResponse;
import org.pedrcruz.backendarch.core.inventorymanagement.application.InventoryService;
import org.pedrcruz.backendarch.core.inventorymanagement.domain.model.Inventory;
import org.pedrcruz.backendarch.core.productmanagement.application.ProductService;
import org.pedrcruz.backendarch.pagination.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "API for managing inventory")
public class InventoryResource {

    private final InventoryService inventoryService;
    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create inventory", description = "Create a new inventory entry for a product")
    public ResponseEntity<Inventory> createInventory(@RequestBody final CreateInventoryRequest request) {
        final var product = productService.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        final var inventory = inventoryService.createInventory(
                product,
                request.getCurrentQuantity(),
                request.getMinimumStockLevel(),
                request.getMaximumStockLevel(),
                request.getReorderPoint(),
                request.getReorderQuantity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }

    @GetMapping
    @Operation(summary = "Get all inventories", description = "Retrieve all inventory entries with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Inventory>>> getAllInventories(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var inventories = inventoryService.findAll();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, inventories.size());

        final var paginatedInventories = inventories.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedInventories, pageNumber, pageSize, inventories.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Inventories retrieved successfully")
        );
    }

    @GetMapping("/active")
    @Operation(summary = "Get active inventories", description = "Retrieve all active inventory entries with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Inventory>>> getActiveInventories(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var inventories = inventoryService.findActiveInventories();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, inventories.size());

        final var paginatedInventories = inventories.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedInventories, pageNumber, pageSize, inventories.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Active inventories retrieved successfully")
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory by ID", description = "Retrieve inventory by ID")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable final Long id) {
        final var inventory = inventoryService.findById(id);
        return inventory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get inventory by product ID", description = "Retrieve inventory by product ID")
    public ResponseEntity<Inventory> getInventoryByProductId(@PathVariable final Long productId) {
        final var inventory = inventoryService.findByProductId(productId);
        return inventory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory", description = "Update an existing inventory entry")
    public ResponseEntity<Inventory> updateInventory(@PathVariable final Long id,
                                                   @RequestBody final UpdateInventoryRequest request) {
        final var inventory = inventoryService.updateInventory(
                id,
                request.getCurrentQuantity(),
                request.getMinimumStockLevel(),
                request.getMaximumStockLevel(),
                request.getReorderPoint(),
                request.getReorderQuantity()
        );
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}/quantity")
    @Operation(summary = "Update quantity", description = "Update inventory quantity")
    public ResponseEntity<Inventory> updateQuantity(@PathVariable final Long id,
                                                   @RequestBody final UpdateQuantityRequest request) {
        final var inventory = inventoryService.updateQuantity(id, request.getQuantity());
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}/add-quantity")
    @Operation(summary = "Add quantity", description = "Add quantity to inventory")
    public ResponseEntity<Inventory> addQuantity(@PathVariable final Long id,
                                               @RequestBody final AddQuantityRequest request) {
        final var inventory = inventoryService.addQuantity(id, request.getQuantity());
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}/remove-quantity")
    @Operation(summary = "Remove quantity", description = "Remove quantity from inventory")
    public ResponseEntity<Inventory> removeQuantity(@PathVariable final Long id,
                                                   @RequestBody final RemoveQuantityRequest request) {
        final var inventory = inventoryService.removeQuantity(id, request.getQuantity());
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate inventory", description = "Activate an inventory entry")
    public ResponseEntity<Inventory> activateInventory(@PathVariable final Long id) {
        final var inventory = inventoryService.activateInventory(id);
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate inventory", description = "Deactivate an inventory entry")
    public ResponseEntity<Inventory> deactivateInventory(@PathVariable final Long id) {
        final var inventory = inventoryService.deactivateInventory(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock inventories", description = "Retrieve inventories below minimum stock level with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Inventory>>> getLowStockInventories(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var inventories = inventoryService.findLowStockInventories();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, inventories.size());

        final var paginatedInventories = inventories.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedInventories, pageNumber, pageSize, inventories.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Low stock inventories retrieved successfully")
        );
    }

    @GetMapping("/reorder-point")
    @Operation(summary = "Get inventories at reorder point", description = "Retrieve inventories at or below reorder point with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Inventory>>> getInventoriesAtReorderPoint(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var inventories = inventoryService.findInventoriesAtReorderPoint();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, inventories.size());

        final var paginatedInventories = inventories.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedInventories, pageNumber, pageSize, inventories.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Inventories at reorder point retrieved successfully")
        );
    }

    @GetMapping("/out-of-stock")
    @Operation(summary = "Get out of stock inventories", description = "Retrieve inventories that are out of stock with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Inventory>>> getOutOfStockInventories(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var inventories = inventoryService.findOutOfStockInventories();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, inventories.size());

        final var paginatedInventories = inventories.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedInventories, pageNumber, pageSize, inventories.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Out of stock inventories retrieved successfully")
        );
    }

    @GetMapping("/overstocked")
    @Operation(summary = "Get overstocked inventories", description = "Retrieve inventories above maximum stock level with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Inventory>>> getOverstockedInventories(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var inventories = inventoryService.findOverstockedInventories();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, inventories.size());

        final var paginatedInventories = inventories.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedInventories, pageNumber, pageSize, inventories.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Overstocked inventories retrieved successfully")
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory", description = "Delete an inventory entry")
    public ResponseEntity<Void> deleteInventory(@PathVariable final Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Get inventory count", description = "Get total inventory count")
    public ResponseEntity<Long> getInventoryCount() {
        final var count = inventoryService.getTotalInventoryCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/active")
    @Operation(summary = "Get active inventory count", description = "Get active inventory count")
    public ResponseEntity<Long> getActiveInventoryCount() {
        final var count = inventoryService.getActiveInventoryCount();
        return ResponseEntity.ok(count);
    }
}
