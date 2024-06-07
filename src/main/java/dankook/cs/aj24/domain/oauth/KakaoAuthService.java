package dankook.cs.aj24.domain.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dankook.cs.aj24.domain.jwt.AuthTokens;
import dankook.cs.aj24.domain.jwt.JwtUtil;
import dankook.cs.aj24.domain.user.UserDocument;
import dankook.cs.aj24.domain.user.UserRepository;
import dankook.cs.aj24.domain.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class KakaoAuthService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;
    private final UserRepository userRepository;

    public KakaoAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 인증 코드로 AccessToken 요청
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                tokenUri,
                HttpMethod.POST,
                request,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse access token response", e);
        }

        return jsonNode.get("access_token").asText();
    }

    // AccessToken으로 유저 정보 요청
    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();

        // 카카오 유저 정보 API로 POST 요청을 보내는 부분
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                request,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse user info response", e);
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String name = jsonNode.get("properties").get("nickname").asText();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", id);
        userInfo.put("email", email);
        userInfo.put("name", name);

        // 유저가 이미 존재하는지 확인
        Optional<UserDocument> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            // 유저가 존재하지 않으면 새로 저장
            UserDocument newUser = UserDocument.builder()
                    .name(name)
                    .email(email)
                    .userRole(UserRole.USER)
                    .build();
            userRepository.save(newUser);
        }

        return userInfo;
    }

    // 유저 정보로 JWT 토큰 생성
    public AuthTokens generateJwtTokens(Map<String, Object> userInfo) {
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        // 클레임 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        claims.put("name", name);
        claims.put("email", email);

        // AccessToken과 RefreshToken 생성
        String accessToken = JwtUtil.generateToken(claims, 60);  // 60분 유효기간
        String refreshToken = JwtUtil.generateToken(claims, 60 * 24 * 14);  // 14일 유효기간

        // AuthTokens 객체 생성 및 반환
        return new AuthTokens(accessToken, refreshToken, "Bearer", 60 * 60L);
    }
}
