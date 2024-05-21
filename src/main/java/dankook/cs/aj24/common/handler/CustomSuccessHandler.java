package dankook.cs.aj24.common.handler;

import com.google.gson.Gson;
import dankook.cs.aj24.domain.jwt.JwtConstants;
import dankook.cs.aj24.domain.jwt.JwtUtil;
import dankook.cs.aj24.domain.oauth.PrincipalDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 접근할 수 있는 사용자의 주요 정보 추출
        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();

        // 사용자 정보와 토큰을 포함한 응답 맵 생성
        Map<String, Object> responseMap = principal.getUserInfo();
        responseMap.put("accessToken", JwtUtil.generateToken(responseMap, JwtConstants.ACCESS_EXP_TIME));
        responseMap.put("refreshToken", JwtUtil.generateToken(responseMap, JwtConstants.REFRESH_EXP_TIME));
        responseMap.put("redirectUrl", "http://localhost:8080/login/success");  // 프론트엔드에서 리디렉션할 URL

        // JSON 형태로 응답 생성
        Gson gson = new Gson();
        String json = gson.toJson(responseMap);

        // 응답 타입과 문자 설정
        response.setContentType("application/json; charset=UTF-8");

        // JSON 응답 출력
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();

        // 리디렉션 대신 JSON 응답에 URL 포함
        // 클라이언트는 이 URL을 사용하여 리디렉션을 수행할 수 있음
    }
}
