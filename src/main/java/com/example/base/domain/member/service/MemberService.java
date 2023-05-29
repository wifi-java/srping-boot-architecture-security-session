package com.example.base.domain.member.service;

import com.example.base.common.config.security.model.PrincipalDetail;
import com.example.base.common.exception.AuthenticationFailException;
import com.example.base.common.util.AuthUtils;
import com.example.base.domain.member.dto.MemberDto;
import com.example.base.domain.member.dto.SignUpDto;
import com.example.base.domain.member.model.member.Member;
import com.example.base.domain.member.service.user.userService;
import com.example.base.domain.member.service.signup.SignUpService;
import com.example.base.domain.member.service.valid.IdentityValidateService;
import com.example.base.domain.member.service.valid.PasswordValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.base.domain.member.dto.SignUpDto.SignReq;

@RequiredArgsConstructor
@Service
public class MemberService {
  @Qualifier("signUpServiceImpl")
  private final SignUpService signUpService;

  @Qualifier("userServiceImpl")
  private final userService userService;

  @Qualifier("identityValidateServiceImpl")
  private final IdentityValidateService identityValidateService;

  @Qualifier("passwordValidateServiceImpl")
  private final PasswordValidateService passwordValidateService;

  /**
   * 회원 가입
   *
   * @param signReq
   */
  @Transactional
  public void signUp(SignReq signReq) {
    Member member = SignUpDto.toEntity(signReq);

    if (member.isNormalChannel()) {
      identityValidateService.validate(member.getId());
      passwordValidateService.validate(member.getPw(), member.getPwConfirm());

      signUpService.checkExistIdentity(member);
      signUpService.signUpForNormal(member);

    } else {
      signUpService.checkExistIdentity(member);
      signUpService.signUpForSocial(member);
    }
  }

  /**
   * 사용자 정보 조회
   *
   * @return MemberDto.InfoRes
   */
  public MemberDto.InfoRes info() {
    PrincipalDetail principalDetail = AuthUtils.getMemberAuth().orElseThrow(AuthenticationFailException::new);

    // 세션에 있는 정보를 내려준다.
    // return MemberDto.toDto(principalDetail.getMember());

    // 디비 조회해서 정보를 내려준다.
    return MemberDto.toDto(userService.getUser(principalDetail.getMember()));

  }
}
