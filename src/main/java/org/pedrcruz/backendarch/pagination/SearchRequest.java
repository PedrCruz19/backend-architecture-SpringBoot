package org.pedrcruz.backendarch.pagination;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic request wrapper for search operations with pagination
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchRequest<T> {
	@Valid
	@NotNull
	Page page;

	@Valid
	@NotNull
	T query;
}
