package com.ironmountain.imtest.security.token.auth;

import com.ironmountain.imtest.exceptions.BusinessException;
import com.ironmountain.imtest.exceptions.messages.Msg;
import com.ironmountain.imtest.security.exceptions.TokenValidationException;
import com.ironmountain.imtest.security.token.AbstractTokenProvider;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider extends AbstractTokenProvider implements TokenProvider{
    private static final String AUTH = "auth";
    private static final String USER_ID = "user_id";

    @Value("${jwt.token.expiration}")
    private long expiration;


    @Override
    public String createToken(long userId, String username, boolean keepLogged, List<String> roles) {

        Date validity = null;
        if(!keepLogged) {
            Date now = new Date();
            validity = new Date(now.getTime() + (expiration * 60 * 60 * 1000));
        }
        List<Claim<?>> claims = List.of(
                new Claim<>(USER_ID, userId),
                new Claim<>(AUTH, roles)
        );
        return createToken(username, claims, validity);
    }

    @Override
    public boolean validateToken(String token) throws TokenValidationException {
        try {
            var _expiration = getClaims(token).getBody().getExpiration();
            return _expiration==null || new Date().before(_expiration);
        } catch (JwtException | IllegalArgumentException e){
            throw new TokenValidationException();
        }

    }

    @Override
    public List<String> getRoleList(String token) {
        List<?> roles = getClaims(token).getBody().get(AUTH, List.class);
        return roles.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public long getUserId(String token) {
        Long id = getClaims(token).getBody().get(USER_ID, Long.class);
        if(Objects.isNull(id)){
            throw new BusinessException(Msg.TOKEN_USER_ID_NULL);
        }
        return id;
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getBody().getSubject();
    }


    @Override
    public UserDetails getUserDetails(String token) {
        String userName =  getUsername(token);
        List<String> roleList = getRoleList(token);
        return new UserDetailsImpl(userName, roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

}
