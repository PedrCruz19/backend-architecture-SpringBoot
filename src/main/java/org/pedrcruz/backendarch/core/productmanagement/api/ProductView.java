package org.pedrcruz.backendarch.core.productmanagement.api;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductView {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private String categoryName;
    private boolean active;
    private LocalDateTime registrationDate;
    private LocalDateTime lastActivityChangeDate;
    private String imageUrl;
    private int stockQuantity;
}
