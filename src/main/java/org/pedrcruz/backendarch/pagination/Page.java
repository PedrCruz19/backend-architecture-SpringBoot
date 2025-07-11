package org.pedrcruz.backendarch.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 *
 */
@AllArgsConstructor
@Data
public class Page {
	@Min(value = 1, message = "Paging must start with page 1")
	int number;

	@Min(value = 1, message = "You can request minimum 1 records")
	@Max(value = 100, message = "You can request maximum 100 records")
	int limit;

	public Page() {
		this(1, 10);
	}
}