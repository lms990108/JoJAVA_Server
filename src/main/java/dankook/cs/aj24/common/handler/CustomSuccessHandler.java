package dankook.cs.aj24.common.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 스프링 시큐리티 로그인 성공시 핸들러, 아직 사용하지 않음
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 로그인 성공 후 리디렉션할 URL 설정
        String redirectUrl = "http://localhost:8080/"; // 프론트엔드로 리디렉트 할 주소
        response.sendRedirect(redirectUrl);
    }
}
