package dankook.cs.aj24.domain.oauth;

import dankook.cs.aj24.domain.user.UserDocument;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class PrincipalDetail implements UserDetails, OAuth2User {

    private UserDocument userDocument;
    private Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;

    public PrincipalDetail(UserDocument userDocument, Collection<? extends GrantedAuthority> authorities) {
        this.userDocument = userDocument;
        this.authorities = authorities;
    }

    public PrincipalDetail(UserDocument userDocument, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.userDocument = userDocument;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    // info 에 들어가는 것들이 토큰에 들어가는 데이터
    public Map<String, Object> getUserInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", userDocument.getName());
        info.put("email", userDocument.getEmail());
        info.put("role", userDocument.getUserRole());
        return info;
    }

    @Override
    public String getName() {
        return userDocument.getEmail();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // 비밀번호 일단 없음
    @Override
    public String getPassword() {
//        return userDocument.getPassword();
        return null;
    }

    @Override
    public String getUsername() {
        return userDocument.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}