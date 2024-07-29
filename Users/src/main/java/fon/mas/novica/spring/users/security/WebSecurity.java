package fon.mas.novica.spring.users.security;

import fon.mas.novica.spring.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurity {

//    @Value("${gateway.ip}")
//    private String gatewayIp;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UsersService usersService;
    @Autowired
    Environment environment;

    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder
                .userDetailsService(usersService)
                .passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = managerBuilder.build();
        http.authenticationManager(authenticationManager);
        http.addFilter(new AuthenticationFilter(authenticationManager, usersService, environment));
        http.addFilter(new AuthorizationFilter(authenticationManager, usersService, environment));

         http.authorizeHttpRequests(
                auth ->
                        auth
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/users/all").hasRole("ADMIN")
                                .requestMatchers("/**").authenticated()


                                .anyRequest().permitAll()
        );

//         http.authorizeHttpRequests(auth ->
//                 auth.requestMatchers("/**")
//                         .access((new WebExpressionAuthorizationManager(
//                                 "hasIpAddress('" + gatewayIp + "')"
//                         ))));

         http.csrf(AbstractHttpConfigurer::disable);

         http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

         http.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}
