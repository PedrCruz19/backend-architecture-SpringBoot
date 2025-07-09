package org.pedrcruz.backendarch.core.inventorymanagement.api;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateQuantityRequest {

    @Min(0)
    private int quantity;
}
