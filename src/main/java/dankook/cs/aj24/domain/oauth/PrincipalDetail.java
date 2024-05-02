package dankook.cs.aj24.domain.oauth;

import dankook.cs.aj24.domain.user.UserDocument;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
public class PrincipalDetail implements UserDetails, OAuth2User {

    private UserDocument userDocument;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    // Constructor for UserDetails
    public PrincipalDetail(UserDocument userDocument) {
        this.userDocument = userDocument;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(userDocument.getUserRole().name()));
    }

    // Constructor for OAuth2User
    public PrincipalDetail(UserDocument userDocument, Map<String, Object> attributes) {
        this.userDocument = userDocument;
        this.attributes = attributes;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(userDocument.getUserRole().name()));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return userDocument.getName();
    }

    @Override // 비밀번호 없음
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userDocument.getName(); // Using kakaoId as the username.
    }

    @Override
    public boolean isAccountNonExpired() {
        return !userDocument.isDeleted();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !userDocument.isDeleted();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !userDocument.isDeleted();
    }

    @Override
    public boolean isEnabled() {
        return !userDocument.isDeleted();
    }
}
