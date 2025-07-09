package org.pedrcruz.backendarch.core.categorymanagement.api;

import org.springframework.stereotype.Component;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryViewMapper {

    public CategoryView toCategoryView(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryView.builder()
                .id(null) // ID is private, we'll ignore it for now
                .name(category.getName() != null ? category.getName().getWord() : null)
                .description(category.getDescription() != null ? category.getDescription().getWord() : null)
                .parentCategoryId(null) //    Parent ID not accessible
                .parentCategoryName(category.getParentCategory() != null && category.getParentCategory().getName() != null
                    ? category.getParentCategory().getName().getWord() : null)
                .active(category.isActive())
                .registrationDate(category.getRegistrationDate() != null ? category.getRegistrationDate().toString() : null)
                .lastActivityChangeDate(category.getLastActivityChangeDate() != null ? category.getLastActivityChangeDate().toString() : null)
                .build();
    }

    public List<CategoryView> toCategoryView(List<Category> categories) {
        if (categories == null) {
            return null;
        }

        return categories.stream()
                .map(this::toCategoryView)
                .collect(Collectors.toList());
    }
}
