package org.pedrcruz.backendarch.core.inventorymanagement.application;

import org.pedrcruz.backendarch.core.inventorymanagement.domain.model.Inventory;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface InventoryService {

    Inventory createInventory(Product product, int currentQuantity, int minimumStockLevel,
                            int maximumStockLevel, int reorderPoint, int reorderQuantity);

    Inventory updateInventory(Long id, int currentQuantity, int minimumStockLevel,
                            int maximumStockLevel, int reorderPoint, int reorderQuantity);

    Inventory updateQuantity(Long id, int quantity);

    Inventory addQuantity(Long id, int quantity);

    Inventory removeQuantity(Long id, int quantity);

    Inventory updateMinimumStockLevel(Long id, int minimumStockLevel);

    Inventory updateMaximumStockLevel(Long id, int maximumStockLevel);

    Inventory updateReorderPoint(Long id, int reorderPoint);

    Inventory updateReorderQuantity(Long id, int reorderQuantity);

    Inventory activateInventory(Long id);

    Inventory deactivateInventory(Long id);

    Optional<Inventory> findById(Long id);

    Optional<Inventory> findByProduct(Product product);

    Optional<Inventory> findByProductId(Long productId);

    List<Inventory> findAll();

    List<Inventory> findActiveInventories();

    List<Inventory> findInactiveInventories();

    List<Inventory> findLowStockInventories();

    List<Inventory> findInventoriesAtReorderPoint();

    List<Inventory> findOutOfStockInventories();

    List<Inventory> findOverstockedInventories();

    List<Inventory> findByQuantityRange(int minQuantity, int maxQuantity);

    void deleteInventory(Long id);

    boolean existsById(Long id);

    boolean existsByProduct(Product product);

    boolean existsByProductId(Long productId);

    long getTotalInventoryCount();

    long getActiveInventoryCount();
}
