package org.pedrcruz.backendarch.core.inventorymanagement.api;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateInventoryRequest {

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
