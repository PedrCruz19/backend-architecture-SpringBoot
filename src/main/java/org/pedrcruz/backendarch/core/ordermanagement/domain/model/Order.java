package org.pedrcruz.backendarch.core.ordermanagement.domain.model;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import jakarta.persistence.*;
import lombok.Getter;
import org.pedrcruz.backendarch.core.domain.Date;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An order entity representing customer orders in the cafeteria system.
 * Contains order items, status, total amount, and customer information.
 */
@Entity
@Table(name = "orders")
public class Order implements AggregateRoot<Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Getter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Getter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Getter
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "order_date"))
    private Date orderDate;

    @Getter
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "last_updated_date"))
    private Date lastUpdatedDate;

    @Getter
    @Column(length = 1000)
    private String notes;

    protected Order() {
        // for ORM
    }

    /**
     * Constructor for creating an order.
     */
    public Order(final User customer, final String notes) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        this.customer = customer;
        this.notes = notes;
        this.status = OrderStatus.PENDING;
        this.totalAmount = BigDecimal.ZERO;
        this.orderDate = Date.now();
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Adds an item to the order.
     */
    public void addItem(final Product product, final int quantity, final BigDecimal unitPrice) {
        if (!status.canBeModified()) {
            throw new IllegalStateException("Cannot modify order in status: " + status);
        }

        // Check if product already exists in order
        for (OrderItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.updateQuantity(item.getQuantity() + quantity);
                recalculateTotal();
                this.lastUpdatedDate = Date.now();
                return;
            }
        }

        // Add new item
        final var orderItem = new OrderItem(this, product, quantity, unitPrice);
        items.add(orderItem);
        recalculateTotal();
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Removes an item from the order.
     */
    public void removeItem(final Long itemId) {
        if (!status.canBeModified()) {
            throw new IllegalStateException("Cannot modify order in status: " + status);
        }

        items.removeIf(item -> item.getId().equals(itemId));
        recalculateTotal();
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Updates the quantity of an existing item.
     */
    public void updateItemQuantity(final Long itemId, final int newQuantity) {
        if (!status.canBeModified()) {
            throw new IllegalStateException("Cannot modify order in status: " + status);
        }

        for (OrderItem item : items) {
            if (item.getId().equals(itemId)) {
                item.updateQuantity(newQuantity);
                recalculateTotal();
                this.lastUpdatedDate = Date.now();
                return;
            }
        }
        throw new IllegalArgumentException("Item not found in order");
    }

    /**
     * Confirms the order.
     */
    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only confirm pending orders");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm order with no items");
        }

        this.status = OrderStatus.CONFIRMED;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Starts preparing the order.
     */
    public void startPreparing() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Can only start preparing confirmed orders");
        }

        this.status = OrderStatus.PREPARING;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Marks the order as ready.
     */
    public void markAsReady() {
        if (status != OrderStatus.PREPARING) {
            throw new IllegalStateException("Can only mark preparing orders as ready");
        }

        this.status = OrderStatus.READY;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Delivers the order.
     */
    public void deliver() {
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Can only deliver ready orders");
        }

        this.status = OrderStatus.DELIVERED;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Cancels the order.
     */
    public void cancel() {
        if (!status.canBeCancelled()) {
            throw new IllegalStateException("Cannot cancel order in status: " + status);
        }

        this.status = OrderStatus.CANCELLED;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Updates the order notes.
     */
    public void updateNotes(final String notes) {
        this.notes = notes;
        this.lastUpdatedDate = Date.now();
    }

    /**
     * Gets a read-only view of the order items.
     */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Gets the number of items in the order.
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Gets the total number of products in the order.
     */
    public int getTotalProductCount() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    /**
     * Checks if the order is active (not cancelled or delivered).
     */
    public boolean isActive() {
        return status.isActive();
    }

    /**
     * Checks if the order can be modified.
     */
    public boolean canBeModified() {
        return status.canBeModified();
    }

    /**
     * Checks if the order can be cancelled.
     */
    public boolean canBeCancelled() {
        return status.canBeCancelled();
    }

    /**
     * Recalculates the total amount based on order items.
     */
    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
