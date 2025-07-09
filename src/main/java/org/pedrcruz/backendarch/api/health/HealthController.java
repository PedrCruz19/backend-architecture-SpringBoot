package org.pedrcruz.backendarch.api.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.api.dto.ApiResponse;
import org.pedrcruz.backendarch.core.categorymanagement.application.CategoryService;
import org.pedrcruz.backendarch.core.productmanagement.application.ProductService;
import org.pedrcruz.backendarch.core.ordermanagement.application.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Health Check", description = "System health and diagnostics")
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;

    @Operation(summary = "Health check", description = "Check if the system is running properly")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("version", "1.0.0");

        return ResponseEntity.ok(
            ApiResponse.success(health, "System is healthy")
        );
    }

    @Operation(summary = "System statistics", description = "Get system statistics and metrics")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Categories stats
            long totalCategories = categoryService.findActiveCategories().size();
            stats.put("totalCategories", totalCategories);

            // Products stats
            long totalProducts = productService.findActiveProducts().size();
            stats.put("totalProducts", totalProducts);

            // Orders stats
            long totalOrders = orderService.getTotalOrderCount();
            long activeOrders = orderService.getActiveOrderCount();
            stats.put("totalOrders", totalOrders);
            stats.put("activeOrders", activeOrders);

            stats.put("timestamp", LocalDateTime.now());
            stats.put("status", "operational");

        } catch (Exception e) {
            stats.put("status", "degraded");
            stats.put("error", "Some services are unavailable");
        }

        return ResponseEntity.ok(
            ApiResponse.success(stats, "Statistics retrieved successfully")
        );
    }
}
