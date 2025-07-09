package org.pedrcruz.backendarch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Paulo Gandra de Sousa
 *
 */
@SpringBootApplication
@EntityScan(basePackages = {
    "org.pedrcruz.backendarch.core.categorymanagement.domain.model",
    "org.pedrcruz.backendarch.core.productmanagement.domain.model",
    "org.pedrcruz.backendarch.core.inventorymanagement.domain.model",
    "org.pedrcruz.backendarch.core.ordermanagement.domain.model",
    "org.pedrcruz.backendarch.core.usermanagement.domain.model"
})
@EnableJpaRepositories(basePackages = {
    "org.pedrcruz.backendarch.core.categorymanagement",
    "org.pedrcruz.backendarch.core.productmanagement",
    "org.pedrcruz.backendarch.core.inventorymanagement",
    "org.pedrcruz.backendarch.core.ordermanagement",
    "org.pedrcruz.backendarch.core.usermanagement"
})
public class BackendARCH {

	public static void main(final String[] args) {
		SpringApplication.run(BackendARCH.class, args);
	}

}
