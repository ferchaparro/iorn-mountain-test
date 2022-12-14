package com.ironmountain.imtest.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityContextRepository contextRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Disable CSRF (cross site request forgery)
        http.cors()
                .and()
        .csrf().disable()

                .headers()
                .frameOptions().disable()
                .and()


        // No session will be created or used by spring security
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

        // Entry points
        .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**/auth/sign-in/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**/contacts/save").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/**/contacts/update").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/**/contacts/by-id/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                        .and()

                .securityContext(config -> config.securityContextRepository(contextRepository))

                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();


    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.POST, "/**/auth/sign-in/**")
                .antMatchers("/h2-console/**")
                .antMatchers(HttpMethod.OPTIONS, "/**"); // Request type options should be allowed.
    }

}
