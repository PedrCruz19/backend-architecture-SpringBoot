package org.pedrcruz.backendarch.core.domain;

import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;


/**
 * A Word is a value object that represents a word in the system. It is used to
 * represent keywords, names, and other textual information.
 * A word is a string that contains only letters and has at least two letters.
 * The word is stored in the database as a string.
 */
@Embeddable
@Value
@Accessors(fluent = true)
public class Word implements ValueObject, Comparable<Word>{
    /**
     * The serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * If the word is not set, it will be set to this value.
     */
    private static final String NO_INFO = "";
    /**
     * The word itself. It is a string that contains only letters and has at least two letters.
     */
    String word;

    /**
     * Public getter for the word value.
     * @return the word value
     */
    public String getWord() {
        return word;
    }

    /**
     * The constructor of the Word class. It takes a string as input and checks if it is valid.
     * If it is not valid, it throws an IllegalArgumentException.
     * @param word the word to be set
     */
    public Word(String word) {
        if (!isValid(word)) throw new IllegalArgumentException("Word cannot contain special characters but '_' or spaces, and must have at least two characters.");
        this.word = word;
    }

    /**
     * The default constructor of the Word class. It sets the word to NO_INFO.
     */
    public Word() {
        word = NO_INFO;
    }

    /**
     * String representation of the word. It returns the word itself.
     * @return the word itself
     */
    @Override
    public String toString() {
        return word;
    }

    /**
     * Compares this word to another word. It returns a negative integer, zero, or a positive integer
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this word is less than, equal to, or greater than the specified word.
     */
    @Override
    public int compareTo(Word o) {
        if (this.word == null && o.word == null) return 0;
        if (this.word == null) return -1;
        if (o.word == null) return 1;
        return this.word.compareTo(o.word);
    }

    /**
     * Compares this word to another word. It returns true if they are the same, false otherwise.
     * @param word the word to be compared
     * @return true if they are the same, false otherwise
     */
    public boolean sameAs(Word word) {
        if (this.word == null && word == null) return true;
        if (this.word == null) return false;
        if (word == null) return false;
        return StringUtils.stripAccents(this.word).equalsIgnoreCase(StringUtils.stripAccents(word.toString()));
    }

    /**
     * Compares this word to another object. It returns true if they are the same, false otherwise.
     * @param o the object to be compared
     * @return true if they are the same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return StringUtils.stripAccents(this.word).equalsIgnoreCase(StringUtils.stripAccents(word1.toString()));
    }

    /**
     * Compares this word to a string. It returns true if they are the same, false otherwise.
     * @param word the string to be compared
     * @return true if they are the same, false otherwise
     */
    public boolean sameAsString(String word) {
        if (this.word == null && word == null) return true;
        if (this.word == null) return false;
        if (word == null) return false;
        return StringUtils.stripAccents(this.word).equalsIgnoreCase(StringUtils.stripAccents(word));
    }

    /**
     * Checks if the word is valid. It returns true if it is valid, false otherwise.
     * A word is valid if it contains only letters and has at least two letters.
     * @param word the word to be checked
     * @return true if it is valid, false otherwise
     */
    private boolean isValid(String word) {
        return word.matches("\\p{L}[\\p{L}\\d_ ]+");
    }
}