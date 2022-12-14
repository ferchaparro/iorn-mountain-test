package com.ironmountain.imtest.security.token.auth;

import com.ironmountain.imtest.security.exceptions.TokenValidationException;
import com.ironmountain.imtest.security.token.ITokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TokenProvider extends ITokenProvider {

    String createToken(long userId, String username, boolean keepLogged, List<String> roles);

    String resolveToken(String token);

    boolean validateToken(String token) throws TokenValidationException;

    List<String> getRoleList(String token);

    long getUserId(String token);

    String getUsername(String token);

    UserDetails getUserDetails(String token);

    Authentication getAuthentication(String token);
}