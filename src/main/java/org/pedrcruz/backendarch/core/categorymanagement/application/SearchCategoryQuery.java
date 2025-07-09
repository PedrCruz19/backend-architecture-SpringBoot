package org.pedrcruz.backendarch.core.categorymanagement.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query object for searching categories with various filter criteria
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCategoryQuery {
	private String name;
	private Boolean active;
	private Long parentId;
}