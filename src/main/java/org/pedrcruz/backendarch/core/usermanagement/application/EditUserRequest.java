package org.pedrcruz.backendarch.core.usermanagement.application;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequest {
	@NotBlank
	private String fullName;

	private Set<String> authorities;
}
