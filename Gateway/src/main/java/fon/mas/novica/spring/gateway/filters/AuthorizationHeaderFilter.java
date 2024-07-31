package fon.mas.novica.spring.gateway.filters;

import fon.mas.novica.spring.gateway.exception.TokenDecoderUnavailableException;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class AuthorizationHeaderFilter implements GlobalFilter {

    @Autowired
    private Environment environment;

    @RequiredArgsConstructor
    @Getter
    private enum AllowedRequests {
        LOGIN(HttpMethod.POST, "/login");

        private final HttpMethod method;
        private final String path;
    }
    private boolean isRequestWhitelisted(HttpMethod method, String path) {
        AntPathMatcher pathMatcher = new AntPathMatcher();

        return Arrays.stream(AllowedRequests.values()).anyMatch(route ->
                route.getMethod().equals(method) && pathMatcher.match(route.getPath(),path)
        );
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("Global filter is being executed...");
        ServerHttpRequest request = exchange.getRequest();

        HttpMethod method = request.getMethod();
        String path = request.getPath().toString();
        log.debug("Request {} on path {}", method, path);

        if (isRequestWhitelisted(method, path)){
            log.debug("Request for {} is whitelisted...", path);
            return chain.filter(exchange);
        }

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
            log.debug("There is no authorization header");
            return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
        }

        String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        String jwt = authorizationHeader.replace("Bearer", "").trim();

        try {
            if (!isJwtValid(jwt)) {
                log.debug("JWT is not valid");
                return onError(exchange, "JWT token is not valid!", HttpStatus.UNAUTHORIZED);
            }
        } catch (TokenDecoderUnavailableException ex){
            log.error(ex.getMessage(), ex);
            return onError(exchange, ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }

        log.debug("Request successfully filtered. Invoking chain...");
        return chain.filter(exchange);
    }


    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    private boolean isJwtValid(String jwt){
        boolean isValid = true;

        String tokenSecret = getTokenSecret();

        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey key = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        String subject = null;
        try {
            Jwt<Header, Claims> parsedToken = jwtParser.parse(jwt);
            subject = parsedToken.getBody().getSubject();
            if (subject == null || subject.isEmpty()) isValid = false;
            if (parsedToken.getBody().getExpiration().before(new Date())) isValid = false;

        } catch (Exception ex){
            isValid = false;
        }

        return isValid;
    }

    private String getTokenSecret(){
        String secret = environment.getProperty("token.secret");
        if (secret == null || secret.isBlank()) throw new TokenDecoderUnavailableException("Cant locate token secret. Try again later!");

        return secret;
    }
}
