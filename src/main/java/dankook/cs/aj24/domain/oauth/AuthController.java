package dankook.cs.aj24.domain.oauth;

import dankook.cs.aj24.domain.jwt.AuthTokens;
import dankook.cs.aj24.domain.oauth.KakaoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "인증 관련 API")
public class AuthController {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @PostMapping("/kakao")
    @Operation(
            summary = "카카오 로그인",
            description = "카카오 인증 코드를 이용해 JWT AccessToken을 발급받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokens.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public AuthTokens kakaoLogin(@RequestParam String code) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        Map<String, Object> userInfo = kakaoAuthService.getUserInfo(accessToken);
        customOAuth2UserService.saveSocialUser(userInfo.get("id").toString(), userInfo.get("name").toString(), "", userInfo.get("email").toString());
        return kakaoAuthService.generateJwtTokens(userInfo);
    }
}
