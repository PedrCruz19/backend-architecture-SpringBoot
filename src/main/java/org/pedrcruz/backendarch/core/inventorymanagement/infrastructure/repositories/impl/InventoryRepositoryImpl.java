package org.pedrcruz.backendarch.core.inventorymanagement.infrastructure.repositories.impl;

import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.inventorymanagement.domain.model.Inventory;
import org.pedrcruz.backendarch.core.inventorymanagement.domain.repositories.InventoryRepository;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    private final SpringDataInventoryRepository springDataInventoryRepository;

    public InventoryRepositoryImpl(final SpringDataInventoryRepository springDataInventoryRepository) {
        this.springDataInventoryRepository = springDataInventoryRepository;
    }

    @Override
    public <S extends Inventory> List<S> saveAll(final Iterable<S> entities) {
        return springDataInventoryRepository.saveAll(entities);
    }

    @Override
    public <S extends Inventory> S save(final S entity) {
        return springDataInventoryRepository.save(entity);
    }

    @Override
    public Optional<Inventory> findById(final Long objectId) {
        return springDataInventoryRepository.findById(objectId);
    }

    @Override
    public List<Inventory> findAll() {
        return springDataInventoryRepository.findAll();
    }

    @Override
    public List<Inventory> findByActivityStatus(final ActivityStatus activityStatus) {
        return springDataInventoryRepository.findByActivityStatus(activityStatus);
    }

    @Override
    public Optional<Inventory> findByProduct(final Product product) {
        return springDataInventoryRepository.findByProduct(product);
    }

    @Override
    public Optional<Inventory> findByProductId(final Long productId) {
        return springDataInventoryRepository.findByProductId(productId);
    }

    @Override
    public List<Inventory> findByCurrentQuantityLessThan(final int quantity) {
        return springDataInventoryRepository.findByCurrentQuantityLessThan(quantity);
    }

    @Override
    public List<Inventory> findByCurrentQuantityGreaterThan(final int quantity) {
        return springDataInventoryRepository.findByCurrentQuantityGreaterThan(quantity);
    }

    @Override
    public List<Inventory> findByCurrentQuantityBetween(final int minQuantity, final int maxQuantity) {
        return springDataInventoryRepository.findByCurrentQuantityBetween(minQuantity, maxQuantity);
    }

    @Override
    public List<Inventory> findByCurrentQuantityLessThanEqual(final int quantity) {
        return springDataInventoryRepository.findByCurrentQuantityLessThanEqual(quantity);
    }

    @Override
    public List<Inventory> findByMinimumStockLevelGreaterThan(final int level) {
        return springDataInventoryRepository.findByMinimumStockLevelGreaterThan(level);
    }

    @Override
    public List<Inventory> findByMaximumStockLevelLessThan(final int level) {
        return springDataInventoryRepository.findByMaximumStockLevelLessThan(level);
    }

    @Override
    public List<Inventory> findByReorderPointGreaterThan(final int point) {
        return springDataInventoryRepository.findByReorderPointGreaterThan(point);
    }

    @Override
    public List<Inventory> findBelowMinimumStock() {
        return springDataInventoryRepository.findBelowMinimumStock();
    }

    @Override
    public List<Inventory> findAtReorderPoint() {
        return springDataInventoryRepository.findAtReorderPoint();
    }

    @Override
    public List<Inventory> findOutOfStock() {
        return springDataInventoryRepository.findOutOfStock();
    }

    @Override
    public List<Inventory> findAboveMaximumStock() {
        return springDataInventoryRepository.findAboveMaximumStock();
    }

    @Override
    public void deleteById(final Long id) {
        springDataInventoryRepository.deleteById(id);
    }

    @Override
    public void delete(final Inventory entity) {
        springDataInventoryRepository.delete(entity);
    }

    @Override
    public boolean existsById(final Long id) {
        return springDataInventoryRepository.existsById(id);
    }

    @Override
    public boolean existsByProduct(final Product product) {
        return springDataInventoryRepository.existsByProduct(product);
    }

    @Override
    public boolean existsByProductId(final Long productId) {
        return springDataInventoryRepository.existsByProductId(productId);
    }

    @Override
    public long count() {
        return springDataInventoryRepository.count();
    }

    @Override
    public long countByActivityStatus(final ActivityStatus activityStatus) {
        return springDataInventoryRepository.countByActivityStatus(activityStatus);
    }
}
