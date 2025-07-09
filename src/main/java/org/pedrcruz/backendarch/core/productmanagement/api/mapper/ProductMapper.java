package org.pedrcruz.backendarch.core.productmanagement.api.mapper;

import org.pedrcruz.backendarch.core.categorymanagement.domain.repositories.CategoryRepository;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.core.inventorymanagement.application.InventoryService;
import org.pedrcruz.backendarch.core.productmanagement.api.dto.CreateProductRequest;
import org.pedrcruz.backendarch.core.productmanagement.api.dto.ProductResponse;
import org.pedrcruz.backendarch.core.productmanagement.api.dto.UpdateProductRequest;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductMapper {

    private final CategoryRepository categoryRepository;
    private final InventoryService inventoryService;

    public ProductMapper(CategoryRepository categoryRepository, InventoryService inventoryService) {
        this.categoryRepository = categoryRepository;
        this.inventoryService = inventoryService;
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        var builder = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName().getWord())
                .description(product.getDescription().getWord())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .stockQuantity(product.getStockQuantity())
                .active(product.isActive())
                .inStock(product.getStockQuantity() > 0)
                .lowStock(product.getStockQuantity() < 10) // Define low stock threshold
                .registrationDate(convertToLocalDateTime(product.getRegistrationDate()))
                .lastActivityChangeDate(convertToLocalDateTime(product.getLastActivityChangeDate()));

        // Add category information
        if (product.getCategory() != null) {
            builder.categoryId(product.getCategory().getId())
                   .categoryName(product.getCategory().getName().getWord());
        }

        // Add inventory information if available
        try {
            var inventory = inventoryService.findByProduct(product);
            if (inventory.isPresent()) {
                var inv = inventory.get();
                builder.inventoryQuantity(inv.getCurrentQuantity())
                       .minimumStockLevel(inv.getMinimumStockLevel())
                       .needsReorder(inv.isAtReorderPoint());
            }
        } catch (Exception e) {
            // Log error but don't fail the mapping
        }

        return builder.build();
    }

    public Product toEntity(CreateProductRequest request) {
        if (request == null) {
            return null;
        }

        var category = categoryRepository.getById(request.getCategoryId());

        return new Product(
                new Word(request.getName()),
                new Word(request.getDescription()),
                request.getPrice(),
                category,
                request.getImageUrl(),
                request.getStockQuantity()
        );
    }

    public void updateEntity(UpdateProductRequest request, Product product) {
        if (request == null || product == null) {
            return;
        }

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            product.changeName(new Word(request.getName()));
        }

        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            product.changeDescription(new Word(request.getDescription()));
        }

        if (request.getPrice() != null) {
            product.changePrice(request.getPrice());
        }

        if (request.getCategoryId() != null) {
            var category = categoryRepository.getById(request.getCategoryId());
            product.changeCategory(category);
        }

        if (request.getImageUrl() != null) {
            product.changeImageUrl(request.getImageUrl());
        }

        if (request.getStockQuantity() != null) {
            product.updateStockQuantity(request.getStockQuantity());
        }
    }

    private LocalDateTime convertToLocalDateTime(org.pedrcruz.backendarch.core.domain.Date date) {
        if (date == null || date.date() == null) {
            return null;
        }
        return date.date().atStartOfDay();
    }
}
