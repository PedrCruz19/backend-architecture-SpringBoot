package org.pedrcruz.backendarch.core.ordermanagement.bootstrapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pedrcruz.backendarch.core.ordermanagement.application.OrderService;
import org.pedrcruz.backendarch.core.productmanagement.application.ProductService;
import org.pedrcruz.backendarch.core.usermanagement.application.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(5) // Run after inventory is bootstrapped
public class OrderBootstrapper implements CommandLineRunner {

    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting order bootstrapping...");

        try {
            // Get all users and products to create sample orders
            final var users = userService.searchUsers(null, null);
            final var products = productService.findActiveProducts();

            if (users.isEmpty() || products.isEmpty()) {
                log.warn("No users or products found. Skipping order bootstrapping.");
                return;
            }

            // Create sample orders for demonstration
            for (int i = 0; i < Math.min(users.size(), 3); i++) {
                final var user = users.get(i);

                // Create a sample order
                final var order = orderService.createOrder(
                        user.getId(),
                        "Sample order " + (i + 1) + " for demonstration"
                );

                // Add some items to the order
                if (products.size() > 0) {
                    orderService.addItemToOrder(order.getId(), products.get(0).getId(), 2);
                }
                if (products.size() > 1) {
                    orderService.addItemToOrder(order.getId(), products.get(1).getId(), 1);
                }

                // Confirm the order
                orderService.confirmOrder(order.getId());

                log.info("Created sample order with ID: {} for user: {}",
                        order.getId(), user.getUsername());
            }

            log.info("Order bootstrapping completed successfully.");

        } catch (Exception e) {
            log.error("Error during order bootstrapping", e);
        }
    }
}
