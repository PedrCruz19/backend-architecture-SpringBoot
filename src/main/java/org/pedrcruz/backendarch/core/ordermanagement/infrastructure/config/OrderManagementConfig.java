package org.pedrcruz.backendarch.core.ordermanagement.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("org.pedrcruz.backendarch.core.ordermanagement.domain.model")
@EnableJpaRepositories("org.pedrcruz.backendarch.core.ordermanagement.infrastructure.repositories.impl")
public class OrderManagementConfig {
}
