package org.pedrcruz.backendarch.core.productmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.core.productmanagement.application.SearchProductQuery;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.core.productmanagement.domain.repositories.ProductRepository;
import org.pedrcruz.backendarch.exceptions.NotFoundException;
import org.pedrcruz.backendarch.pagination.Page;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for Product entities
 */
@Repository
@CacheConfig(cacheNames = "products")
public interface SpringDataProductRepository extends ProductRepository, ProductRepoCustom, CrudRepository<Product, Long> {

    @Override
    @CacheEvict(allEntries = true)
    <S extends Product> List<S> saveAll(Iterable<S> entities);

    @Override
    @CacheEvict(allEntries = true)
    <S extends Product> S save(S entity);

    /**
     * findById searches a specific product and returns an optional
     */
    @Override
    @Cacheable
    Optional<Product> findById(Long objectId);

    /**
     * getById explicitly loads a product or throws an exception if the product does not
     * exist or it is not active
     *
     * @param id
     * @return the product
     */
    @Override
    @Cacheable
    default Product getById(final Long id) {
        final var maybeProduct = findById(id);
        // throws 404 Not Found if the product does not exist or if it is not active
        return maybeProduct
                .filter(Product::isActive)
                .orElseThrow(() -> new NotFoundException(Product.class, id));
    }

    @Override
    @Cacheable
    List<Product> findByName(Word name);

    @Override
    @Cacheable
    List<Product> findByNameAndActivityStatus(Word name, ActivityStatus activityStatus);

    @Override
    @Cacheable
    List<Product> findByActivityStatus(ActivityStatus activityStatus);

    @Override
    @Cacheable
    List<Product> findByCategoryId(Long categoryId);

    @Override
    @Cacheable
    List<Product> findByCategoryIdAndActivityStatus(Long categoryId, ActivityStatus activityStatus);

    @Override
    @Cacheable
    List<Product> findByPriceLessThan(BigDecimal price);

    @Override
    @Cacheable
    List<Product> findByPriceGreaterThan(BigDecimal price);

    @Override
    @Cacheable
    List<Product> findByStockQuantityLessThan(int quantity);
}

/**
 * Custom interface to add custom methods to spring repository.
 */
interface ProductRepoCustom {
    List<Product> searchProducts(Page page, SearchProductQuery query);
}

/**
 * Implementation of custom repository methods using JPA Criteria API
 */
@RequiredArgsConstructor
class ProductRepoCustomImpl implements ProductRepoCustom {

    // get the underlying JPA Entity Manager via spring through constructor dependency injection
    private final EntityManager em;

    @Override
    public List<Product> searchProducts(final Page page, final SearchProductQuery query) {
        final var cb = em.getCriteriaBuilder();
        final CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        final Root<Product> root = cq.from(Product.class);
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();

        // Filter by name if provided
        if (StringUtils.hasText(query.getName())) {
            where.add(cb.like(cb.lower(root.get("name").get("word")), "%" + query.getName().toLowerCase() + "%"));
        }

        // Filter by category if provided
        if (query.getCategoryId() != null) {
            where.add(cb.equal(root.get("category").get("id"), query.getCategoryId()));
        }

        // Filter by active status if provided
        if (query.getActive() != null) {
            where.add(cb.equal(root.get("activityStatus").get("active"), query.getActive()));
        }

        // Filter by price range if provided
        if (query.getMinPrice() != null) {
            where.add(cb.greaterThanOrEqualTo(root.get("price"), query.getMinPrice()));
        }

        if (query.getMaxPrice() != null) {
            where.add(cb.lessThanOrEqualTo(root.get("price"), query.getMaxPrice()));
        }

        // Filter by minimum stock quantity if provided
        if (query.getMinStock() != null) {
            where.add(cb.greaterThanOrEqualTo(root.get("stockQuantity"), query.getMinStock()));
        }

        // Apply all filters using AND (all conditions must be met)
        if (!where.isEmpty()) {
            cq.where(cb.and(where.toArray(new Predicate[0])));
        }

        // Order by name by default
        cq.orderBy(cb.asc(root.get("name").get("word")));

        // Apply pagination
        final TypedQuery<Product> q = em.createQuery(cq);

        // Calculate pagination
        int pageNumber = page.getNumber();
        int pageSize = page.getLimit();

        q.setFirstResult((pageNumber) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }
}
