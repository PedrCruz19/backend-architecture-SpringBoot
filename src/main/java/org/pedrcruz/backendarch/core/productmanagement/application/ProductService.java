package org.pedrcruz.backendarch.core.productmanagement.application;

import org.pedrcruz.backendarch.core.productmanagement.api.dto.CreateProductRequest;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.pedrcruz.backendarch.pagination.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findProductByName(String name);

    Product createProduct(CreateProductRequest request);

    Product editProduct(Long id, CreateProductRequest request);

    List<Product> searchProducts(SearchProductQuery query, Page page);

    Product getById(Long id);

    Product deleteProduct(Long id);

    Product updateStock(Long id, int quantity);

    List<Product> findAll();

    List<Product> findActiveProducts();

    Optional<Product> findById(Long id);
}
