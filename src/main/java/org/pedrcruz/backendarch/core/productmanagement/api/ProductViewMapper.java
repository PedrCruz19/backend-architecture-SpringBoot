package org.pedrcruz.backendarch.core.productmanagement.api;

import lombok.RequiredArgsConstructor;
import org.pedrcruz.backendarch.core.domain.Date;
import org.pedrcruz.backendarch.core.productmanagement.domain.model.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductViewMapper {

    public ProductView toProductView(final Product product) {
        final var view = new ProductView();
        view.setId(product.getId());
        view.setName(product.getName().getWord());
        view.setDescription(product.getDescription().getWord());
        view.setPrice(product.getPrice());
        view.setCategoryId(product.getCategory().getId());
        view.setCategoryName(product.getCategory().getName().getWord());
        view.setActive(product.isActive());

        final LocalDateTime registrationDate = Date.toLocalDateTime(product.getRegistrationDate(), ZoneId.systemDefault());
        view.setRegistrationDate(registrationDate);

        final LocalDateTime lastActivityChangeDate = Date.toLocalDateTime(product.getLastActivityChangeDate(), ZoneId.systemDefault());
        view.setLastActivityChangeDate(lastActivityChangeDate);

        view.setImageUrl(product.getImageUrl());
        view.setStockQuantity(product.getStockQuantity());

        return view;
    }

    public List<ProductView> toProductView(final List<Product> products) {
        return products.stream()
                .map(this::toProductView)
                .collect(Collectors.toList());
    }
}
