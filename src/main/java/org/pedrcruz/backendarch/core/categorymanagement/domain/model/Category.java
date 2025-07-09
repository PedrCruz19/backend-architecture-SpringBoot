package org.pedrcruz.backendarch.core.categorymanagement.domain.model;



import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.representations.dto.DTOable;
import jakarta.persistence.*;
import lombok.Getter;
import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.domain.Date;
import org.pedrcruz.backendarch.core.domain.Word;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

/**
 * A category entity representing hierarchical categories for archery products.
 * Categories can have parent categories and subcategories, allowing for
 * organized product classification like: Bows -> Recurve Bows -> Traditional Recurve
 */
@Getter
@Entity
@Table(name = "categories")
public class Category implements AggregateRoot<Word>{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverride(name = "word", column = @Column(name = "name_word", unique = true))
    private Word name;

    @Embedded
    @AttributeOverride(name = "word", column = @Column(name = "description_word"))
    private Word description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Category> subcategories = new HashSet<>();

    @Embedded
    private ActivityStatus activityStatus;

    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "registration_date"))
    private Date registrationDate;

    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "last_activity_change_date"))
    private Date lastActivityChangeDate;

    protected Category() {
        // for ORM
    }

    /**
     * Constructor for root category (no parent).
     */
    public Category(final Word name, final Word description) {
        this(name, description, null);
    }

    /**
     * Constructor for subcategory (with parent).
     */
    public Category(final Word name, final Word description, final Category parentCategory) {
        if (name == null) {
            throw new IllegalArgumentException("Category name cannot be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("Category description cannot be null");
        }

        this.name = name;
        this.description = description;
        this.parentCategory = parentCategory;
        this.activityStatus = new ActivityStatus(true);
        this.registrationDate = Date.now();
        this.lastActivityChangeDate = Date.now();

        // Add this category to parent's subcategories if parent exists
        if (parentCategory != null) {
            parentCategory.addSubcategory(this);
        }
    }

    /**
     * Adds a subcategory to this category.
     */
    public void addSubcategory(final Category subcategory) {
        if (subcategory == null) {
            throw new IllegalArgumentException("Subcategory cannot be null");
        }
        if (subcategory.equals(this)) {
            throw new IllegalArgumentException("Category cannot be its own subcategory");
        }
        if (this.isDescendantOf(subcategory)) {
            throw new IllegalArgumentException("Cannot create circular category hierarchy");
        }

        this.subcategories.add(subcategory);
        subcategory.parentCategory = this;
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Removes a subcategory from this category.
     */
    public void removeSubcategory(final Category subcategory) {
        if (subcategory != null && this.subcategories.remove(subcategory)) {
            subcategory.parentCategory = null;
            this.lastActivityChangeDate = Date.now();
        }
    }

    /**
     * Checks if this category is a root category (has no parent).
     */
    public boolean isRootCategory() {
        return this.parentCategory == null;
    }

    /**
     * Checks if this category has subcategories.
     */
    public boolean hasSubcategories() {
        return !this.subcategories.isEmpty();
    }

    /**
     * Checks if this category is a descendant of another category.
     */
    public boolean isDescendantOf(final Category potentialAncestor) {
        if (potentialAncestor == null) {
            return false;
        }

        Category current = this.parentCategory;
        while (current != null) {
            if (current.equals(potentialAncestor)) {
                return true;
            }
            current = current.parentCategory;
        }
        return false;
    }

    /**
     * Gets the full hierarchical path of this category (e.g., "Bows > Recurve > Traditional").
     */
    public String getHierarchicalPath() {
        if (isRootCategory()) {
            return this.name.getWord();
        }
        return parentCategory.getHierarchicalPath() + " > " + this.name.getWord();
    }

    /**
     * Gets the depth level of this category in the hierarchy (root = 0).
     */
    public int getDepthLevel() {
        int depth = 0;
        Category current = this.parentCategory;
        while (current != null) {
            depth++;
            current = current.parentCategory;
        }
        return depth;
    }

    /**
     * Deactivates the category and all its subcategories.
     */
    public void deactivate() {
        this.activityStatus = new ActivityStatus(false);
        this.lastActivityChangeDate = Date.now();

        // Deactivate all subcategories
        for (Category subcategory : subcategories) {
            subcategory.deactivate();
        }
    }

    /**
     * Activates the category.
     */
    public void activate() {
        this.activityStatus = new ActivityStatus(true);
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Checks if the category is active.
     */
    public boolean isActive() {
        return this.activityStatus.isActive();
    }


    @Override
    public Word identity() {
        return this.name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return DomainEntities.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof Category)) {
            return false;
        }

        final var that = (Category) other;
        if (this == that) {
            return true;
        }

        return identity().equals(that.identity());
    }

    public void changeName(Word name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name cannot be null");
        }
        this.name = name;
        this.lastActivityChangeDate = Date.now();
    }

    public void changeDescription(Word description) {
        if (description == null) {
            throw new IllegalArgumentException("Category description cannot be null");
        }
        this.description = description;
        this.lastActivityChangeDate = Date.now();
    }


}

