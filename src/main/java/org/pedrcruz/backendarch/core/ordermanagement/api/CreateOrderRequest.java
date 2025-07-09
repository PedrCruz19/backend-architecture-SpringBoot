package org.pedrcruz.backendarch.core.ordermanagement.api;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotNull
    private Long customerId;

    private String notes;
}
