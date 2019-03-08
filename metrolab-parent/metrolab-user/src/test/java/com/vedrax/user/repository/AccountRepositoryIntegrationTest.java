package com.vedrax.user.repository;

import com.vedrax.user.domain.Account;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author remypenchenat
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryIntegrationTest {
    
    @Autowired
    private AccountRepository accountRepository;
    
    public AccountRepositoryIntegrationTest() {
    }

    @Test
    public void whenUserIsRegistered_thenReturnsAccount() {
        Optional<Account> result = accountRepository.findByEmail("finance@vedrax.com");
        
        assertTrue(result.isPresent());
    }
    
    @Test
    public void whenUserIsNotRegistered_thenReturnsNoAccount() {
        Optional<Account> result = accountRepository.findByEmail("mouloud@vedrax.com");
        
        assertFalse(result.isPresent());
    }
    
    @Test
    public void WhenFindByEmailHasNoEmailAsParameter_thenReturnsNoAccount() {
        Optional<Account> result = accountRepository.findByEmail(null);
        
        assertFalse(result.isPresent());
    }
    
}
