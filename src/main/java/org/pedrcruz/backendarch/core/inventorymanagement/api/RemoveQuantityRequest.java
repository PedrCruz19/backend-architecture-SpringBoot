package org.pedrcruz.backendarch.core.inventorymanagement.api;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RemoveQuantityRequest {

    @Min(1)
    private int quantity;
}
