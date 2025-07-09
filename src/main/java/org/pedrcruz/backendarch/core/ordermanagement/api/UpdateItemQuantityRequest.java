package org.pedrcruz.backendarch.core.ordermanagement.api;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateItemQuantityRequest {

    @Min(1)
    private int quantity;
}
