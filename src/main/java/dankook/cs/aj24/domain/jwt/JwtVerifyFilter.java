package dankook.cs.aj24.domain.jwt;

import com.google.gson.Gson;
import dankook.cs.aj24.common.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class JwtVerifyFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    private static void checkAuthorizationHeader(String header) {
        if(header == null) {
            throw new CustomJwtException("토큰이 전달되지 않았습니다");
        } else if (!header.startsWith(JwtConstants.JWT_TYPE)) {
            throw new CustomJwtException("BEARER 로 시작하지 않는 올바르지 않은 토큰 형식입니다");
        }
    }

    // 필터를 거치지 않을 URL 을 설정하고, true 를 return 하면 현재 필터를 건너뛰고 다음 필터로 이동
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(JwtConstants.WHITELIST, requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(JwtConstants.JWT_HEADER);
            checkAuthorizationHeader(authHeader); // header 형식 검사
            String token = JwtUtil.getTokenFromHeader(authHeader);

            if (redisUtil.isTokenBlacklisted(token)) { // 토큰 블랙리스트 검사
                throw new CustomJwtException("로그인 세션이 만료되었습니다.");
            }

            Authentication authentication = JwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleAuthenticationError(response, e);
        }
    }

    private void handleAuthenticationError(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"error\":\"" + e.getMessage() + "\"}");
        writer.close();
    }

}