package org.pedrcruz.backendarch.core.categorymanagement.application;

import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.core.categorymanagement.api.dto.CreateCategoryRequest;
import org.pedrcruz.backendarch.core.categorymanagement.api.dto.UpdateCategoryRequest;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.core.categorymanagement.domain.repositories.CategoryRepository;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public Optional<Category> findCategoryByName(final String name) {
        return repository.findByName(new Word(name)).stream().findFirst();
    }

    @Override
    public Category createCategory(final CreateCategoryRequest request) {
        final var name = new Word(request.getName());
        final var description = new Word(request.getDescription());
        final var category = new Category(name, description);
        return repository.save(category);
    }

    @Override
    public Category createsubCategory(final CreateCategoryRequest request, final Long parentId) {
        final var parent = repository.getById(parentId);
        final var name = new Word(request.getName());
        final var description = new Word(request.getDescription());
        final var category = new Category(name, description, parent);
        return repository.save(category);
    }

    @Override
    public Category editCategory(final Long id, final UpdateCategoryRequest request) {
        final var category = repository.getById(id);
        if (request != null) {
            final var name = new Word(request.getName());
            final var description = new Word(request.getDescription());
            // Update the category fields
            category.changeName(name);
            category.changeDescription(description);
        }
        return repository.save(category);
    }

    @Override
    public List<Category> searchCategories(final SearchCategoryQuery query, final Page page) {
        return repository.searchCategories(page, query);
    }

    @Override
    public Category getById(final Long id) {
        return repository.getById(id);
    }

    @Override
    public Category deleteCategory(final Long id) {
        final var category = repository.getById(id);
        repository.delete(category);
        return category;
    }

    @Override
    public List<Category> findActiveCategories() {
        return repository.findByActivityStatus(new org.pedrcruz.backendarch.core.domain.ActivityStatus(true));
    }
}
