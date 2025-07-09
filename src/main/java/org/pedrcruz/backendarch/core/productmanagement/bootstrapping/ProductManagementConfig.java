package org.pedrcruz.backendarch.core.productmanagement.bootstrapping;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"org.pedrcruz.backendarch.core.productmanagement"})
@EntityScan(basePackages = {
    "org.pedrcruz.backendarch.core.productmanagement.domain.model",
    "org.pedrcruz.backendarch.core.categorymanagement.domain.model"
})
@EnableJpaRepositories(basePackages = {"org.pedrcruz.backendarch.core.productmanagement.infrastructure"})
public class ProductManagementConfig {
    // Configuration for product management module
}
