package com.example.base.common.config.security.handler;

import com.example.base.common.exception.AccountDormancyException;
import com.example.base.common.exception.AccountLockException;
import com.example.base.common.exception.LoginFailException;
import com.example.base.common.response.ResultData;
import com.example.base.common.response.Status;
import com.example.base.common.util.RequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request
          , HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

    ResultData<?> resultData = ResultData.of(Status.AUTHENTICATION_FAIL);
    Throwable throwable = exception.getCause();

    if (throwable instanceof LoginFailException) {
      resultData = ResultData.of(Status.LOGIN_FAIL);
    } else if (throwable instanceof AccountDormancyException) {
      resultData = ResultData.of(Status.ACCOUNT_DORMANCY);
    } else if (throwable instanceof AccountLockException) {
      resultData = ResultData.of(Status.ACCOUNT_LOCK);
    }

    response.setContentType("text/html; charset=utf-8");
    response.setCharacterEncoding("utf-8");

    String jsonString = objectMapper.writeValueAsString(resultData);

    if (RequestUtils.isNativeMobileApp()) {
      response.setStatus(HttpStatus.OK.value());
    } else {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    OutputStream out = response.getOutputStream();
    out.write(jsonString.getBytes());
  }
}
