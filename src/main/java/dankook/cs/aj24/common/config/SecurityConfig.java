package dankook.cs.aj24.common.config;

import dankook.cs.aj24.domain.jwt.JwtConstants;
import dankook.cs.aj24.domain.oauth.CustomOAuth2UserService;
import dankook.cs.aj24.domain.jwt.JwtVerifyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import dankook.cs.aj24.common.handler.CustomSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration  // 스프링의 설정 클래스를 정의하는 어노테이션
@EnableWebSecurity  // 스프링 시큐리티의 웹 보안 기능을 활성화
@EnableMethodSecurity  // 메서드 수준에서의 보안을 활성화
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtVerifyFilter jwtVerifyFilter;

    @Autowired  // 스프링의 의존성 주입을 위한 생성자
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JwtVerifyFilter jwtVerifyFilter) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtVerifyFilter = jwtVerifyFilter;
    }

    @Bean  // 스프링 컨테이너에 의해 관리되는 빈 객체를 생성
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)  // CSRF(Cross-Site Request Forgery) 보호 기능을 비활성화
                .formLogin(AbstractHttpConfigurer::disable)  // 스프링 시큐리티의 기본 로그인 페이지를 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)  // HTTP 기본 인증 비활성화

                // HTTP 요청에 대한 보안 규칙을 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(JwtConstants.WHITELIST).permitAll()  // 지정된 경로들은 인증 없이 접근 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // "/admin/**" 경로는 'ADMIN' 역할을 가진 사용자만 접근 가능
                        .anyRequest().authenticated())  // 그 외 모든 요청은 인증을 요구

                // JWT 필터 추가
                .addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class)

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))  // OAuth2 로그인 시 사용자 정보를 가져오는 서비스를 설정
                        .successHandler(customSuccessHandler))  // 로그인 성공 후 처리를 위한 핸들러 설정

                // 세션 관리 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션을 생성하지 않고, 상태를 유지하지 않는 정책 설정

                .build();  // HttpSecurity 객체를 사용하여 SecurityFilterChain을 생성
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
