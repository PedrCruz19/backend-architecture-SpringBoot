package org.pedrcruz.backendarch.core.productmanagement.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pedrcruz.backendarch.pagination.SearchRequest;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductQuery {
    private String name;
    private Long categoryId;
    private Boolean active;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
}
