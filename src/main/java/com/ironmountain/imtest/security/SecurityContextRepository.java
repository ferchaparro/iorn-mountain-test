package com.ironmountain.imtest.security;

import com.ironmountain.imtest.security.exceptions.TokenValidationException;
import com.ironmountain.imtest.security.token.auth.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@AllArgsConstructor
public class SecurityContextRepository implements org.springframework.security.web.context.SecurityContextRepository {

    private final TokenProvider jwtTokenProvider;
    private static final String AUTHORIZATION="Authorization";

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder httpRequestResponseHolder) {
        String token = jwtTokenProvider.resolveToken(httpRequestResponseHolder.getRequest().getHeader(AUTHORIZATION));
        if (Objects.isNull(token)) {
            return new SecurityContextImpl();
        }
        try {
            jwtTokenProvider.validateToken(token);
        } catch (TokenValidationException e) {
            return new SecurityContextImpl();
        }
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        return new SecurityContextImpl(auth);
    }

    @Override
    public void saveContext(SecurityContext securityContext, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }

    @Override
    public boolean containsContext(HttpServletRequest httpServletRequest) {
        return false;
    }

}
