package org.pedrcruz.backendarch.core.categorymanagement.bootstrapping;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"org.pedrcruz.backendarch.core.categorymanagement"})
@EntityScan(basePackages = {"org.pedrcruz.backendarch.core.categorymanagement.domain.model"})
@EnableJpaRepositories(basePackages = {"org.pedrcruz.backendarch.core.categorymanagement.infrastructure"})
public class CategoryManagementConfig {
    // Configuration for category management module
}
