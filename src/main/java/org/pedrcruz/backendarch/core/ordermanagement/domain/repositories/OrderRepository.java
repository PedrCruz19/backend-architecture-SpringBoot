package org.pedrcruz.backendarch.core.ordermanagement.domain.repositories;

import org.pedrcruz.backendarch.core.ordermanagement.domain.model.Order;
import org.pedrcruz.backendarch.core.ordermanagement.domain.model.OrderStatus;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.pedrcruz.backendarch.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    <S extends Order> List<S> saveAll(Iterable<S> entities);

    <S extends Order> S save(S entity);

    Optional<Order> findById(Long objectId);

    default Order getById(final Long id) {
        final var maybeOrder = findById(id);
        // throws 404 Not Found if the order does not exist
        return maybeOrder.orElseThrow(() -> new NotFoundException(Order.class, id));
    }

    List<Order> findAll();

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCustomer(User customer);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerAndStatus(User customer, OrderStatus status);

    List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);

    List<Order> findActiveOrders();

    List<Order> findPendingOrders();

    List<Order> findConfirmedOrders();

    List<Order> findPreparingOrders();

    List<Order> findReadyOrders();

    List<Order> findDeliveredOrders();

    List<Order> findCancelledOrders();

    List<Order> findByTotalAmountGreaterThan(BigDecimal amount);

    List<Order> findByTotalAmountLessThan(BigDecimal amount);

    List<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<Order> findOrdersWithItemCount(int itemCount);

    List<Order> findOrdersWithMinimumItems(int minItems);

    List<Order> findOrdersWithMaximumItems(int maxItems);

    void deleteById(Long id);

    void delete(Order entity);

    boolean existsById(Long id);

    boolean existsByCustomerAndStatus(User customer, OrderStatus status);

    boolean existsByCustomerIdAndStatus(Long customerId, OrderStatus status);

    long count();

    long countByStatus(OrderStatus status);

    long countByCustomer(User customer);

    long countByCustomerId(Long customerId);

    long countActiveOrders();
}
