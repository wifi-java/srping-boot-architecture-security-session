package com.example.base.common.config.security.handler;

import com.example.base.common.response.ResultData;
import com.example.base.common.response.Status;
import com.example.base.common.util.RequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request
          , HttpServletResponse response
          , AccessDeniedException accessDeniedException) throws IOException, ServletException {


    ResultData<?> resultData = ResultData.of(Status.ACCESS_DENIED);
    String jsonString = objectMapper.writeValueAsString(resultData);

    response.setContentType("text/html; charset=utf-8");
    response.setCharacterEncoding("utf-8");

    if (RequestUtils.isNativeMobileApp()) {
      response.setStatus(HttpStatus.OK.value());
    } else {
      response.setStatus(HttpStatus.FORBIDDEN.value());
    }

    OutputStream out = response.getOutputStream();
    out.write(jsonString.getBytes());


//    response.setContentType("text/html; charset=utf-8");
//    response.setCharacterEncoding("utf-8");
//    PrintWriter out = response.getWriter();
//
//    out.println("<script>");
//    out.println("alert('권한이 없습니다.')");
//    out.println("location.replace('/login')");
//    out.println("</script>");
  }
}
