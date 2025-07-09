package org.pedrcruz.backendarch.core.usermanagement.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.Role;

import java.util.Set;

@Data
public class UpdateUserRequest {

    @Size(min = 2, max = 255, message = "Full name must be between 2 and 255 characters")
    private String fullName;

    @NotEmpty(message = "User must have at least one authority")
    private Set<Role> authorities;

    private Boolean enabled;
}
