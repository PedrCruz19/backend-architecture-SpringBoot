package org.pedrcruz.backendarch.core.ordermanagement.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemRequest {

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;
}
