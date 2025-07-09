package org.pedrcruz.backendarch.core.categorymanagement.api.mapper;

import org.pedrcruz.backendarch.core.categorymanagement.api.dto.EditCategoryRequest;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.util.api.AbstractResource;
import org.pedrcruz.backendarch.util.mapping.AbstractMapper;

abstract class EditCategoryMapper implements AbstractMapper {
    /**
     * Converts an EditCategoryRequest to a Category.
     *
     * @param request the EditCategoryRequest to convert
     * @return the converted Category
     */
    public abstract Category toCategory(EditCategoryRequest request);

    /**
     * Converts a Category to an EditCategoryRequest.
     *
     * @param category the Category to convert
     * @return the converted EditCategoryRequest
     */
    public abstract EditCategoryRequest toEditCategoryRequest(Category category);

    /**
     * Converts an EditCategoryRequest to an AbstractResource.
     *
     * @param request the EditCategoryRequest to convert
     * @return the converted AbstractResource
     */
    public abstract AbstractResource toResource(EditCategoryRequest request);

}
