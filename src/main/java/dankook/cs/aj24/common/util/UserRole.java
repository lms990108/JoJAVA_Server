package dankook.cs.aj24.common.util;

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
