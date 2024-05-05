package dankook.cs.aj24.domain.jwt;

import io.jsonwebtoken.ExpiredJwtException;

public class CustomExpiredJwtException extends ExpiredJwtException {
    private final String message;

    public CustomExpiredJwtException(String message, ExpiredJwtException source) {
        super(source.getHeader(), source.getClaims(), source.getMessage());
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}