package com.vedrax.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedrax.user.dto.LoginDto;
import com.vedrax.user.service.AccountService;
import com.vedrax.user.util.AccountUtility;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author remypenchenat
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerUnitTest {

    private final String BASE_URL = "/public/auth";
    private final String LOGIN = "/login";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    private String validCredentials;
    private String credentialsFromUnknownUser;
    private String invalidCredentials;

    @Before
    public void setUp() throws Exception {
        validCredentials = mockLogin("finance@vedrax.com", "securityToken");
        credentialsFromUnknownUser = mockLogin("unknown@domain.com", null);
        invalidCredentials = mockLogin("invalid", null);
    }

    private String mockLogin(String email, String expectedResult) throws Exception {
        LoginDto loginDto = AccountUtility.buildLoginDto(email);
        when(accountService.login(loginDto.getUsername(), loginDto.getPassword())).thenReturn(Optional.ofNullable(expectedResult));
        return objectMapper.writeValueAsString(loginDto);
    }

    @Test
    public void whenLoginIsValid_thenReturnsStatus200() throws Exception {
        performPost(LOGIN, validCredentials, status().isOk());
        verify(accountService, times(1)).login("finance@vedrax.com", "password");
    }

    @Test
    public void whenUserIsNotRegistered_thenReturnsStatus401() throws Exception {
        performPost(LOGIN, credentialsFromUnknownUser, status().isUnauthorized());
    }

    @Test
    public void whenLoginIsInvalid_thenReturnsStatus400() throws Exception {
        performPost(LOGIN, invalidCredentials, status().isBadRequest());
    }

    private void performPost(String url, String content, ResultMatcher status) throws Exception {
        mvc.perform(post(BASE_URL + url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status);
    }

}
