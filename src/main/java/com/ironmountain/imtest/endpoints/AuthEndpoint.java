package com.ironmountain.imtest.endpoints;

import com.ironmountain.imtest.security.token.auth.TokenProvider;
import com.ironmountain.imtest.services.UserService;
import com.ironmountain.imtest.transfer.Authentication;
import com.ironmountain.imtest.transfer.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthEndpoint {
    private final UserService userService;
    private final TokenProvider jwtTokenProvider;

    @PostMapping("sign-in")
    public ResponseEntity<Authentication> signIn(@RequestBody Credentials credentials) {
        String token = userService.authenticate(credentials.getUsername(), credentials.getPassword(), credentials.isKeepLogged());
        var userId = jwtTokenProvider.getUserId(token);
        return ResponseEntity.ok(Authentication.builder().token(token)
                .id(userId)
                .build());
    }
}
