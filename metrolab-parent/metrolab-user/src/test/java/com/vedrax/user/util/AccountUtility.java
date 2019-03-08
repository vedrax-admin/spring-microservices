package com.vedrax.user.util;

import com.vedrax.user.domain.Account;
import com.vedrax.user.dto.AccountDto;
import com.vedrax.user.dto.LoginDto;

/**
 *
 * @author remypenchenat
 */
public class AccountUtility {
    
    public static AccountDto buildAccountDto(String email, String fullName) {
        AccountDto accountDto = new AccountDto();
        accountDto.setEmail(email);
        accountDto.setFullName(fullName);
        accountDto.setPassword("password");
        accountDto.setSecurityRole("USER");
        return accountDto;
    }

    public static Account getAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setId(1L);
        account.setEmail(accountDto.getEmail());
        account.setFullName(accountDto.getFullName());
        account.setPassword("$2a$10$.6QjV9sL8OukcnF0aHZxgOCBe7iX29xoJ4dURBx9i8dx8MdhVQpHK");
        account.setSecurityRole("USER");
        return account;
    }
    
    public static LoginDto buildLoginDto(String username) {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword("password");
        return loginDto;
    }
    
}
