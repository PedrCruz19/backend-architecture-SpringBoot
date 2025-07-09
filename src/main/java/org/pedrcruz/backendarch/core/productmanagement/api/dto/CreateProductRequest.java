package org.pedrcruz.backendarch.core.productmanagement.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Product description is required")
    @Size(min = 10, max = 500, message = "Product description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999.99", message = "Price must be less than 1000")
    @Digits(integer = 3, fraction = 2, message = "Price must have at most 3 integer digits and 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    @Pattern(regexp = "^(https?://.*\\.(jpg|jpeg|png|gif|webp))?$",
             message = "Image URL must be a valid HTTP/HTTPS URL ending with jpg, jpeg, png, gif, or webp")
    private String imageUrl;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Max(value = 9999, message = "Stock quantity cannot exceed 9999")
    private int stockQuantity = 0;
}
