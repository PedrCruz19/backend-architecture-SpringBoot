package org.pedrcruz.backendarch.core.categorymanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.core.categorymanagement.application.SearchCategoryQuery;
import org.pedrcruz.backendarch.core.categorymanagement.domain.model.Category;
import org.pedrcruz.backendarch.core.categorymanagement.domain.repositories.CategoryRepository;
import org.pedrcruz.backendarch.core.domain.ActivityStatus;
import org.pedrcruz.backendarch.core.domain.Word;
import org.pedrcruz.backendarch.exceptions.NotFoundException;
import org.pedrcruz.backendarch.pagination.Page;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "categories")
public interface SpringDataCategoryRepository extends CategoryRepository, CategoryRepoCustom, CrudRepository<Category, Long> {

	@Override
	@CacheEvict(allEntries = true)
	<S extends Category> List<S> saveAll(Iterable<S> entities);

	@Override
	@Caching(evict = { @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
			@CacheEvict(key = "#p0.name", condition = "#p0.name != null") })
	<S extends Category> S save(S entity);

	@Override
	@Cacheable
	Optional<Category> findById(Long objectId);

	@Override
	@Cacheable
	default Category getById(final Long id) {
		final var maybeCategory = findById(id);
		return maybeCategory.filter(Category::isActive)
				.orElseThrow(() -> new NotFoundException(Category.class, id));
	}

	@Override
	@Cacheable
	List<Category> findByParentCategoryId(Long parentId);

	@Override
	@Cacheable
	List<Category> findByParentCategoryIdAndActivityStatus(Long parentId, ActivityStatus activityStatus);

	@Override
	@Cacheable
	List<Category> findByActivityStatus(ActivityStatus activityStatus);

	@Override
	@Cacheable
	List<Category> findByName(Word name);

	@Override
	@Cacheable
	List<Category> findByNameAndActivityStatus(Word name, ActivityStatus activityStatus);

	@Override
	@CacheEvict(allEntries = true)
	void delete(Category category);

}

interface CategoryRepoCustom {
	List<Category> searchCategories(Page page, SearchCategoryQuery query);
}

@RequiredArgsConstructor
class CategoryRepoCustomImpl implements CategoryRepoCustom {

	private final EntityManager em;

	@Override
	public List<Category> searchCategories(final Page page, final SearchCategoryQuery query) {
		final var cb = em.getCriteriaBuilder();
		final CriteriaQuery<Category> cq = cb.createQuery(Category.class);
		final Root<Category> root = cq.from(Category.class);
		cq.select(root);

		final List<Predicate> where = new ArrayList<>();
		if (query != null) {
			if (StringUtils.hasText(query.getName())) {
				where.add(cb.like(cb.lower(root.get("name").get("word")), "%" + query.getName().toLowerCase() + "%"));
			}
			if (query.getActive() != null) {
				where.add(cb.equal(root.get("activityStatus").get("status"), query.getActive()));
			}
			if (query.getParentId() != null) {
				where.add(cb.equal(root.get("parentCategory").get("id"), query.getParentId()));
			}
		}
		cq.where(where.toArray(new Predicate[0]));
		cq.orderBy(cb.asc(root.get("name").get("word")));
		final TypedQuery<Category> queryResult = em.createQuery(cq);
		queryResult.setFirstResult((page.getNumber() - 1) * page.getLimit());
		queryResult.setMaxResults(page.getLimit());
		return queryResult.getResultList();
	}


}