package com.example.base.domain.member.controller.rest;

import com.example.base.domain.member.service.MemberService;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class MemberRestControllerTest {

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private MemberService memberService;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  public void mockMvcSetUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).apply(springSecurity()).build();
  }

  @Order(1)
  @Test
  @DisplayName("회원가입 테스트")
  public void signUpTest() throws Exception {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "테스터");
    jsonObject.put("id", "test12341");
    jsonObject.put("pw", "a123145234!");
    jsonObject.put("pwConfirm", "a123145234!");

    mockMvc.perform(post("/api/member/v1/sign-up")
                    .content(jsonObject.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
  }

  @Order(2)
  @Test
  @DisplayName("SNS 회원가입 테스트")
  public void signUpForSNSTest() throws Exception {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "111테스터");
    jsonObject.put("id", "1234");
    jsonObject.put("channel", "K");

    mockMvc.perform(post("/api/member/v1/sign-up")
                    .content(jsonObject.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
  }

  @Order(3)
  @Test
  @DisplayName("로그인 테스트")
  public void loginTest() throws Exception {
    mockMvc.perform(formLogin()
                    .loginProcessingUrl("/api/member/v1/login")
                    .userParameter("id")
                    .passwordParam("pw")
                    .user("test1234")
                    .password("a123145234!"))
            .andDo(print())
            .andExpect(status().isOk());
  }

  @Order(4)
  @Test
  @DisplayName("회원정보 테스트")
  @WithUserDetails(value = "test1234", userDetailsServiceBeanName = "securityUserService")
  public void userInfoTest() throws Exception {
    // WithUserDetails 어노테이션을 이용하여 해당 계정으로 로그인 후 해당 계정에 정보를 요청한다.
    // 디비에 test1234가 등록되어 있어야 테스트가 통과한다.

    mockMvc.perform(get("/api/member/v1/info")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
  }
}