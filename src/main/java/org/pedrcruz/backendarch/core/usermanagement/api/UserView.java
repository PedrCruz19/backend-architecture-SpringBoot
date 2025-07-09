package org.pedrcruz.backendarch.core.usermanagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {

	private String id;

	private String username;
	private String fullName;
	private Long version;
}
