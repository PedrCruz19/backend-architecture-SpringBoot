package org.pedrcruz.backendarch.core.ordermanagement.infrastructure.repositories.impl;

import org.pedrcruz.backendarch.core.ordermanagement.domain.model.Order;
import org.pedrcruz.backendarch.core.ordermanagement.domain.model.OrderStatus;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SpringDataOrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCustomer(User customer);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerAndStatus(User customer, OrderStatus status);

    List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.status IN ('PENDING', 'CONFIRMED', 'PREPARING', 'READY')")
    List<Order> findActiveOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING'")
    List<Order> findPendingOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 'CONFIRMED'")
    List<Order> findConfirmedOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 'PREPARING'")
    List<Order> findPreparingOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 'READY'")
    List<Order> findReadyOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 'DELIVERED'")
    List<Order> findDeliveredOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 'CANCELLED'")
    List<Order> findCancelledOrders();

    List<Order> findByTotalAmountGreaterThan(BigDecimal amount);

    List<Order> findByTotalAmountLessThan(BigDecimal amount);

    List<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    @Query("SELECT o FROM Order o WHERE SIZE(o.items) = :itemCount")
    List<Order> findOrdersWithItemCount(@Param("itemCount") int itemCount);

    @Query("SELECT o FROM Order o WHERE SIZE(o.items) >= :minItems")
    List<Order> findOrdersWithMinimumItems(@Param("minItems") int minItems);

    @Query("SELECT o FROM Order o WHERE SIZE(o.items) <= :maxItems")
    List<Order> findOrdersWithMaximumItems(@Param("maxItems") int maxItems);

    boolean existsByCustomerAndStatus(User customer, OrderStatus status);

    boolean existsByCustomerIdAndStatus(Long customerId, OrderStatus status);

    long countByStatus(OrderStatus status);

    long countByCustomer(User customer);

    long countByCustomerId(Long customerId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status IN ('PENDING', 'CONFIRMED', 'PREPARING', 'READY')")
    long countActiveOrders();
}
