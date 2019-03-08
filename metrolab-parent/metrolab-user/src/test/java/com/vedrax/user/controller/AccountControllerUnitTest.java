package com.vedrax.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedrax.security.TokenService;
import com.vedrax.security.UserPrincipal;
import com.vedrax.user.Application;
import com.vedrax.user.domain.Account;
import com.vedrax.user.dto.AccountDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.vedrax.user.service.AccountService;
import com.vedrax.user.util.AccountUtility;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author remypenchenat
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class AccountControllerUnitTest {

    private final String BASE_URL = "/accounts";
    private final String AUTHORIZATION = "Authorization";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenService tokenService;

    @MockBean
    private AccountService accountService;

    private String securityToken;
    private String validAccountDtoStr;
    private String invalidAccountDtoStr;
    private UserPrincipal userPrincipal;

    @Before
    public void setUp() throws Exception {
        validAccountDtoStr = mockAccount("e.robert@vedrax.com", "Elodie Robert");
        invalidAccountDtoStr = mockAccount("invalid", "Elodie Robert");
        //create JWT
        userPrincipal = new UserPrincipal("finance@vedrax.com", "Remy Penchenat", "ADMIN");
        securityToken = "Bearer " + tokenService.expiring(userPrincipal);
    }

    private String mockAccount(String email, String fullName) throws Exception {
        AccountDto accountDto = AccountUtility.buildAccountDto(email, fullName);
        Account account = AccountUtility.getAccount(accountDto);
        when(accountService.register(accountDto)).thenReturn(account);
        return objectMapper.writeValueAsString(accountDto);
    }

    @After
    public void tearDown() {
        validAccountDtoStr = null;
        invalidAccountDtoStr = null;
        securityToken = null;
    }

    @Test
    public void whenRegistrationIsValid_thenReturnsStatus200() throws Exception {

        mvc.perform(post(BASE_URL)
                .header(AUTHORIZATION, securityToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validAccountDtoStr))
                .andExpect(status().isOk());
    }

    @Test
    public void whenRegistrationHasNoSecurityToken_thenReturnsStatus401() throws Exception {

        mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validAccountDtoStr))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenRegistrationIsInvalid_thenReturnsStatus400() throws Exception {

        mvc.perform(post(BASE_URL)
                .header(AUTHORIZATION, securityToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidAccountDtoStr))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void whenGettingCurrentUserWithSecurityToken_thenReturnsStatus200() throws Exception {

        mvc.perform(get(BASE_URL + "/current")
                .header(AUTHORIZATION, securityToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", is("finance@vedrax.com")));
    }

    @Test
    public void whenGettingCurrentUserWitoutSecurityToken_thenReturnsStatus401() throws Exception {
        mvc.perform(get(BASE_URL + "/current"))
                .andExpect(status().isUnauthorized());
    }

}
