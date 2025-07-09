package org.pedrcruz.backendarch.core.categorymanagement.application;

import org.pedrcruz.backendarch.core.categorymanagement.api.dto.CreateCategoryRequest;
import org.pedrcruz.backendarch.core.categorymanagement.api.dto.UpdateCategoryRequest;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.pagination.Page;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<Category> findCategoryByName(String name);

    Category createCategory(CreateCategoryRequest name);

    Category createsubCategory(CreateCategoryRequest request, Long parentId);

    Category editCategory(Long id, UpdateCategoryRequest request);

    List<Category> searchCategories(SearchCategoryQuery query, Page page);

    Category getById(Long id);

    Category deleteCategory(Long id);

    List<Category> findActiveCategories();

}
