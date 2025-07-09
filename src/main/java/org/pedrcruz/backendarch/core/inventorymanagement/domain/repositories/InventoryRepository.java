package org.pedrcruz.backendarch.core.inventorymanagement.domain.repositories;

import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.inventorymanagement.domain.model.Inventory;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    <S extends Inventory> List<S> saveAll(Iterable<S> entities);

    <S extends Inventory> S save(S entity);

    Optional<Inventory> findById(Long objectId);

    default Inventory getById(final Long id) {
        final var maybeInventory = findById(id);
        // throws 404 Not Found if the inventory does not exist or if it is not active
        return maybeInventory
                .filter(Inventory::isActive)
                .orElseThrow(() -> new NotFoundException(Inventory.class, id));
    }

    List<Inventory> findAll();

    List<Inventory> findByActivityStatus(ActivityStatus activityStatus);

    Optional<Inventory> findByProduct(Product product);

    Optional<Inventory> findByProductId(Long productId);

    List<Inventory> findByCurrentQuantityLessThan(int quantity);

    List<Inventory> findByCurrentQuantityGreaterThan(int quantity);

    List<Inventory> findByCurrentQuantityBetween(int minQuantity, int maxQuantity);

    List<Inventory> findByCurrentQuantityLessThanEqual(int quantity);

    List<Inventory> findByMinimumStockLevelGreaterThan(int level);

    List<Inventory> findByMaximumStockLevelLessThan(int level);

    List<Inventory> findByReorderPointGreaterThan(int point);

    List<Inventory> findBelowMinimumStock();

    List<Inventory> findAtReorderPoint();

    List<Inventory> findOutOfStock();

    List<Inventory> findAboveMaximumStock();

    void deleteById(Long id);

    void delete(Inventory entity);

    boolean existsById(Long id);

    boolean existsByProduct(Product product);

    boolean existsByProductId(Long productId);

    long count();

    long countByActivityStatus(ActivityStatus activityStatus);
}
