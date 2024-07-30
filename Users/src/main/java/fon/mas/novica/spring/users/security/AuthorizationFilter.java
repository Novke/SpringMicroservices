package fon.mas.novica.spring.users.security;

import fon.mas.novica.spring.users.service.UsersService;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {
    private final Environment environment;
    private final UsersService usersService;
    private final String headerName;
    private final String headerPrefix;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UsersService usersService, Environment environment) {
        super(authenticationManager);
        this.environment = environment;
        this.usersService = usersService;
        headerName = environment.getProperty("token.header.name");
        headerPrefix = environment.getProperty("token.header.prefix");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = request.getHeader(headerName);

        if (authorizationHeader == null
                || !authorizationHeader.startsWith(headerPrefix)){
            chain.doFilter(request, response);
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(headerName);

        if (authorizationHeader == null){
            return null;
        }

        String token = authorizationHeader.replace(headerPrefix, "");
        String tokenSecret = environment.getProperty("token.secret");

        if (tokenSecret==null) return null;

        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build();

        String username;
        try {
            Jwt<Header, Claims> jwt = jwtParser.parse(token);
            username = jwt.getBody().getSubject();
        } catch (ExpiredJwtException ex){
            log.debug("Expired JWT! ", ex);
            return null;
        }

        if (username == null){
            log.debug("Username is null");
            return null;
        }

        UserDetails userDetails = usersService.loadUserByUsername(username);

        log.debug("Authorization successful.");
        return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
    }
}
