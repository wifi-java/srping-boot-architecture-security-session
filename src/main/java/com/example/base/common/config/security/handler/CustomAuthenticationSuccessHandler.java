package com.example.base.common.config.security.handler;

import com.example.base.common.response.ResultData;
import com.example.base.common.response.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request
          , HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    ResultData<?> resultData = ResultData.of(Status.SUCCESS);
    String jsonString = objectMapper.writeValueAsString(resultData);

    response.setContentType("text/html; charset=utf-8");
    response.setCharacterEncoding("utf-8");

    response.setStatus(HttpStatus.OK.value());

    OutputStream out = response.getOutputStream();
    out.write(jsonString.getBytes());
  }
}
