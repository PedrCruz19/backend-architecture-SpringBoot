package org.pedrcruz.backendarch.core.categorymanagement.api.mapper;

import org.pedrcruz.backendarch.core.categorymanagement.api.dto.CategoryResponse;
import org.pedrcruz.backendarch.core.categorymanagement.api.dto.CreateCategoryRequest;
import org.pedrcruz.backendarch.core.categorymanagement.api.dto.UpdateCategoryRequest;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.core.productmanagement.domain.repositories.ProductRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryMapper {

    private final ProductRepository productRepository;

    public CategoryMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        long productCount = 0;
        try {
            productCount = productRepository.findByCategoryId(category.getId()).size();
        } catch (Exception e) {
            // Log error but don't fail the mapping
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName().getWord())
                .description(category.getDescription().getWord())
                .active(category.isActive())
                .createdDate(convertToLocalDateTime(category.getRegistrationDate()))
                .lastUpdatedDate(convertToLocalDateTime(category.getLastActivityChangeDate()))
                .productCount(productCount)
                .build();
    }

    public Category toEntity(CreateCategoryRequest request) {
        if (request == null) {
            return null;
        }

        return new Category(
                new Word(request.getName()),
                new Word(request.getDescription())
        );
    }

    public void updateEntity(UpdateCategoryRequest request, Category category) {
        if (request == null || category == null) {
            return;
        }

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            category.changeName(new Word(request.getName()));
        }

        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            category.changeDescription(new Word(request.getDescription()));
        }
    }

    private LocalDateTime convertToLocalDateTime(org.pedrcruz.backendarch.core.domain.Date date) {
        if (date == null || date.date() == null) {
            return null;
        }
        return date.date().atStartOfDay();
    }
}
