package org.pedrcruz.backendarch.core.inventorymanagement.infrastructure.repositories.impl;

import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.inventorymanagement.domain.model.Inventory;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataInventoryRepository extends JpaRepository<Inventory, Long> {

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

    @Query("SELECT i FROM Inventory i WHERE i.currentQuantity < i.minimumStockLevel AND i.activityStatus.status = true")
    List<Inventory> findBelowMinimumStock();

    @Query("SELECT i FROM Inventory i WHERE i.currentQuantity <= i.reorderPoint AND i.activityStatus.status = true")
    List<Inventory> findAtReorderPoint();

    @Query("SELECT i FROM Inventory i WHERE i.currentQuantity = 0 AND i.activityStatus.status = true")
    List<Inventory> findOutOfStock();

    @Query("SELECT i FROM Inventory i WHERE i.currentQuantity > i.maximumStockLevel AND i.activityStatus.status = true")
    List<Inventory> findAboveMaximumStock();

    boolean existsByProduct(Product product);

    boolean existsByProductId(Long productId);

    long countByActivityStatus(ActivityStatus activityStatus);
}
