package dankook.cs.aj24.domain.oauth;

import dankook.cs.aj24.domain.jwt.AuthTokens;

public class OAuthResponse {
    private Long id;
    private String nickname;
    private String email;
    private AuthTokens token;

    public OAuthResponse(Long id, String nickname, String email, AuthTokens token) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.token = token;
    }
}
