package org.pedrcruz.backendarch.core.usermanagement.application;

import org.mapstruct.*;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.Role;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.pedrcruz.backendarch.util.mapping.AbstractMapper;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Based on <a href="https://github.com/Yoh0xFF/java-spring-security-example">...</a>
 *
 */
@Mapper(componentModel = "spring")
abstract class EditUserMapper implements AbstractMapper {

	@Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringToRole")
	public abstract User create(CreateUserRequest request);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringToRole")
	public abstract void update(EditUserRequest request, @MappingTarget User user);

	@Named("stringToRole")
	protected Set<Role> stringToRole(final Set<String> authorities) {
		if (authorities != null) {
			return authorities.stream().map(Role::new).collect(toSet());
		}
		return new HashSet<>();
	}

}
