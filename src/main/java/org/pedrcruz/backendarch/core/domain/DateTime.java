package org.pedrcruz.backendarch.core.domain;

import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A Date is a value object that represents a date in the format DD-MM-YYYY.
 * It is a general class that can be used to represent any date in the system.
 */
@Embeddable
@Value
@Accessors(fluent = true)
public class DateTime implements ValueObject {
    /**
     * The serial version UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * The date itself. It is a string that contains the date in the format DD-MM-YYYY.
     */
    LocalDateTime date;

    /**
     * The constructor of the Date class. It takes a string as input and checks if it is valid.
     * If the date is blank, it sets the date to the current date.
     * If it is not valid, it throws an IllegalArgumentException.
     * @param date the date to be set
     */
    public DateTime(String date) {
        validateDate(date);
        if (date.isBlank()) this.date = LocalDateTime.now();
        else this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm:ss"));
    }

    /**
     * The constructor of the Date class. It takes a LocalDate as input.
     * @param localDate the date to be set
     */
    public DateTime(LocalDateTime localDate) {
        if (localDate == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = localDate;
    }

    /**
     * Default constructor of the Date class. It sets the date to null.
     */
    public DateTime() {
        date = null;
    }

    /**
     * Method to validate the date format. It checks if the date is null and if it matches the format DD-MM-YYYY.
     * @param date the date to be validated
     */
    private void validateDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("Registration date cannot be null");
        }
        if (date.isBlank()) return;
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}:\\d{2}:\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Registration date must be in the format DD-MM-YYYY:HH:mm:ss");
        }
    }

    /**
     * String representation of the date. It returns the date itself.
     * @return the date itself
     */
    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm:ss"));
    }

    /**
     * Compares this date to another date. It returns a negative integer, zero, or a positive integer
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this date is less than, equal to, or greater than the specified date.
     */
    public int compareTo(DateTime o) {
        if (this.date == null && o.date == null) return 0;
        if (this.date == null) return -1;
        if (o.date == null) return 1;
        return this.date.compareTo(o.date);
    }

    /**
     * Compares if this date is after another date. It returns true if this date is after the other date, false otherwise.
     * @param otherDate the date to be compared
     */
    public boolean isAfter(DateTime otherDate) {
        if (this.date == null && otherDate.date == null) return false;
        if (this.date == null) return false;
        if (otherDate.date == null) return true;
        return this.date.isAfter(otherDate.date);
    }

    /**
     * Compares if this date is before another date. It returns true if this date is before the other date, false otherwise.
     * @param otherDate the date to be compared
     * @return true if this is before the other date, false otherwise
     */
    public boolean isBefore(DateTime otherDate) {
        if (this.date == null && otherDate.date == null) return false;
        if (this.date == null) return true;
        if (otherDate.date == null) return false;
        return this.date.isBefore(otherDate.date);
    }

    /**
     * Returns a new Date object with the current date.
     * @return a new Date object with the current date
     */
    public static DateTime now() {
        return new DateTime(LocalDateTime.now());
    }

    public LocalDateTime getDate() {
        return this.date;
    }
}