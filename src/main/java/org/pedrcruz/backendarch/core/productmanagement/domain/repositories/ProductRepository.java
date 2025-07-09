package org.pedrcruz.backendarch.core.productmanagement.domain.repositories;

import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.core.productmanagement.application.SearchProductQuery;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.exceptions.NotFoundException;
import org.pedrcruz.backendarch.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    <S extends Product> List<S> saveAll(Iterable<S> entities);

    <S extends Product> S save(S entity);

    Optional<Product> findById(Long objectId);

    default Product getById(final Long id) {
        final var maybeProduct = findById(id);
        // throws 404 Not Found if the product does not exist or if it is not active
        return maybeProduct
                .filter(Product::isActive)
                .orElseThrow(() -> new NotFoundException(Product.class, id));
    }

    List<Product> findAll(); // Added method to get all products

    List<Product> searchProducts(Page page, SearchProductQuery query);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByCategoryIdAndActivityStatus(Long categoryId, ActivityStatus activityStatus);

    List<Product> findByActivityStatus(ActivityStatus activityStatus);

    List<Product> findByName(Word name);

    List<Product> findByNameAndActivityStatus(Word name, ActivityStatus activityStatus);

    List<Product> findByPriceLessThan(BigDecimal price);

    List<Product> findByPriceGreaterThan(BigDecimal price);

    List<Product> findByStockQuantityLessThan(int quantity);

    //delete product
    void delete(Product product);
}
