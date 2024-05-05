package dankook.cs.aj24.domain.oauth;

import dankook.cs.aj24.common.util.UserRole;
import dankook.cs.aj24.domain.user.UserDocument;
import dankook.cs.aj24.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
        String socialId = kakaoUserInfo.getSocialId();
        String name = kakaoUserInfo.getName();

        Optional<UserDocument> bySocialId = userRepository.findBySocialId(socialId);
        UserDocument user = bySocialId.orElseGet(() -> saveSocialUser(socialId, name));

        return new PrincipalDetail(user, Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().getValue())),
                attributes);
    }

    // 소셜 ID 로 가입된 사용자가 없으면 새로운 사용자를 만들어 저장한다
    public UserDocument saveSocialUser(String socialId, String name) {
        UserDocument newUser = UserDocument.builder().socialId(socialId).name(name).userRole(UserRole.USER).build();
        return userRepository.save(newUser);
    }

}
