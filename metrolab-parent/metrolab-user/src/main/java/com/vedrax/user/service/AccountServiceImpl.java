package com.vedrax.user.service;

import com.vedrax.exception.ApiException;
import com.vedrax.security.TokenService;
import com.vedrax.security.UserPrincipal;
import com.vedrax.user.domain.Account;
import com.vedrax.user.dto.AccountDto;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.Validate;
import com.vedrax.user.repository.AccountRepository;

/**
 *
 * An {@link AccountService} implementation for managing user account
 *
 * @author remypenchenat
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
            TokenService tokenService,
            PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Sign up a new user with the provided DTO
     *
     * @param accountDto
     * @return
     */
    @Override
    public Account register(AccountDto accountDto) {
        Validate.notNull(accountDto, "accountDto should be provided");

        validateIfNotRegistered(accountDto.getEmail());
        Account account = constructAccountWithDto(accountDto);
        return accountRepository.save(account);
    }

    /**
     * Throws an {@link ApiException} if the user is already registered
     *
     * @param email
     */
    private void validateIfNotRegistered(String email) {
        Validate.notNull(email, "email should be provided");
        
        Optional<Account> userOpt = accountRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            throw new ApiException("User with email [" + email + "] already registered.");
        }
    }

    /**
     * Constructs an account with the provided account DTO
     *
     * @param accountDto
     * @return
     */
    private Account constructAccountWithDto(AccountDto accountDto) {
        Account account = new Account();
        account.setEmail(accountDto.getEmail());
        account.setFullName(accountDto.getFullName());
        account.setSecurityRole(accountDto.getSecurityRole());
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        return account;
    }

    /**
     * Gain access to a user with the provided credentials. If the user is
     * authenticated returns a security token
     *
     * @param username
     * @param password
     * @return The security token if authenticated
     */
    @Override
    public Optional<String> login(String username, String password) {
        Validate.notNull(username, "username should be provided");
        Validate.notNull(password, "password should be provided");

        return accountRepository
                .findByEmail(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> createToken(user));
    }

    /**
     * Create a security token with the provided account
     *
     * @param account
     * @return
     */
    private String createToken(Account account) {
        UserPrincipal userPrincipal = new UserPrincipal(account.getEmail(),
                account.getFullName(), account.getSecurityRole());
        return tokenService.expiring(userPrincipal);
    }

}
