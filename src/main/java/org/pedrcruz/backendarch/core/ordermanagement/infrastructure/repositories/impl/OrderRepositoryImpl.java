package org.pedrcruz.backendarch.core.ordermanagement.infrastructure.repositories.impl;

import org.pedrcruz.backendarch.core.ordermanagement.domain.model.Order;
import org.pedrcruz.backendarch.core.ordermanagement.domain.model.OrderStatus;
import org.pedrcruz.backendarch.core.ordermanagement.domain.repositories.OrderRepository;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SpringDataOrderRepository springDataOrderRepository;

    public OrderRepositoryImpl(final SpringDataOrderRepository springDataOrderRepository) {
        this.springDataOrderRepository = springDataOrderRepository;
    }

    @Override
    public <S extends Order> List<S> saveAll(final Iterable<S> entities) {
        return springDataOrderRepository.saveAll(entities);
    }

    @Override
    public <S extends Order> S save(final S entity) {
        return springDataOrderRepository.save(entity);
    }

    @Override
    public Optional<Order> findById(final Long objectId) {
        return springDataOrderRepository.findById(objectId);
    }

    @Override
    public List<Order> findAll() {
        return springDataOrderRepository.findAll();
    }

    @Override
    public List<Order> findByStatus(final OrderStatus status) {
        return springDataOrderRepository.findByStatus(status);
    }

    @Override
    public List<Order> findByCustomer(final User customer) {
        return springDataOrderRepository.findByCustomer(customer);
    }

    @Override
    public List<Order> findByCustomerId(final Long customerId) {
        return springDataOrderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> findByCustomerAndStatus(final User customer, final OrderStatus status) {
        return springDataOrderRepository.findByCustomerAndStatus(customer, status);
    }

    @Override
    public List<Order> findByCustomerIdAndStatus(final Long customerId, final OrderStatus status) {
        return springDataOrderRepository.findByCustomerIdAndStatus(customerId, status);
    }

    @Override
    public List<Order> findActiveOrders() {
        return springDataOrderRepository.findActiveOrders();
    }

    @Override
    public List<Order> findPendingOrders() {
        return springDataOrderRepository.findPendingOrders();
    }

    @Override
    public List<Order> findConfirmedOrders() {
        return springDataOrderRepository.findConfirmedOrders();
    }

    @Override
    public List<Order> findPreparingOrders() {
        return springDataOrderRepository.findPreparingOrders();
    }

    @Override
    public List<Order> findReadyOrders() {
        return springDataOrderRepository.findReadyOrders();
    }

    @Override
    public List<Order> findDeliveredOrders() {
        return springDataOrderRepository.findDeliveredOrders();
    }

    @Override
    public List<Order> findCancelledOrders() {
        return springDataOrderRepository.findCancelledOrders();
    }

    @Override
    public List<Order> findByTotalAmountGreaterThan(final BigDecimal amount) {
        return springDataOrderRepository.findByTotalAmountGreaterThan(amount);
    }

    @Override
    public List<Order> findByTotalAmountLessThan(final BigDecimal amount) {
        return springDataOrderRepository.findByTotalAmountLessThan(amount);
    }

    @Override
    public List<Order> findByTotalAmountBetween(final BigDecimal minAmount, final BigDecimal maxAmount) {
        return springDataOrderRepository.findByTotalAmountBetween(minAmount, maxAmount);
    }

    @Override
    public List<Order> findOrdersWithItemCount(final int itemCount) {
        return springDataOrderRepository.findOrdersWithItemCount(itemCount);
    }

    @Override
    public List<Order> findOrdersWithMinimumItems(final int minItems) {
        return springDataOrderRepository.findOrdersWithMinimumItems(minItems);
    }

    @Override
    public List<Order> findOrdersWithMaximumItems(final int maxItems) {
        return springDataOrderRepository.findOrdersWithMaximumItems(maxItems);
    }

    @Override
    public void deleteById(final Long id) {
        springDataOrderRepository.deleteById(id);
    }

    @Override
    public void delete(final Order entity) {
        springDataOrderRepository.delete(entity);
    }

    @Override
    public boolean existsById(final Long id) {
        return springDataOrderRepository.existsById(id);
    }

    @Override
    public boolean existsByCustomerAndStatus(final User customer, final OrderStatus status) {
        return springDataOrderRepository.existsByCustomerAndStatus(customer, status);
    }

    @Override
    public boolean existsByCustomerIdAndStatus(final Long customerId, final OrderStatus status) {
        return springDataOrderRepository.existsByCustomerIdAndStatus(customerId, status);
    }

    @Override
    public long count() {
        return springDataOrderRepository.count();
    }

    @Override
    public long countByStatus(final OrderStatus status) {
        return springDataOrderRepository.countByStatus(status);
    }

    @Override
    public long countByCustomer(final User customer) {
        return springDataOrderRepository.countByCustomer(customer);
    }

    @Override
    public long countByCustomerId(final Long customerId) {
        return springDataOrderRepository.countByCustomerId(customerId);
    }

    @Override
    public long countActiveOrders() {
        return springDataOrderRepository.countActiveOrders();
    }
}
