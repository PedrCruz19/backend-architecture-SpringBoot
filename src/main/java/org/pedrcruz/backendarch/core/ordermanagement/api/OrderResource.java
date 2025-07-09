package org.pedrcruz.backendarch.core.ordermanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.api.dto.PagedResponse;
import org.pedrcruz.backendarch.core.ordermanagement.application.OrderService;
import org.pedrcruz.backendarch.core.ordermanagement.domain.model.Order;
import org.pedrcruz.backendarch.core.ordermanagement.domain.model.OrderStatus;
import org.pedrcruz.backendarch.pagination.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "API for managing orders")
public class OrderResource {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create order", description = "Create a new order for a customer")
    public ResponseEntity<Order> createOrder(@RequestBody final CreateOrderRequest request) {
        final var order = orderService.createOrder(request.getCustomerId(), request.getNotes());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve all orders with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getAllOrders(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findAll();

        // Apply pagination logic
        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Orders retrieved successfully")
        );
    }

    @GetMapping("/active")
    @Operation(summary = "Get active orders", description = "Retrieve all active orders with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getActiveOrders(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findActiveOrders();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Active orders retrieved successfully")
        );
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieve orders by status with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getOrdersByStatus(
            @PathVariable final OrderStatus status,
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findByStatus(status);

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Orders by status retrieved successfully")
        );
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get orders by customer", description = "Retrieve orders by customer ID with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getOrdersByCustomer(
            @PathVariable final Long customerId,
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findByCustomerId(customerId);

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Customer orders retrieved successfully")
        );
    }

    @GetMapping("/customer/{customerId}/status/{status}")
    @Operation(summary = "Get customer orders by status", description = "Retrieve customer orders by status with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getCustomerOrdersByStatus(
            @PathVariable final Long customerId,
            @PathVariable final OrderStatus status,
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findByCustomerIdAndStatus(customerId, status);

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Customer orders by status retrieved successfully")
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve order by ID")
    public ResponseEntity<Order> getOrderById(@PathVariable final Long id) {
        final var order = orderService.findById(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Add item to order", description = "Add a product item to an order")
    public ResponseEntity<Order> addItemToOrder(@PathVariable final Long id,
                                              @RequestBody final AddItemRequest request) {
        final var order = orderService.addItemToOrder(id, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    @Operation(summary = "Remove item from order", description = "Remove an item from an order")
    public ResponseEntity<Order> removeItemFromOrder(@PathVariable final Long id,
                                                    @PathVariable final Long itemId) {
        final var order = orderService.removeItemFromOrder(id, itemId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/items/{itemId}")
    @Operation(summary = "Update item quantity", description = "Update the quantity of an item in an order")
    public ResponseEntity<Order> updateItemQuantity(@PathVariable final Long id,
                                                   @PathVariable final Long itemId,
                                                   @RequestBody final UpdateItemQuantityRequest request) {
        final var order = orderService.updateItemQuantity(id, itemId, request.getQuantity());
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/confirm")
    @Operation(summary = "Confirm order", description = "Confirm a pending order")
    public ResponseEntity<Order> confirmOrder(@PathVariable final Long id) {
        final var order = orderService.confirmOrder(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/start-preparing")
    @Operation(summary = "Start preparing order", description = "Start preparing a confirmed order")
    public ResponseEntity<Order> startPreparingOrder(@PathVariable final Long id) {
        final var order = orderService.startPreparingOrder(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/ready")
    @Operation(summary = "Mark order as ready", description = "Mark a preparing order as ready")
    public ResponseEntity<Order> markOrderAsReady(@PathVariable final Long id) {
        final var order = orderService.markOrderAsReady(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/deliver")
    @Operation(summary = "Deliver order", description = "Mark a ready order as delivered")
    public ResponseEntity<Order> deliverOrder(@PathVariable final Long id) {
        final var order = orderService.deliverOrder(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel an order")
    public ResponseEntity<Order> cancelOrder(@PathVariable final Long id) {
        final var order = orderService.cancelOrder(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/notes")
    @Operation(summary = "Update order notes", description = "Update the notes for an order")
    public ResponseEntity<Order> updateOrderNotes(@PathVariable final Long id,
                                                 @RequestBody final UpdateNotesRequest request) {
        final var order = orderService.updateOrderNotes(id, request.getNotes());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending orders", description = "Retrieve all pending orders with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getPendingOrders(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findPendingOrders();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Pending orders retrieved successfully")
        );
    }

    @GetMapping("/confirmed")
    @Operation(summary = "Get confirmed orders", description = "Retrieve all confirmed orders with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getConfirmedOrders(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findConfirmedOrders();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Confirmed orders retrieved successfully")
        );
    }

    @GetMapping("/preparing")
    @Operation(summary = "Get preparing orders", description = "Retrieve all orders being prepared with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getPreparingOrders(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findPreparingOrders();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Preparing orders retrieved successfully")
        );
    }

    @GetMapping("/ready")
    @Operation(summary = "Get ready orders", description = "Retrieve all ready orders with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getReadyOrders(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findReadyOrders();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Ready orders retrieved successfully")
        );
    }

    @GetMapping("/delivered")
    @Operation(summary = "Get delivered orders", description = "Retrieve all delivered orders with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getDeliveredOrders(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findDeliveredOrders();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Delivered orders retrieved successfully")
        );
    }

    @GetMapping("/cancelled")
    @Operation(summary = "Get cancelled orders", description = "Retrieve all cancelled orders with pagination")
    public ResponseEntity<org.pedrcruz.backendarch.api.dto.ApiResponse<PagedResponse<Order>>> getCancelledOrders(
            @Parameter(description = "Pagination parameters") final Page page) {
        final var orders = orderService.findCancelledOrders();

        final int pageNumber = page != null ? page.getNumber() : 0;
        final int pageSize = page != null ? page.getLimit() : 20;
        final int startIndex = pageNumber * pageSize;
        final int endIndex = Math.min(startIndex + pageSize, orders.size());

        final var paginatedOrders = orders.subList(startIndex, endIndex);
        final var pagedResponse = PagedResponse.of(paginatedOrders, pageNumber, pageSize, orders.size());

        return ResponseEntity.ok(
            org.pedrcruz.backendarch.api.dto.ApiResponse.success(pagedResponse, "Cancelled orders retrieved successfully")
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Delete an order")
    public ResponseEntity<Void> deleteOrder(@PathVariable final Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Get order count", description = "Get total order count")
    public ResponseEntity<Long> getOrderCount() {
        final var count = orderService.getTotalOrderCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/active")
    @Operation(summary = "Get active order count", description = "Get active order count")
    public ResponseEntity<Long> getActiveOrderCount() {
        final var count = orderService.getActiveOrderCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Get order count by status", description = "Get order count by status")
    public ResponseEntity<Long> getOrderCountByStatus(@PathVariable final OrderStatus status) {
        final var count = orderService.getOrderCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/revenue/total")
    @Operation(summary = "Get total revenue", description = "Get total revenue from delivered orders")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        final var revenue = orderService.getTotalRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/status/{status}")
    @Operation(summary = "Get revenue by status", description = "Get revenue by order status")
    public ResponseEntity<BigDecimal> getRevenueByStatus(@PathVariable final OrderStatus status) {
        final var revenue = orderService.getRevenueByStatus(status);
        return ResponseEntity.ok(revenue);
    }
}
