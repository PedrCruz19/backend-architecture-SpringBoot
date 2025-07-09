package org.pedrcruz.backendarch.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Generic response wrapper for collections
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 */
@Data
@AllArgsConstructor
public class ListResponse<T> {
	private List<T> items;
}