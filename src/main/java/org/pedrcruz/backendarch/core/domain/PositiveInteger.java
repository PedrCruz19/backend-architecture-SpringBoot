package org.pedrcruz.backendarch.core.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.Value;
import lombok.experimental.Accessors;

@Embeddable
@Value
@Accessors(fluent = true)
public class PositiveInteger implements ValueObject {

    private static final long serialVersionUID = 1L;

    private final int number;

    /**
     * Constructor for PositiveInteger.
     *
     * @param number the number
     */
    public PositiveInteger(final int number) {
        Preconditions.ensure(number >= 0, "This number must be positive");
        this.number = number;
    }

    /**
     * Only for ORM.
     */
    public PositiveInteger() {
        this.number = 0;
    }

    /**
     * Gets the integer value.
     *
     * @return the integer value
     */
    public int value() {
        return this.number;
    }

    /**
     * Builds the string representation of the number.
     *
     * @return the string representation of the number.
     */
    @Override
    public String toString() {
        return String.format("%d", number);
    }

}