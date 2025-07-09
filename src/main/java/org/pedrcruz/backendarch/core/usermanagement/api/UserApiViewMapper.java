package org.pedrcruz.backendarch.core.usermanagement.api;

import java.util.List;

import org.mapstruct.Mapper;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.pedrcruz.backendarch.util.mapping.AbstractViewMapper;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Mapper(componentModel = "spring")
public interface UserApiViewMapper extends AbstractViewMapper {

    UserView toUserView(User user);

    List<UserView> toUserView(List<User> users);
}
