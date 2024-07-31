package fon.mas.novica.spring.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fon.mas.novica.spring.users.model.dto.login.LoginRequestCmd;
import fon.mas.novica.spring.users.service.UsersService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UsersService usersService;
    private final Environment environment;


    public AuthenticationFilter(AuthenticationManager authenticationManager, UsersService usersService, Environment environment) {
        super(authenticationManager);
        this.usersService = usersService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestCmd credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequestCmd.class);

            log.debug("User tried to login: " + credentials.getUsername());

            UserDetails user = usersService.loadUserByUsername(credentials.getUsername());

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            user.getAuthorities()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDetails userDetails = usersService.loadUserByUsername(username);

        String tokenSecret = environment.getProperty("token.secret");
        String expTimeString = environment.getProperty("token.expiration.time");
        Long tokenExpirationTime = expTimeString == null ? 360000L : Long.parseLong(expTimeString);

        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        log.debug("Successful auth! Building JWT...");

        String token = generateToken(userDetails, secretKey, tokenExpirationTime);

        response.addHeader("id-token", token);
        response.addHeader("curr-user", userDetails.getUsername());
    }

    private String generateToken(UserDetails userDetails, SecretKey secretKey, Long tokenExpirationTime) {
        // Extract roles from UserDetails
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Add roles to claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        //Building JWT
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(tokenExpirationTime)))
                .addClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

}
