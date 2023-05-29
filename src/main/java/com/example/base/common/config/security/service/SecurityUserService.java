package com.example.base.common.config.security.service;

import com.example.base.common.config.security.model.PrincipalDetail;
import com.example.base.common.config.security.service.account.AccountService;
import com.example.base.domain.member.model.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class SecurityUserService implements UserDetailsService {
  @Qualifier("accountServiceImpl")
  private final AccountService accountService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = new Member();
    member.setId(username);

    member = accountService.getAccount(member);

    return PrincipalDetail.builder().member(member).build();
  }
}
