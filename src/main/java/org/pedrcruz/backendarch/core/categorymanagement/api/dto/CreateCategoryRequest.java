package org.pedrcruz.backendarch.core.categorymanagement.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Category description is required")
    @Size(min = 5, max = 255, message = "Category description must be between 5 and 255 characters")
    private String description;
}
