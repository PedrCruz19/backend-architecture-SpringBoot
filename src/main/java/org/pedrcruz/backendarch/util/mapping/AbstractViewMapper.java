package org.pedrcruz.backendarch.util.mapping;

import java.util.Optional;

import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.money.domain.model.Money;

/**
 *
 * @author Paulo Gandra Sousa 17/07/2023.
 */

/**
 * This interface provides default methods for mapping domain model objects
 * to their string representations.
 * It is intended to be used in view mappers
 * to ensure consistent formatting of common domain types
 * such as Designation, Description, and Money.
 *
 */
public interface AbstractViewMapper {

	default String map(final Designation name) {
		return name.toString();
	}

	default String map(final Description desc) {
		return desc.toString();
	}

	default String map(final Optional<Description> maybeDesc) {
		return maybeDesc.map(Description::toString).orElse(null);
	}

	default String map(final Money p) {
		return p.toSimpleString();
	}
}
