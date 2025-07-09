package org.pedrcruz.backendarch.util.mapping;

import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.money.domain.model.Money;

/**
 *
 * @author Paulo Gandra Sousa 17/07/2023.
 *
 */
public interface AbstractMapper {

	default Money toMoney(final String price) {
		return Money.valueOf(price);
	}

	default Designation toDesignation(final String desc) {
		return Designation.valueOf(desc);
	}

	default Description toDescription(final String desc) {
		if (desc != null) {
			return Description.valueOf(desc);
		}
		return null;
	}
}
