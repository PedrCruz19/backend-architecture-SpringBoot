package org.pedrcruz.backendarch.core.inventorymanagement.domain.model;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import jakarta.persistence.*;
import lombok.Getter;
import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.domain.Date;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;

import java.io.Serial;

/**
 * An inventory entity representing stock levels and inventory management for products.
 * Tracks quantity, minimum stock levels, and movement history.
 */
@Entity
@Table(name = "inventory")
public class Inventory implements AggregateRoot<Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Getter
    @Column(nullable = false)
    private int currentQuantity;

    @Getter
    @Column(nullable = false)
    private int minimumStockLevel;

    @Getter
    @Column(nullable = false)
    private int maximumStockLevel;

    @Getter
    @Column(nullable = false)
    private int reorderPoint;

    @Getter
    @Column(nullable = false)
    private int reorderQuantity;

    @Getter
    @Embedded
    private ActivityStatus activityStatus;

    @Getter
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "created_date"))
    private Date createdDate;

    @Getter
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "last_updated_date"))
    private Date lastUpdatedDate;

    protected Inventory() {
        // for ORM
    }

    /**
     * Constructor for creating an inventory entry.
     */
    public Inventory(final Product product, final int currentQuantity, final int minimumStockLevel,
                    final int maximumStockLevel, final int reorderPoint, final int reorderQuantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (currentQuantity < 0) {
            throw new IllegalArgumentException("Current quantity cannot be negative");
        }
        if (minimumStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }
        if (maximumStockLevel < 0) {
            throw new IllegalArgumentException("Maximum stock level cannot be negative");
        }
        if (reorderPoint < 0) {
            throw new IllegalArgumentException("Reorder point cannot be negative");
        }
        if (reorderQuantity < 0) {
            throw new IllegalArgumentException("Reorder quantity cannot be negative");
        }
        if (maximumStockLevel < minimumStockLevel) {
            throw new IllegalArgumentException("Maximum stock level cannot be less than minimum stock level");
        }

        this.product = product;
        this.currentQuantity = currentQuantity;
        this.minimumStockLevel = minimumStockLevel;
        this.maximumStockLevel = maximumStockLevel;
        this.reorderPoint = reorderPoint;
        this.reorderQuantity = reorderQuantity;
        this.activityStatus = new ActivityStatus(true);
        this.createdDate = Date.now();
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Checks if the inventory is active.
     */
    public boolean isActive() {
        return this.activityStatus.isActive();
    }

    /**
     * Deactivates the inventory.
     */
    public void deactivate() {
        this.activityStatus = new ActivityStatus(false);
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Activates the inventory.
     */
    public void activate() {
        this.activityStatus = new ActivityStatus(true);
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Updates the current quantity.
     */
    public void updateQuantity(final int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.currentQuantity = quantity;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Adds to the current quantity.
     */
    public void addQuantity(final int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity to add cannot be negative");
        }
        this.currentQuantity += quantity;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Removes from the current quantity.
     */
    public void removeQuantity(final int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity to remove cannot be negative");
        }
        if (this.currentQuantity < quantity) {
            throw new IllegalArgumentException("Cannot remove more quantity than available");
        }
        this.currentQuantity -= quantity;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Updates the minimum stock level.
     */
    public void updateMinimumStockLevel(final int minimumStockLevel) {
        if (minimumStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }
        if (minimumStockLevel > this.maximumStockLevel) {
            throw new IllegalArgumentException("Minimum stock level cannot be greater than maximum stock level");
        }
        this.minimumStockLevel = minimumStockLevel;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Updates the maximum stock level.
     */
    public void updateMaximumStockLevel(final int maximumStockLevel) {
        if (maximumStockLevel < 0) {
            throw new IllegalArgumentException("Maximum stock level cannot be negative");
        }
        if (maximumStockLevel < this.minimumStockLevel) {
            throw new IllegalArgumentException("Maximum stock level cannot be less than minimum stock level");
        }
        this.maximumStockLevel = maximumStockLevel;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Updates the reorder point.
     */
    public void updateReorderPoint(final int reorderPoint) {
        if (reorderPoint < 0) {
            throw new IllegalArgumentException("Reorder point cannot be negative");
        }
        this.reorderPoint = reorderPoint;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Updates the reorder quantity.
     */
    public void updateReorderQuantity(final int reorderQuantity) {
        if (reorderQuantity < 0) {
            throw new IllegalArgumentException("Reorder quantity cannot be negative");
        }
        this.reorderQuantity = reorderQuantity;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Checks if the inventory is below minimum stock level.
     */
    public boolean isBelowMinimumStock() {
        return this.currentQuantity < this.minimumStockLevel;
    }

    /**
     * Checks if the inventory is at or below reorder point.
     */
    public boolean isAtReorderPoint() {
        return this.currentQuantity <= this.reorderPoint;
    }

    /**
     * Checks if the inventory is above maximum stock level.
     */
    public boolean isAboveMaximumStock() {
        return this.currentQuantity > this.maximumStockLevel;
    }

    /**
     * Checks if the inventory is out of stock.
     */
    public boolean isOutOfStock() {
        return this.currentQuantity == 0;
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
