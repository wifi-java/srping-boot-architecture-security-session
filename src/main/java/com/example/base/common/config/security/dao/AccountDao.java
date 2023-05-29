package com.example.base.common.config.security.dao;

import com.example.base.domain.member.model.member.Member;

import java.util.Optional;

public interface AccountDao {

  Optional<Member> selectAccount(Member member);
}
