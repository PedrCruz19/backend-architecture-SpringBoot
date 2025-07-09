package org.pedrcruz.backendarch.core.usermanagement.api.mapper;

import org.pedrcruz.backendarch.core.usermanagement.api.dto.CreateUserRequest;
import org.pedrcruz.backendarch.core.usermanagement.api.dto.UpdateUserRequest;
import org.pedrcruz.backendarch.core.usermanagement.api.dto.UserResponse;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.User;
import org.pedrcruz.backendarch.core.usermanagement.domain.model.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .enabled(user.isEnabled())
                .authorities(user.getAuthorities())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .createdBy(user.getCreatedBy())
                .version(user.getVersion())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .build();
    }

    public org.pedrcruz.backendarch.core.usermanagement.application.CreateUserRequest toApplicationRequest(CreateUserRequest request) {
        if (request == null) {
            return null;
        }

        var applicationRequest = new org.pedrcruz.backendarch.core.usermanagement.application.CreateUserRequest(
                request.getUsername(),
                request.getFullName(),
                request.getPassword()
        );

        // Convert Set<Role> to Set<String>
        if (request.getAuthorities() != null) {
            var stringAuthorities = request.getAuthorities().stream()
                    .map(Role::getAuthority)
                    .collect(java.util.stream.Collectors.toSet());
            applicationRequest.setAuthorities(stringAuthorities);
        }

        return applicationRequest;
    }

    public org.pedrcruz.backendarch.core.usermanagement.application.EditUserRequest toApplicationEditRequest(UpdateUserRequest request) {
        if (request == null) {
            return null;
        }

        var applicationRequest = new org.pedrcruz.backendarch.core.usermanagement.application.EditUserRequest();
        applicationRequest.setFullName(request.getFullName());

        // Convert Set<Role> to Set<String>
        if (request.getAuthorities() != null) {
            var stringAuthorities = request.getAuthorities().stream()
                    .map(Role::getAuthority)
                    .collect(java.util.stream.Collectors.toSet());
            applicationRequest.setAuthorities(stringAuthorities);
        }

        return applicationRequest;
    }
}
