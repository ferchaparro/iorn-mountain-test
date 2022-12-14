package com.ironmountain.imtest.security.token;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AbstractTokenProvider {

    @Value("${jwt.token.key}")
    protected String secretKey;
    protected String jwtKey;

    @PostConstruct
    protected void init() {
        jwtKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    protected String createToken(String subject, List<Claim<?>> claimList, @Nullable Date expiration) {
        Claims claims = Jwts.claims().setSubject(subject);
        claimList.forEach(claim -> claims.put(claim.key, claim.value));

        Date now = new Date();

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now).compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS256, jwtKey);

        if(Objects.nonNull(expiration)) {
            builder.setExpiration(expiration);
        }
        return builder.compact();
    }

    public String resolveToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    protected Jws<Claims> getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(resolveToken(token));
    }

    protected static class Claim <T> {
        private final String key;
        private final T value;

        public Claim(String key, T value) {
            this.key = key;
            this.value = value;
        }

    }
}