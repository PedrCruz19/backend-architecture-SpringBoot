package org.pedrcruz.backendarch.core.inventorymanagement.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateInventoryRequest {

    @NotNull
    private Long productId;

    @Min(0)
    private int currentQuantity;

    @Min(0)
    private int minimumStockLevel;

    @Min(0)
    private int maximumStockLevel;

    @Min(0)
    private int reorderPoint;

    @Min(0)
    private int reorderQuantity;
}
