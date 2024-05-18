package dankook.cs.aj24.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    private UserRole(String value) {
        this.value = value;
    }
}
