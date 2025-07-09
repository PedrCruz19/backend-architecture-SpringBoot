package org.pedrcruz.backendarch.core.productmanagement.application;

import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.core.categorymanagement.domain.repositories.CategoryRepository;
import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.core.productmanagement.api.dto.CreateProductRequest;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.core.productmanagement.domain.repositories.ProductRepository;
import org.pedrcruz.backendarch.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    @Override
    public Optional<Product> findProductByName(final String name) {
        return repository.findByName(new Word(name)).stream().findFirst();
    }

    @Override
    public Product createProduct(final CreateProductRequest request) {
        final var name = new Word(request.getName());
        final var description = new Word(request.getDescription());
        final var category = categoryRepository.getById(request.getCategoryId());

        final var product = new Product(
            name,
            description,
            request.getPrice(),
            category,
            request.getImageUrl(),
            request.getStockQuantity()
        );

        return repository.save(product);
    }

    @Override
    public Product editProduct(final Long id, final CreateProductRequest request) {
        final var product = repository.getById(id);
        if (request != null) {
            final var name = new Word(request.getName());
            final var description = new Word(request.getDescription());

            // Update the product fields
            product.changeName(name);
            product.changeDescription(description);
            product.changePrice(request.getPrice());

            // Update category if it's different
            if (!product.getCategory().getId().equals(request.getCategoryId())) {
                final var category = categoryRepository.getById(request.getCategoryId());
                product.changeCategory(category);
            }

            // Update image URL if provided
            product.changeImageUrl(request.getImageUrl());

            // Update stock quantity
            product.updateStockQuantity(request.getStockQuantity());
        }

        return repository.save(product);
    }

    @Override
    public List<Product> searchProducts(final SearchProductQuery query, final Page page) {
        return repository.searchProducts(page, query);
    }

    @Override
    public Product getById(final Long id) {
        return repository.getById(id);
    }

    @Override
    public Product deleteProduct(final Long id) {
        final var product = repository.getById(id);
        product.deactivate();
        return repository.save(product);
    }

    @Override
    public Product updateStock(final Long id, final int quantity) {
        final var product = repository.getById(id);
        product.updateStockQuantity(quantity);
        return repository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Product> findActiveProducts() {
        return repository.findByActivityStatus(new ActivityStatus(true));
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return repository.findById(id);
    }
}
