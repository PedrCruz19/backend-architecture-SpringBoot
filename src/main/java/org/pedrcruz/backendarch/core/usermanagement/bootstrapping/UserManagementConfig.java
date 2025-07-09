package org.pedrcruz.backendarch.core.usermanagement.bootstrapping;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"org.pedrcruz.backendarch.core.usermanagement"})
@EntityScan(basePackages = {"org.pedrcruz.backendarch.core.usermanagement.domain.model"})
@EnableJpaRepositories(basePackages = {"org.pedrcruz.backendarch.core.usermanagement.infrastructure.repositories.impl"})
public class UserManagementConfig {
    // Configuration for user management module
    // This class is used to set up component scanning and JPA repositories for the user management module
    // It ensures that the necessary components, entities, and repositories are scanned and available for use
    // in the application context.

}
