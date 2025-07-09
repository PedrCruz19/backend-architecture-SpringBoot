package org.pedrcruz.backendarch.core.usermanagement.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.Role;

import java.util.Set;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    @Email(message = "Username must be a valid email address")
    @Size(max = 255, message = "Username must not exceed 255 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&].*$",
             message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String rePassword;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 255, message = "Full name must be between 2 and 255 characters")
    private String fullName;

    @NotNull(message = "Authorities are required")
    @NotEmpty(message = "User must have at least one authority")
    private Set<Role> authorities;
}
