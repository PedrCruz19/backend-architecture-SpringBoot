package org.pedrcruz.backendarch.core.productmanagement.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private int stockQuantity;
    private boolean active;
    private boolean inStock;
    private boolean lowStock;

    // Category information
    private Long categoryId;
    private String categoryName;

    // Timestamps
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActivityChangeDate;

    // Inventory information
    private Integer inventoryQuantity;
    private Integer minimumStockLevel;
    private Boolean needsReorder;
}
