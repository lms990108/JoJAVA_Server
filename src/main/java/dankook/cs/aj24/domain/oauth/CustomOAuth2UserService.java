package dankook.cs.aj24.domain.oauth;

import dankook.cs.aj24.domain.user.UserRole;
import dankook.cs.aj24.domain.user.UserDocument;
import dankook.cs.aj24.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

        // KakaoUserInfo 클래스를 통해 소셜 로그인에서 받은 사용자 정보를 추출
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
        String socialId = kakaoUserInfo.getSocialId();
        String name = kakaoUserInfo.getName();
        String email = kakaoUserInfo.getEmail();
        String profileImg = kakaoUserInfo.getProfileImg(); // 프로필 이미지 URL

        // 소셜 ID를 기준으로 기존 사용자를 검색하거나 새로운 사용자를 등록
        Optional<UserDocument> bySocialId = userRepository.findBySocialId(socialId);
        UserDocument user = bySocialId.orElseGet(() -> saveSocialUser(socialId, name, profileImg, email));

        PrincipalDetail principalDetail = new PrincipalDetail(user, Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().getValue())),
                attributes);

        // SecurityContextHolder에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(
                new OAuth2AuthenticationToken(principalDetail, principalDetail.getAuthorities(), userRequest.getClientRegistration().getRegistrationId())
        );

        return principalDetail;
    }

    // 사용자 정보를 저장하는 메서드, 필요한 정보를 모두 파라미터로 전달
    public UserDocument saveSocialUser(String socialId, String name, String profileImg, String email) {
        UserDocument newUser = UserDocument.builder()
                .name(name)
                .profileImg(profileImg)
                .email(email)
                .userRole(UserRole.USER)
                .build();
        return userRepository.save(newUser);
    }
}
