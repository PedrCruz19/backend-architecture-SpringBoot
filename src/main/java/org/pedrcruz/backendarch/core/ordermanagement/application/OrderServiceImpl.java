package org.pedrcruz.backendarch.core.ordermanagement.application;

import org.pedrcruz.backendarch.core.ordermanagement.domain.model.Order;
import org.pedrcruz.backendarch.core.ordermanagement.domain.model.OrderStatus;
import org.pedrcruz.backendarch.core.ordermanagement.domain.repositories.OrderRepository;
import org.pedrcruz.backendarch.core.productmanagement.application.ProductService;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.core.usermanagement.application.UserService;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.pedrcruz.backendarch.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;

    public OrderServiceImpl(final OrderRepository orderRepository,
                           final UserService userService,
                           final ProductService productService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public Order createOrder(final User customer, final String notes) {
        final var order = new Order(customer, notes);
        return orderRepository.save(order);
    }

    @Override
    public Order createOrder(final Long customerId, final String notes) {
        final var customer = userService.getUser(customerId);
        return createOrder(customer, notes);
    }

    @Override
    public Order addItemToOrder(final Long orderId, final Product product, final int quantity, final BigDecimal unitPrice) {
        final var order = orderRepository.getById(orderId);
        order.addItem(product, quantity, unitPrice);
        return orderRepository.save(order);
    }

    @Override
    public Order addItemToOrder(final Long orderId, final Long productId, final int quantity) {
        final var order = orderRepository.getById(orderId);
        final var product = productService.getById(productId);
        final var unitPrice = product.getPrice();
        order.addItem(product, quantity, unitPrice);
        return orderRepository.save(order);
    }

    @Override
    public Order removeItemFromOrder(final Long orderId, final Long itemId) {
        final var order = orderRepository.getById(orderId);
        order.removeItem(itemId);
        return orderRepository.save(order);
    }

    @Override
    public Order updateItemQuantity(final Long orderId, final Long itemId, final int newQuantity) {
        final var order = orderRepository.getById(orderId);
        order.updateItemQuantity(itemId, newQuantity);
        return orderRepository.save(order);
    }

    @Override
    public Order confirmOrder(final Long orderId) {
        final var order = orderRepository.getById(orderId);
        order.confirm();
        return orderRepository.save(order);
    }

    @Override
    public Order startPreparingOrder(final Long orderId) {
        final var order = orderRepository.getById(orderId);
        order.startPreparing();
        return orderRepository.save(order);
    }

    @Override
    public Order markOrderAsReady(final Long orderId) {
        final var order = orderRepository.getById(orderId);
        order.markAsReady();
        return orderRepository.save(order);
    }

    @Override
    public Order deliverOrder(final Long orderId) {
        final var order = orderRepository.getById(orderId);
        order.deliver();
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(final Long orderId) {
        final var order = orderRepository.getById(orderId);
        order.cancel();
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderNotes(final Long orderId, final String notes) {
        final var order = orderRepository.getById(orderId);
        order.updateNotes(notes);
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(final Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByStatus(final OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomer(final User customer) {
        return orderRepository.findByCustomer(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(final Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerAndStatus(final User customer, final OrderStatus status) {
        return orderRepository.findByCustomerAndStatus(customer, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerIdAndStatus(final Long customerId, final OrderStatus status) {
        return orderRepository.findByCustomerIdAndStatus(customerId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findActiveOrders() {
        return orderRepository.findActiveOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findPendingOrders() {
        return orderRepository.findPendingOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findConfirmedOrders() {
        return orderRepository.findConfirmedOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findPreparingOrders() {
        return orderRepository.findPreparingOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findReadyOrders() {
        return orderRepository.findReadyOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findDeliveredOrders() {
        return orderRepository.findDeliveredOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findCancelledOrders() {
        return orderRepository.findCancelledOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByTotalAmountRange(final BigDecimal minAmount, final BigDecimal maxAmount) {
        return orderRepository.findByTotalAmountBetween(minAmount, maxAmount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findOrdersWithMinimumItems(final int minItems) {
        return orderRepository.findOrdersWithMinimumItems(minItems);
    }

    @Override
    public void deleteOrder(final Long id) {
        final var order = orderRepository.getById(id);
        orderRepository.delete(order);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(final Long id) {
        return orderRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasCustomerActiveOrder(final User customer) {
        return orderRepository.findByCustomer(customer).stream()
                .anyMatch(Order::isActive);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasCustomerActiveOrder(final Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .anyMatch(Order::isActive);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalOrderCount() {
        return orderRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getOrderCountByStatus(final OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCustomerOrderCount(final Long customerId) {
        return orderRepository.countByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveOrderCount() {
        return orderRepository.countActiveOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        return orderRepository.findByStatus(OrderStatus.DELIVERED).stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getRevenueByStatus(final OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
