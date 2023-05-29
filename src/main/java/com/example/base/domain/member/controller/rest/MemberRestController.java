package com.example.base.domain.member.controller.rest;

import com.example.base.common.response.ResultData;
import com.example.base.common.response.Status;
import com.example.base.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.example.base.domain.member.dto.SignUpDto.SignReq;

@RequestMapping(path = "/api/member/v1")
@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberRestController {

  private final MemberService memberService;

  @PostMapping(path = "/sign-up")
  @Operation(summary = "회원가입", description = "회원가입")
  public ResultData<?> signUp(@RequestBody SignReq signReq) {
    memberService.signUp(signReq);
    return ResultData.of(Status.SUCCESS);
  }

  @GetMapping(path = "/info")
  @Operation(summary = "회원정보", description = "회원정보를 조회한다.")
  public ResultData<?> info() {
    return ResultData.of(Status.SUCCESS, memberService.info());
  }
}
