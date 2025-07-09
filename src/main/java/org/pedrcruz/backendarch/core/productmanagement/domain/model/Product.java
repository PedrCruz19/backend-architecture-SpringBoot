package org.pedrcruz.backendarch.core.productmanagement.domain.model;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import jakarta.persistence.*;
import lombok.Getter;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.domain.Date;
import org.pedrcruz.backendarch.core.domain.Word;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * A product entity representing products available in the cafeteria.
 * Products belong to specific categories and have properties like name,
 * description, price, etc.
 */
@Entity
@Table(name = "products")
public class Product implements AggregateRoot<Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @Embedded
    @AttributeOverride(name = "word", column = @Column(name = "name_word", unique = true))
    private Word name;

    @Getter
    @Embedded
    @AttributeOverride(name = "word", column = @Column(name = "description_word"))
    private Word description;

    @Getter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Getter
    @Embedded
    private ActivityStatus activityStatus;

    @Getter
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "registration_date"))
    private Date registrationDate;

    @Getter
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "last_activity_change_date"))
    private Date lastActivityChangeDate;

    @Getter
    @Column
    private String imageUrl;

    @Getter
    @Column
    private int stockQuantity;

    protected Product() {
        // for ORM
    }

    /**
     * Constructor for creating a product.
     */
    public Product(final Word name, final Word description, final BigDecimal price,
                  final Category category, final String imageUrl, final int stockQuantity) {
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("Product description cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Product price cannot be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Product category cannot be null");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Product stock quantity cannot be negative");
        }

        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.activityStatus = new ActivityStatus(true);
        this.registrationDate = Date.now();
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Checks if the product is active.
     */
    public boolean isActive() {
        return this.activityStatus.isActive();
    }

    /**
     * Deactivates the product.
     */
    public void deactivate() {
        this.activityStatus = new ActivityStatus(false);
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Activates the product.
     */
    public void activate() {
        this.activityStatus = new ActivityStatus(true);
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Changes the product name.
     */
    public void changeName(final Word name) {
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        this.name = name;
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Changes the product description.
     */
    public void changeDescription(final Word description) {
        if (description == null) {
            throw new IllegalArgumentException("Product description cannot be null");
        }
        this.description = description;
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Changes the product price.
     */
    public void changePrice(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Product price cannot be null");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        this.price = price;
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Changes the product category.
     */
    public void changeCategory(final Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Product category cannot be null");
        }
        this.category = category;
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Changes the product image URL.
     */
    public void changeImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Updates the stock quantity.
     */
    public void updateStockQuantity(final int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Product stock quantity cannot be negative");
        }
        this.stockQuantity = stockQuantity;
        this.lastActivityChangeDate = Date.now();
    }

    /**
     * Reduces stock by a given quantity.
     */
    public boolean reduceStock(final int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (this.stockQuantity < quantity) {
            return false;
        }
        this.stockQuantity -= quantity;
        this.lastActivityChangeDate = Date.now();
        return true;
    }

    /**
     * Adds stock.
     */
    public void addStock(final int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.stockQuantity += quantity;
        this.lastActivityChangeDate = Date.now();
    }

    @Override
    public boolean equals(final Object o) {
        return DomainEntities.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public boolean sameAs(final Object other) {
        return DomainEntities.areEqual(this, other);
    }

    @Override
    public Long identity() {
        return this.id;
    }
}
