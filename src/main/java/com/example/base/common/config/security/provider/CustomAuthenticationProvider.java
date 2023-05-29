package com.example.base.common.config.security.provider;

import com.example.base.common.config.security.service.SecurityUserService;
import com.example.base.common.exception.AccountDormancyException;
import com.example.base.common.exception.AccountLockException;
import com.example.base.common.exception.AuthenticationException;
import com.example.base.common.exception.LoginFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final SecurityUserService securityUserService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    UserDetails userDetails = securityUserService.loadUserByUsername(username);

    // 비밀번호 검증
    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
      throw new AuthenticationException("LoginException", new LoginFailException());
    }

    // 휴면 계정 상태
    if (!userDetails.isEnabled() || !userDetails.isAccountNonExpired()) {
      throw new AuthenticationException("AccountDormancyException", new AccountDormancyException());
    }

    // 계정 장금 상태
    if (!userDetails.isAccountNonLocked()) {
      throw new AuthenticationException("AccountLockException", new AccountLockException());
    }

    return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
