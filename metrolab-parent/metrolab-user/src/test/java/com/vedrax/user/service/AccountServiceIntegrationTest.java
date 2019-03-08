package com.vedrax.user.service;

import com.vedrax.exception.ApiException;
import com.vedrax.user.domain.Account;
import com.vedrax.user.dto.AccountDto;
import com.vedrax.user.util.AccountUtility;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author remypenchenat
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void whenUserIsNotAlreadyRegistered_thenReturnsAccount() {
        AccountDto userDto = AccountUtility.buildAccountDto("bernard.jojo@vedrax.com", "Bernard Jojo");
        Account result = accountService.register(userDto);

        assertNotNull(result);
    }

    @Test(expected = ApiException.class)
    public void whenUserIsAlreadyRegistered_thenThrowsException() {
        AccountDto userDto = AccountUtility.buildAccountDto("finance@vedrax.com", "Remy Penchenat");
        accountService.register(userDto);
    }

    @Test
    public void whenLoginIsValid_thenReturnsSecurityToken() {
        Optional<String> tokenOpt = accountService.login("finance@vedrax.com", "password");
        
        assertTrue(tokenOpt.isPresent());
    }

    @Test
    public void WhenUnknownUserTryToSignIn_thenReturnsNoSecurityToken() {
        Optional<String> tokenOpt = accountService.login("mouloud@vedrax.com", "password");

        assertFalse(tokenOpt.isPresent());
    }

    @Test
    public void whenLoginIsInvalid_thenReturnsNoSecurityToken() {
        Optional<String> tokenOpt = accountService.login("finance@vedrax.com", "invalid");

        assertFalse(tokenOpt.isPresent());
    }

}
