package org.pedrcruz.backendarch.core.ordermanagement.application;

import org.pedrcruz.backendarch.core.ordermanagement.domain.model.Order;
import org.pedrcruz.backendarch.core.ordermanagement.domain.model.OrderStatus;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(User customer, String notes);

    Order createOrder(Long customerId, String notes);

    Order addItemToOrder(Long orderId, Product product, int quantity, BigDecimal unitPrice);

    Order addItemToOrder(Long orderId, Long productId, int quantity);

    Order removeItemFromOrder(Long orderId, Long itemId);

    Order updateItemQuantity(Long orderId, Long itemId, int newQuantity);

    Order confirmOrder(Long orderId);

    Order startPreparingOrder(Long orderId);

    Order markOrderAsReady(Long orderId);

    Order deliverOrder(Long orderId);

    Order cancelOrder(Long orderId);

    Order updateOrderNotes(Long orderId, String notes);

    Optional<Order> findById(Long id);

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

    List<Order> findByTotalAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    List<Order> findOrdersWithMinimumItems(int minItems);

    void deleteOrder(Long id);

    boolean existsById(Long id);

    boolean hasCustomerActiveOrder(User customer);

    boolean hasCustomerActiveOrder(Long customerId);

    long getTotalOrderCount();

    long getOrderCountByStatus(OrderStatus status);

    long getCustomerOrderCount(Long customerId);

    long getActiveOrderCount();

    BigDecimal getTotalRevenue();

    BigDecimal getRevenueByStatus(OrderStatus status);
}
