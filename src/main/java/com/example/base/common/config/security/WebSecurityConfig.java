package com.example.base.common.config.security;

import com.example.base.common.config.security.handler.CustomAccessDeniedHandler;
import com.example.base.common.config.security.handler.CustomAuthenticationFailureHandler;
import com.example.base.common.config.security.handler.CustomAuthenticationSuccessHandler;
import com.example.base.common.config.security.handler.CustomLogoutSuccessHandler;
import com.example.base.common.config.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
  @Qualifier("SHA512PasswordEncoder")
  private final PasswordEncoder passwordEncoder;
  private final CustomAuthenticationProvider customAuthenticationProvider;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
  private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public WebSecurityCustomizer configure() {
    // Spring Security를 적용하지 않을 리소스를 설정
    return (web) -> web.ignoring()
            .requestMatchers("/css/**")
            .requestMatchers("/js/**");

  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    String LOGIN_URL = "/login";
    String LOGIN_PROCESS_URL = "/api/member/v1/login";
    String LOGOUT_URL = "/api/member/v1/logout";

    return http

            // cors 설정
            //.cors()
            //.configurationSource(corsConfigurationSource())
            //.and()

            .authorizeHttpRequests()

            // 매치 되는 url public 하게 접근 허용
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
            .requestMatchers(LOGIN_URL).permitAll()
            .requestMatchers(LOGIN_PROCESS_URL).permitAll()
            .requestMatchers(LOGOUT_URL).permitAll()
            .requestMatchers("/api/**").permitAll()

            // 인증된 사용자만 접근 허용
            .anyRequest()
            .authenticated()
            .and()

            // 로그인 설정
            .formLogin()
            // 로그인 페이지
            .loginPage(LOGIN_URL)
            // 로그인 요청 url
            .loginProcessingUrl(LOGIN_PROCESS_URL)
            .usernameParameter("id")
            .passwordParameter("pw")
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler(customAuthenticationFailureHandler)
            .and()

            // 로그아웃 설정
            .logout()
            // 로그아웃 요청 url
            .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
            .logoutSuccessHandler(customLogoutSuccessHandler)
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .and()
            .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
            .and()
            // Cross-Site Request Forgery 비활성화
            .csrf().disable()
            .build();
  }

  // CORS 설정
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 쿠키를 받을건지
    configuration.setAllowCredentials(false);

    // 허용할 도메인 url
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));

    // 허용할 method
    configuration.setAllowedMethods(Arrays.asList("*"));

    // 허용할 header
    configuration.addAllowedHeader("*");

    // 클라이언트로 내려줄때 허용할 header
    configuration.setExposedHeaders(Arrays.asList("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;

  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    // 로그인 시도시 CustomAuthenticationProvider에서 처리하도록 위임
    return http
            .getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(customAuthenticationProvider)
            .build();
  }


}
