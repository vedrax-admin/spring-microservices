package com.vedrax.user.service;

import com.vedrax.user.domain.Account;
import com.vedrax.user.dto.AccountDto;
import java.util.Optional;

/**
 *
 * Interface for managing account
 *
 * @author remypenchenat
 */
public interface AccountService {

    /**
     * Sign up a new user with the provided DTO
     *
     * @param accountDto
     * @return
     */
    Account register(AccountDto accountDto);

    /**
     * Gain access to a user with the provided credentials. If the user is
     * authenticated returns a security token
     *
     * @param username
     * @param password
     * @return the JWT Token
     */
    Optional<String> login(String username, String password);
}
