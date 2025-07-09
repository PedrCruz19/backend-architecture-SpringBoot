package org.pedrcruz.backendarch.core.productmanagement.bootstrapping;


import lombok.RequiredArgsConstructor;

import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.core.categorymanagement.domain.repositories.CategoryRepository;
import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.core.productmanagement.domain.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Spring will load and execute all components that implement the interface
 * CommandLineRunner on startup, so we will use that as a way to bootstrap some
 * data for testing purposes.
 * <p>
 * To enable this bootstrapping make sure you activate the spring
 * profile "bootstrap" in application.properties
 *
 * @author Pedro Cruz
 *
 */
@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@Order(12) // Execute after CategoryBootstrapper (which has Order 11)
public class ProductBootstrapper implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        try {
            // Print the count of existing products
            List<Product> existingProducts = productRepository.findByActivityStatus(new ActivityStatus(true));
            System.out.println("i: Found " + existingProducts.size() + " existing active products in the database");

            // Check if we have enough categories to continue
            List<Category> allCategories = categoryRepository.findByActivityStatus(new ActivityStatus(true));
            if (allCategories.isEmpty()) {
                System.out.println("No active categories found. Cannot create products without categories.");
                return;
            }

            System.out.println("Found " + allCategories.size() + " active categories. Proceeding with product creation.");

            // Add some products if we have fewer than 5
            if (existingProducts.size() < 5) {
                createHotBeverages();
                createColdBeverages();
                createSandwiches();
                createSalads();

                // Print the results
                System.out.println("i: Bootstrap products created successfully!");
            } else {
                System.out.println("i: Sufficient products already exist, skipping bootstrap creation.");
            }

            // Verify all products and their categories
            verifyProductCategories();

        } catch (Exception e) {
            System.err.println("Error during product bootstrapping: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void verifyProductCategories() {
        List<Product> products = productRepository.findByActivityStatus(new ActivityStatus(true));

        int withCategory = 0;
        int withoutCategory = 0;


        for (Product product : products) {
            if (product.getCategory() != null) {
                withCategory++;

            } else {
                withoutCategory++;

            }
        }

    }

    private void createHotBeverages() {
        final List<Category> hotBeveragesCategory = categoryRepository.findByName(new Word("HotBeverages"));

        if (!hotBeveragesCategory.isEmpty()) {
            final Category hotBeverages = hotBeveragesCategory.get(0);

            // Create hot beverages if they don't exist
            createProductIfNotExists(
                "Espresso",
                "Strong_black_coffee_brewed_by_forcing_hot_water_through_finely_ground_coffee_beans",
                new BigDecimal("1.50"),
                hotBeverages,
                "/images/sardinha.jpg",
                100
            );

            createProductIfNotExists(
                "Cappuccino",
                "Coffee_with_steamed_milk_foam",
                new BigDecimal("2.50"),
                hotBeverages,
                "/images/sardinha.jpg",
                75
            );

            createProductIfNotExists(
                "HotChocolate",
                "Warm_chocolate_beverage_with_milk_and_sugar",
                new BigDecimal("2.25"),
                hotBeverages,
                "/images/sardinha.jpg",
                50
            );
        }
    }

    private void createColdBeverages() {
        final List<Category> coldBeveragesCategory = categoryRepository.findByName(new Word("ColdBeverages"));

        if (!coldBeveragesCategory.isEmpty()) {
            final Category coldBeverages = coldBeveragesCategory.get(0);

            // Create cold beverages if they don't exist
            createProductIfNotExists(
                "IcedCoffee",
                "Chilled_coffee_served_with_ice_and_optional_milk",
                new BigDecimal("2.75"),
                coldBeverages,
                "/images/sardinha.jpg",
                60
            );

            createProductIfNotExists(
                "Lemonade",
                "Refreshing_citrus_drink_made_with_lemons_and_sugar",
                new BigDecimal("2.00"),
                coldBeverages,
                "/images/sardinha.jpg",
                80
            );

            createProductIfNotExists(
                "OrangeJuice",
                "Freshly_squeezed_orange_juice",
                new BigDecimal("2.50"),
                coldBeverages,
                "/images/sardinha.jpg",
                45
            );
        }
    }

    private void createSandwiches() {
        final List<Category> sandwichesCategory = categoryRepository.findByName(new Word("Sandwiches"));

        if (!sandwichesCategory.isEmpty()) {
            final Category sandwiches = sandwichesCategory.get(0);

            // Create sandwiches if they don't exist
            createProductIfNotExists(
                "HamAndCheese",
                "Classic_sandwich_with_ham_and_cheese",
                new BigDecimal("4.50"),
                sandwiches,
                "/images/sardinha.jpg",
                30
            );

            createProductIfNotExists(
                "TunaSalad",
                "Tuna_with_mayo_lettuce_and_tomato_on_whole_grain_bread",
                new BigDecimal("5.25"),
                sandwiches,
                "/images/sardinha.jpg",
                25
            );

            createProductIfNotExists(
                "VeggieBagel",
                "Cream_cheese_cucumber_tomato_and_sprouts_on_a_bagel",
                new BigDecimal("4.75"),
                sandwiches,
                "/images/sardinha.jpg",
                20
            );
        }
    }

    private void createSalads() {
        final List<Category> saladsCategory = categoryRepository.findByName(new Word("Salads"));

        if (!saladsCategory.isEmpty()) {
            final Category salads = saladsCategory.get(0);

            // Create salads if they don't exist
            createProductIfNotExists(
                "CaesarSalad",
                "Crisp_romaine_lettuce_croutons_parmesan_and_caesar_dressing",
                new BigDecimal("6.50"),
                salads,
                "/images/sardinha.jpg",
                15
            );

            createProductIfNotExists(
                "GreekSalad",
                "Tomatoes_cucumber_olives_feta_cheese_and_olive_oil",
                new BigDecimal("6.25"),
                salads,
                "/images/sardinha.jpg",
                18
            );

            createProductIfNotExists(
                "FruitSalad",
                "Mix_of_seasonal_fresh_fruits",
                new BigDecimal("5.75"),
                salads,
                "/images/sardinha.jpg",
                22
            );
        }
    }

    private void createProductIfNotExists(String name, String description, BigDecimal price,
                                         Category category, String imageUrl, int stockQuantity) {
        try {
            // Explicitly check if a product with this name exists using findByName
            List<Product> existingProducts = productRepository.findByName(new Word(name));

            if (existingProducts.isEmpty()) {
                // Ensure we have a fresh reference to the category
                Category freshCategory = categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new IllegalStateException("Category not found: " + category.getName().getWord()));

                final Product product = new Product(
                    new Word(name),
                    new Word(description),
                    price,
                    freshCategory, // Use the fresh reference
                    imageUrl,
                    stockQuantity
                );

                // Save the product
                Product savedProduct = productRepository.save(product);
                System.out.println("i: Created product: " + name + " with category: " +
                                   freshCategory.getName().getWord() + " (ID: " + freshCategory.identity() + ")");
            } else {
                System.out.println("i: Product '" + name + "' already exists with ID: " + existingProducts.get(0).getId() + ", skipping.");
            }
        } catch (Exception e) {
            System.err.println("Error creating product '" + name + "': " + e.getMessage());
        }
    }
}
