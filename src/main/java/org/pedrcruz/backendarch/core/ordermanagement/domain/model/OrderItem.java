package org.pedrcruz.backendarch.core.ordermanagement.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * An order item entity representing individual products within an order.
 * Contains product reference, quantity, and price information.
 */
@Entity
@Table(name = "order_items")
public class OrderItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Getter
    @Column(nullable = false)
    private int quantity;

    @Getter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Getter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    protected OrderItem() {
        // for ORM
    }

    /**
     * Constructor for creating an order item.
     */
    public OrderItem(final Order order, final Product product, final int quantity, final BigDecimal unitPrice) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be null or negative");
        }

        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Updates the quantity and recalculates the total price.
     */
    public void updateQuantity(final int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = newQuantity;
        this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(newQuantity));
    }

    /**
     * Updates the unit price and recalculates the total price.
     */
    public void updateUnitPrice(final BigDecimal newUnitPrice) {
        if (newUnitPrice == null || newUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be null or negative");
        }
        this.unitPrice = newUnitPrice;
        this.totalPrice = newUnitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    /**
     * Calculates the total price based on quantity and unit price.
     */
    public BigDecimal calculateTotalPrice() {
        return this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id != null && id.equals(orderItem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
