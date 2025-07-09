	package org.pedrcruz.backendarch.core.usermanagement.domain.model;

	import eapli.framework.representations.dto.DTOable;
	import jakarta.persistence.*;
	import jakarta.validation.constraints.Email;
	import jakarta.validation.constraints.NotBlank;
	import jakarta.validation.constraints.NotNull;
	import lombok.Getter;
	import lombok.Setter;
	import org.springframework.data.annotation.CreatedBy;
	import org.springframework.data.annotation.CreatedDate;
	import org.springframework.data.annotation.LastModifiedBy;
	import org.springframework.data.annotation.LastModifiedDate;
	import org.springframework.data.jpa.domain.support.AuditingEntityListener;
	import org.springframework.security.core.userdetails.UserDetails;

	import java.time.LocalDateTime;
	import java.util.HashSet;
	import java.util.Objects;
	import java.util.Set;
	import java.util.UUID;
	import java.util.stream.Collectors;

	/**
	 * Based on https://github.com/Yoh0xFF/java-spring-security-example
	 *
	 */
	@Entity
	@Table(name = "T_USER")
	@EntityListeners(AuditingEntityListener.class)
	public class User implements UserDetails{

		private static final long serialVersionUID = 1L;

		// database primary key
		@Id
		@GeneratedValue
		@Getter
		private Long id;

		// optimistic lock concurrency control
		@Version
		@Getter
		private Long version;

		// auditing info
		@CreatedDate
		@Column(nullable = false, updatable = false)
		@Getter
		private LocalDateTime createdAt;

		// auditing info
		@LastModifiedDate
		@Column(nullable = false)
		@Getter
		private LocalDateTime modifiedAt;

		// auditing info
		@CreatedBy
		@Column(nullable = false, updatable = false)
		@Getter
		private String createdBy;

		// auditing info
		@LastModifiedBy
		@Column(nullable = false)
		private String modifiedBy;

		@Setter
		@Getter
		private boolean enabled = true;

		@Column(unique = true, updatable = false, nullable = false)
		@Email
		@Getter
		@NotNull
		@NotBlank
		private String username;

		@Column(nullable = false)
		@Getter
		@NotNull
		@NotBlank
		private String password;

		@Getter
		@Setter
		private String fullName;

		@ElementCollection
		@Getter
		private final Set<Role> authorities = new HashSet<>();

		protected User() {
			// for ORM only
		}

		/**
		 *
		 * @param username
		 * @param password
		 */
		public User(final String username, final String password) {
			this.username = username;
			setPassword(password);
		}

		/**
		 * factory method. since mapstruct does not handle protected/private setters
		 * neither more than one public constructor, we use these factory methods for
		 * helper creation scenarios
		 *
		 * @param username
		 * @param password
		 * @param fullName
		 * @return
		 */
		public static User newUser(final String username, final String password, final String fullName) {
			final var u = new User(username, password);
			u.setFullName(fullName);
			return u;
		}

		/**
		 * factory method. since mapstruct does not handle protected/private setters
		 * neither more than one public constructor, we use these factory methods for
		 * helper creation scenarios
		 *
		 * @param username
		 * @param password
		 * @param fullName
		 * @param role
		 * @return
		 */
		public static User newUser(final String username, final String password, final String fullName, final String role) {
			final var u = new User(username, password);
			u.setFullName(fullName);
			u.addAuthority(new Role(role));
			return u;
		}

		public void setPassword(final String password) {
			this.password = Objects.requireNonNull(password);
		}

		public void addAuthority(final Role r) {
			authorities.add(r);
		}

		@Override
		public boolean isAccountNonExpired() {
			return isEnabled();
		}

		@Override
		public boolean isAccountNonLocked() {
			return isEnabled();
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return isEnabled();
		}

		public void anonymizeAndDisable() {
			username = UUID.randomUUID().toString() + "@acme.com";
			setFullName("Anonymous Smith");
			setPassword("blank");
			this.setEnabled(false);
		}
	}