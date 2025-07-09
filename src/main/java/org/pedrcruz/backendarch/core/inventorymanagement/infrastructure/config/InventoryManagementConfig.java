package org.pedrcruz.backendarch.core.inventorymanagement.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("org.pedrcruz.backendarch.core.inventorymanagement.domain.model")
@EnableJpaRepositories("org.pedrcruz.backendarch.core.inventorymanagement.infrastructure.repositories.impl")
public class InventoryManagementConfig {
}
