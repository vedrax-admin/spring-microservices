package com.vedrax.user.controller;

import com.vedrax.security.UserPrincipal;
import com.vedrax.user.domain.Account;
import com.vedrax.user.dto.AccountDto;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vedrax.user.service.AccountService;

/**
 *
 * Endpoints for creating and retrieving user
 *
 * @author remypenchenat
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService userService;

    @Autowired
    public AccountController(AccountService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user
     *
     * @param accountDto The account data
     * @return The created account
     */
    @PostMapping()
    public Account registerNewAccount(@Valid @RequestBody AccountDto accountDto) {
        return userService.register(accountDto);
    }

    /**
     * Get the current authenticated user
     *
     * @param user The authenticated user
     * @return The account information extracted from the security token
     */
    @GetMapping("/current")
    public UserPrincipal getCurrent(@AuthenticationPrincipal final UserPrincipal user) {
        return user;
    }

}
