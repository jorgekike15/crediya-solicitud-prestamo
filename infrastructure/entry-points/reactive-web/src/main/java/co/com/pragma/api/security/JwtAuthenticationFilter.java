package co.com.pragma.api.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;

public class JwtAuthenticationFilter extends AuthenticationWebFilter {
    public JwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JwtAuthenticationFilter(ReactiveAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver) {
        super(authenticationManagerResolver);
    }
}