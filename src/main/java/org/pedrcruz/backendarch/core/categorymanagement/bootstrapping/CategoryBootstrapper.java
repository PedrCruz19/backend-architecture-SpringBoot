package org.pedrcruz.backendarch.core.categorymanagement.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.core.categorymanagement.domain.repositories.CategoryRepository;

import org.pedrcruz.backendarch.core.domain.Word;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
@Order(11)
public class CategoryBootstrapper implements CommandLineRunner {

	private final CategoryRepository categoryRepository;


	@Override
	@Transactional
	public void run(final String... args) throws Exception {
		// Check if categories already exist to avoid duplicates
		if (categoryRepository.findByName(new Word("Beverages")).isEmpty()) {
			// Create root categories
			final var drinks = new Category(new Word("Beverages"), new Word("All_kinds_of_beverages"));
			categoryRepository.save(drinks);

			final var food = new Category(new Word("FoodItems"), new Word("Delicious_meals_and_snacks"));
			categoryRepository.save(food);

			// Create subcategories for Beverages
			final var hotDrinks = new Category(new Word("HotBeverages"), new Word("Warm_and_comforting_drinks"), drinks);
			categoryRepository.save(hotDrinks);

			final var coldDrinks = new Category(new Word("ColdBeverages"), new Word("Refreshing_and_cool_drinks"), drinks);
			categoryRepository.save(coldDrinks);

			// Create subcategories for Food Items
			final var sandwiches = new Category(new Word("Sandwiches"), new Word("Freshly_made_sandwiches"), food);
			categoryRepository.save(sandwiches);

			final var salads = new Category(new Word("Salads"), new Word("Healthy_and_green_salads"), food);
			categoryRepository.save(salads);


			System.out.println("i: Bootstrap categories created successfully!");
		} else {
			System.out.println("i:  Bootstrap categories already exist, skipping creation.");
		}
	}
}
