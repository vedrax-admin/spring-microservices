package com.vedrax.security;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 *
 * @author remypenchenat
 */
public class AuthenticationFilterUnitTest {

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/public/**")
    );

    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    private AuthenticationFilter filter;
    private AuthenticationManager authenticationManager;

    @Before
    public void setUp() throws Exception {
        authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenAnswer((InvocationOnMock invocation) -> 
                        (Authentication) invocation.getArguments()[0]);//principal

        filter = new AuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager);
    }

    @Test
    public void whenAuthorizationHeaderIsProvided_thenExtractsAuthenticationToken() throws Exception {

        String securityToken = "securityToken";

        //to be valid, 'bearer ' must be appended
        MockHttpServletRequest request = mockPostRequestWithAuthorizationHeader(SecurityConstants.TOKEN_PREFIX + securityToken);

        Authentication result = filter.attemptAuthentication(request,
                new MockHttpServletResponse());

        assertNotNull(result);
        assertEquals(result.getPrincipal(), securityToken);
    }

    @Test(expected = BadCredentialsException.class)
    public void whenAuthorizationHeaderHasNotBearer_thenThrowsException() throws Exception {
        
        //without 'bearer '
        MockHttpServletRequest request = mockPostRequestWithAuthorizationHeader("securityToken");

        filter.attemptAuthentication(request, new MockHttpServletResponse());
    }

    @Test(expected = BadCredentialsException.class)
    public void whenAuthorizationHeaderHasOnlyBearer_thenThrowsException() throws Exception {
        MockHttpServletRequest request = mockPostRequestWithAuthorizationHeader(SecurityConstants.TOKEN_PREFIX);

        filter.attemptAuthentication(request, new MockHttpServletResponse());
    }
    
    @Test(expected = BadCredentialsException.class)
    public void whenAuthorizationHeaderIsEmpty_thenThrowsException() throws Exception {
        MockHttpServletRequest request = mockPostRequestWithAuthorizationHeader("");

        filter.attemptAuthentication(request, new MockHttpServletResponse());
    }

    @Test(expected = BadCredentialsException.class)
    public void whenAuthorizationHeaderIsNotProvided_thenThrowsException() throws Exception {
        MockHttpServletRequest request = mockPostRequest();

        filter.attemptAuthentication(request, new MockHttpServletResponse());
    }

    private MockHttpServletRequest mockPostRequestWithAuthorizationHeader(String value) {
        MockHttpServletRequest request = mockPostRequest();
        request.addHeader(SecurityConstants.AUTHORIZATION_HEADER, value == null ? "" : value);
        return request;
    }

    private MockHttpServletRequest mockPostRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/");
        return request;
    }

}
