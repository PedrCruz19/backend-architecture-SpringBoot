package org.pedrcruz.backendarch.core.domain;

import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.Value;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * A PhoneNumber is a number that is used to identify a person or an entity.
 * It contains the number itself and it is used to contact the person or entity.
 * It is a value object and it is immutable.
 */
@Embeddable
@Value
@Accessors(fluent = true)
public class PhoneNumber implements ValueObject, Comparable<PhoneNumber>{
    /**
     * The serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * If the number is not set, it will be set to this value.
     */
    private static final int NO_INFO = 0;
    /**
     * The number itself
     */
    int number;

    /**
     * The constructor of the PhoneNumber class. It sets the word to the given value.
     * @param number the number to be set
     */
    public PhoneNumber(int number) {
        if (!isValid(number)) throw new IllegalArgumentException("Number is not valid");
        this.number = number;
    }

    /**
     * The default constructor of the Word class. It sets the word to NO_INFO.
     */
    public PhoneNumber() {
        number = NO_INFO;
    }

    /**
     * String representation of the number.
     * @return the number itself
     */
    @Override
    public String toString() {
        return String.valueOf(number);
    }


    /**
     * Compares this number to another number. It returns a negative integer, zero, or a positive integer as this number is less than, equal to, or greater than the specified number.
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this number is less than, equal to, or greater than the specified number.
     */
    @Override
    public int compareTo(PhoneNumber o) {
        return Integer.compare(this.number, o.number);
    }

    public boolean sameAs(PhoneNumber phoneNumber) {
        if (this == phoneNumber) return true;
        if (phoneNumber == null) return false;
        return this.number == phoneNumber.number;
    }

    /**
     * Compares this number to another object. It returns true if they are the same, false otherwise.
     * @param o  the object to be compared
     * @return true if they are the same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return number == that.number;
    }

    /**
     * Compares this number to another integer. It returns true if they are the same, false otherwise.
     * @param number the integer to be compared
     * @return true if they are the same, false otherwise
     */
    public boolean sameAsInt(int number) {
        return this.number == number;
    }

    /**
     * Checks if the number is valid. It returns true if the number is valid, false otherwise.
     * @param number the number to be checked
     * @return true if the number is valid, false otherwise
     */
    private boolean isValid(int number) {
        return number > 0;
    }
}