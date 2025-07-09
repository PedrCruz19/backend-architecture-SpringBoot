package org.pedrcruz.backendarch.core.usermanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Value
@AllArgsConstructor
public class Role implements GrantedAuthority {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String authority;

	/*
	 * roles
	 */
	public static final String USER_ADMIN = "USER_ADMIN";

	public static final String CUSTOMER = "CUSTOMER";

	public static final String SUPPLIER = "SUPPLIER";

}
