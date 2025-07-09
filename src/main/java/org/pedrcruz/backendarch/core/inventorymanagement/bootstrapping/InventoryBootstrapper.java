package org.pedrcruz.backendarch.core.inventorymanagement.bootstrapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pedrcruz.backendarch.core.inventorymanagement.application.InventoryService;
import org.pedrcruz.backendarch.core.productmanagement.application.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(4) // Run after products are bootstrapped
public class InventoryBootstrapper implements CommandLineRunner {

    private final InventoryService inventoryService;
    private final ProductService productService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting inventory bootstrapping...");

        // Get all active products and create inventory entries
        final var products = productService.findActiveProducts();

        for (final var product : products) {
            if (!inventoryService.existsByProduct(product)) {
                try {
                    // Create inventory with default values
                    final var inventory = inventoryService.createInventory(
                            product,
                            product.getStockQuantity(), // Use product's current stock as initial quantity
                            5,   // Minimum stock level
                            100, // Maximum stock level
                            10,  // Reorder point
                            50   // Reorder quantity
                    );

                    log.info("Created inventory for product: {} with ID: {}",
                            product.getName().getWord(), inventory.getId());
                } catch (Exception e) {
                    log.error("Failed to create inventory for product: {}",
                            product.getName().getWord(), e);
                }
            }
        }

        log.info("Inventory bootstrapping completed.");
    }
}
