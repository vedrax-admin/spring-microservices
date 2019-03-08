package com.vedrax.user.controller;

import com.vedrax.user.dto.LoginDto;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vedrax.user.service.AccountService;
import org.springframework.security.authentication.BadCredentialsException;

/**
 *
 * Public Endpoint for user authentication
 *
 * @author remypenchenat
 */
@RestController
@RequestMapping("/public/auth")
public class AuthenticationController {

    private final AccountService accountService;

    @Autowired
    public AuthenticationController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Sign in a user with the provided credentials
     *
     * @param loginDto The credentials
     * @return Security token if the user is authenticated
     */
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginDto loginDto) {
        return accountService
                .login(loginDto.getUsername(), loginDto.getPassword())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    }

}
