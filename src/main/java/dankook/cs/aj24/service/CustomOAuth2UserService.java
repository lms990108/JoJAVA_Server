package dankook.cs.aj24.service;

import dankook.cs.aj24.document.UserDocument;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("image");

        UserDocument userDocument = new UserDocument(
                null, // id는 MongoDB에서 자동 생성됩니다.
                nickname, // 카카오에서 받은 nickname
                new ArrayList<>(), // 초기 좋아요 목록은 비어 있습니다.
                profileImage, // 카카오에서 받은 프로필 이미지
                null, // createdAt은 MongoDB에서 자동 설정됩니다.
                null, // updatedAt은 MongoDB에서 자동 설정됩니다.
                null  // deletedAt은 초기에 null 입니다.
        );

        userService.createUser(userDocument);
        return oAuth2User;
    }
}