package com.example.base.common.util;

import com.example.base.common.config.security.model.PrincipalDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class AuthUtils {
  /**
   * 로그인된 사용자 정보
   *
   * @return Optional<PrincipalDetail>
   */
  public static Optional<PrincipalDetail> getMemberAuth() {
    PrincipalDetail principalDetail = null;
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication.getPrincipal() instanceof PrincipalDetail) {
        principalDetail = (PrincipalDetail) authentication.getPrincipal();
      }
    } catch (Exception ex) {
      log.error("", ex);
    }

    return Optional.ofNullable(principalDetail);
  }
}
