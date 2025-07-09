package org.pedrcruz.backendarch.core.inventorymanagement.application;

import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.inventorymanagement.domain.model.Inventory;
import org.pedrcruz.backendarch.core.inventorymanagement.domain.repositories.InventoryRepository;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(final InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Inventory createInventory(final Product product, final int currentQuantity, final int minimumStockLevel,
                                   final int maximumStockLevel, final int reorderPoint, final int reorderQuantity) {
        if (inventoryRepository.existsByProduct(product)) {
            throw new IllegalArgumentException("Inventory already exists for this product");
        }

        final var inventory = new Inventory(product, currentQuantity, minimumStockLevel,
                maximumStockLevel, reorderPoint, reorderQuantity);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateInventory(final Long id, final int currentQuantity, final int minimumStockLevel,
                                   final int maximumStockLevel, final int reorderPoint, final int reorderQuantity) {
        final var inventory = inventoryRepository.getById(id);

        inventory.updateQuantity(currentQuantity);
        inventory.updateMinimumStockLevel(minimumStockLevel);
        inventory.updateMaximumStockLevel(maximumStockLevel);
        inventory.updateReorderPoint(reorderPoint);
        inventory.updateReorderQuantity(reorderQuantity);

        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateQuantity(final Long id, final int quantity) {
        final var inventory = inventoryRepository.getById(id);
        inventory.updateQuantity(quantity);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory addQuantity(final Long id, final int quantity) {
        final var inventory = inventoryRepository.getById(id);
        inventory.addQuantity(quantity);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory removeQuantity(final Long id, final int quantity) {
        final var inventory = inventoryRepository.getById(id);
        inventory.removeQuantity(quantity);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateMinimumStockLevel(final Long id, final int minimumStockLevel) {
        final var inventory = inventoryRepository.getById(id);
        inventory.updateMinimumStockLevel(minimumStockLevel);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateMaximumStockLevel(final Long id, final int maximumStockLevel) {
        final var inventory = inventoryRepository.getById(id);
        inventory.updateMaximumStockLevel(maximumStockLevel);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateReorderPoint(final Long id, final int reorderPoint) {
        final var inventory = inventoryRepository.getById(id);
        inventory.updateReorderPoint(reorderPoint);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateReorderQuantity(final Long id, final int reorderQuantity) {
        final var inventory = inventoryRepository.getById(id);
        inventory.updateReorderQuantity(reorderQuantity);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory activateInventory(final Long id) {
        final var inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Inventory.class, id));
        inventory.activate();
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory deactivateInventory(final Long id) {
        final var inventory = inventoryRepository.getById(id);
        inventory.deactivate();
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventory> findById(final Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventory> findByProduct(final Product product) {
        return inventoryRepository.findByProduct(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventory> findByProductId(final Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findActiveInventories() {
        return inventoryRepository.findByActivityStatus(new ActivityStatus(true));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findInactiveInventories() {
        return inventoryRepository.findByActivityStatus(new ActivityStatus(false));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findLowStockInventories() {
        return inventoryRepository.findBelowMinimumStock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findInventoriesAtReorderPoint() {
        return inventoryRepository.findAtReorderPoint();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findOutOfStockInventories() {
        return inventoryRepository.findOutOfStock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findOverstockedInventories() {
        return inventoryRepository.findAboveMaximumStock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findByQuantityRange(final int minQuantity, final int maxQuantity) {
        return inventoryRepository.findByCurrentQuantityBetween(minQuantity, maxQuantity);
    }

    @Override
    public void deleteInventory(final Long id) {
        final var inventory = inventoryRepository.getById(id);
        inventoryRepository.delete(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(final Long id) {
        return inventoryRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProduct(final Product product) {
        return inventoryRepository.existsByProduct(product);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProductId(final Long productId) {
        return inventoryRepository.existsByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalInventoryCount() {
        return inventoryRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveInventoryCount() {
        return inventoryRepository.countByActivityStatus(new ActivityStatus(true));
    }
}
