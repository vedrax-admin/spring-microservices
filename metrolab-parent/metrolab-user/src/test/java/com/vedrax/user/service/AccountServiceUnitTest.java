package com.vedrax.user.service;

import com.vedrax.exception.ApiException;
import com.vedrax.security.TokenService;
import com.vedrax.security.UserPrincipal;
import com.vedrax.user.domain.Account;
import com.vedrax.user.dto.AccountDto;
import com.vedrax.user.repository.AccountRepository;
import com.vedrax.user.util.AccountUtility;
import java.util.Optional;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author remypenchenat
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceUnitTest {

    @Autowired
    private AccountService accountService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    private AccountDto accountBernardJojoDto;
    private AccountDto accountRemyDto;
    private Account accountBernardJojo;
    private Account accountRemy;

    @Before
    public void setUp() {
        accountBernardJojoDto = AccountUtility.buildAccountDto("bernard.jojo@vedrax.com", "Bernard Jojo");
        accountRemyDto = AccountUtility.buildAccountDto("finance@vedrax.com", "Remy Penchenat");
        accountBernardJojo = AccountUtility.getAccount(accountBernardJojoDto);
        accountRemy = AccountUtility.getAccount(accountRemyDto);

        when(accountRepository.findByEmail(accountBernardJojoDto.getEmail())).thenReturn(Optional.empty());
        when(accountRepository.findByEmail(accountRemyDto.getEmail())).thenReturn(Optional.of(accountRemy));
        when(passwordEncoder.encode(accountBernardJojoDto.getPassword())).thenReturn(accountBernardJojo.getPassword());
        when(passwordEncoder.matches("password", accountRemy.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("invalid", accountRemy.getPassword())).thenReturn(false);
        when(tokenService.expiring(ArgumentMatchers.any(UserPrincipal.class))).thenReturn("securityToken");
        when(accountRepository.save(accountBernardJojo)).thenReturn(accountBernardJojo);
    }

    @Test
    public void whenUserIsNotAlreadyRegistered_thenReturnsAccount() {
        Account result = accountService.register(accountBernardJojoDto);
        assertNotNull(result);
    }

    @Test(expected = ApiException.class)
    public void whenUserIsAlreadyRegistered_thenThrowsException() {
        accountService.register(accountRemyDto);
    }

    @Test(expected = NullPointerException.class)
    public void whenRegistrationHasNoDtoAsParameter_thenThrowsException() {
        accountService.register(null);
    }

    @Test
    public void whenLoginIsValid_thenReturnsSecurityToken() {
        Optional<String> securityTokenOpt = accountService.login("finance@vedrax.com", "password");
        assertTrue(securityTokenOpt.isPresent());
    }
    
    @Test
    public void whenLoginIsInvalid_thenReturnsNoSecurityToken() {
        Optional<String> securityTokenOpt = accountService.login("finance@vedrax.com", "invalid");
        assertFalse(securityTokenOpt.isPresent());
    }
    
    @Test(expected = NullPointerException.class)
    public void whenLoginHasNoUsernameAsParameter_thenThrowsException() {
        accountService.login(null, "password");
    }
    
    @Test(expected = NullPointerException.class)
    public void whenLoginHasNoPwdAsParameter_thenThrowsException() {
        accountService.login("finance@vedrax.com", null);
    }

}
