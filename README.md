# Backend Architecture

```


 ______        _        ______  ___  ____   ________  ____  _____  ______                              __      
|_   _ \      / \     .' ___  ||_  ||_  _| |_   __  ||_   \|_   _||_   _ `.                            [ |     
  | |_) |    / _ \   / .'   \_|  | |_/ /     | |_ \_|  |   \ | |    | | `. \    ,--.   _ .--.   .---.  | |--.  
  |  __'.   / ___ \  | |         |  __'.     |  _| _   | |\ \| |    | |  | |    `'_\ : [ `/'`\]/ /'`\] | .-. | 
 _| |__) |_/ /   \ \_\ `.___.'\ _| |  \ \_  _| |__/ | _| |_\   |_  _| |_.' / _  // | |, | |    | \__.  | | | | 
|_______/|____| |____|`.____ .'|____||____||________||_____|\____||______.' (_) \'-;__/[___]   '.___.'[___]|__]
                                                                                                             


```

This application is part of a lab project at ISEP/IPP to teach Domain Driven Design (DDD) REST and Spring Boot. 

_backendarch logo created with [kammerl ascii signature](https://www.kammerl.de/ascii/AsciiSignature.php) using font "varsity"_

## Who do I talk to?

Pedro Cruz [1240589@isep.ipp.pt](emailto:1240589@isep.ipp.pt) / [pruzapple@gmail.com](emailto:pruzapple@gmail.com)

## Architecture

The main architectural style is Hexagonal/Onion.

## Project Structure

Each functional area has a top level package which is then split in the following subpackages:

- api - REST controller, view models/DTOs and corresponding mappers
- application - use case controllers/services
- domain - core business logic
    - model - domain entities and value objects
    - services - domain services
    - repositories - domain repositories (interfaces)
- infrastructure - adapters to infrastructure components 
    - repositories.impl - spring data repositories implementation
- bootstrapping - bootstrapping data mainly for dmeo purposes

# References and bibliography

Start by reading the essential material listed in [EAPLI framework](https://bitbucket.org/pag_isep/eapli.framework/src/master/README.md)

### JPA

- [Entities or DTOs in JPA Queries](https://thoughts-on-java.org/entities-dtos-use-projection/)
- [Primary key mapping](https://thoughts-on-java.org/primary-key-mappings-jpa-hibernate/)
- [equals() and hashCode()](https://thoughts-on-java.org/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/)

### Spring boot readings

Starting points on Spring:

- [Baeldung's springboot tutorial](https://www.baeldung.com/spring-boot-start)
- The complete [Baeldung's Learn Spring Boot](https://www.baeldung.com/spring-boot)
- [mkyong's tutorials](https://www.mkyong.com/tutorials/spring-boot-tutorials/)
- The [Spring.io guides](https://spring.io/guides)
- And the [Spring boot reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)

Fundamental reading used in the scope of this project:

- [Spring boot multi-module projects](https://www.baeldung.com/spring-boot-multiple-modules)
- [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Spring boot console application](https://www.baeldung.com/spring-boot-console-app)
- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Testing in spring Boot](https://www.baeldung.com/spring-boot-testing)

The following guides illustrate how to use some features concretely:

- [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
- [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
- [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
- [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
- [custom banner](https://www.baeldung.com/spring-boot-custom-banners)


