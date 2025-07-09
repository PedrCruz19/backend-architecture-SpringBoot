package org.pedrcruz.backendarch.core.categorymanagement.domain.repositories;

import org.pedrcruz.backendarch.core.categorymanagement.application.SearchCategoryQuery;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.exceptions.NotFoundException;
import org.pedrcruz.backendarch.pagination.Page;


import java.util.List;
import java.util.Optional;


public interface CategoryRepository {

    <S extends Category> List<S> saveAll(Iterable<S> entities);

    <S extends Category> S save(S entity);

    Optional<Category> findById(Long objectId);

    default Category getById(final Long id) {
        final var maybeCategory = findById(id);
        // throws 404 Not Found if the category does not exist or if it is not active
        return maybeCategory
                .filter(Category::isActive)
                .orElseThrow(() -> new NotFoundException(Category.class, id));
    }

    List<Category> searchCategories(Page page, SearchCategoryQuery query);

    List<Category> findByParentCategoryId(Long parentId);

    List<Category> findByParentCategoryIdAndActivityStatus(Long parentId, ActivityStatus activityStatus);

    List<Category> findByActivityStatus(ActivityStatus activityStatus);

    List<Category> findByName(Word name);

    List<Category> findByNameAndActivityStatus(Word name, ActivityStatus activityStatus);

    //delete category
    void delete(Category category);

}
