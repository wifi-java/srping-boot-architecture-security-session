package com.example.base.common.config.security.service.account.impl;

import com.example.base.common.config.security.dao.AccountDao;
import com.example.base.common.config.security.service.account.AccountService;
import com.example.base.common.exception.AuthenticationException;
import com.example.base.common.exception.LoginFailException;
import com.example.base.domain.member.model.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class AccountServiceImpl implements AccountService {
  private final AccountDao accountDao;

  @Override
  public Member getAccount(Member member) {
    try {
      return accountDao.selectAccount(member).orElseThrow();
    } catch (Exception e) {
      log.error("", e);
      throw new AuthenticationException("LoginFailException", new LoginFailException());
    }
  }
}
