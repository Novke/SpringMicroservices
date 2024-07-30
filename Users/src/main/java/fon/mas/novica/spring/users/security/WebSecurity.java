package fon.mas.novica.spring.users.security;

import fon.mas.novica.spring.users.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import java.util.List;

@EnableWebSecurity
@Configuration
@Slf4j
public class WebSecurity {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UsersService usersService;
    @Autowired
    Environment environment;
//    @Autowired
//    DiscoveryClient discoveryClient;
    @Autowired
    ApplicationContext app;

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

//        String gatewayAddr = environment.getProperty("gateway.ip");
        final String gatewayAddr = gatewayIpAddress();

         http.authorizeHttpRequests(
                auth ->
                        auth

                                .requestMatchers("/login").permitAll()

//                                CHECK
                                .requestMatchers("/check").permitAll()
                                .requestMatchers("/user").authenticated()
                                .requestMatchers("/admin").hasRole("ADMIN")

//                                USERS CONTROLLER
                                .requestMatchers("/users/all").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/users/**").authenticated()
                                .requestMatchers("/users/**").hasRole("ADMIN")

                                //problem je sto se ovo ignorise ako je neki od onih gore uslova zadovoljen
                                .requestMatchers("/users/**").access(
                                        new WebExpressionAuthorizationManager(
                                                "hasIpAddress('" + gatewayAddr +"')"))

                                .anyRequest().permitAll()
        );

         http.csrf(AbstractHttpConfigurer::disable);

         http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

         http.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    private String gatewayIpAddress(){
//        List<ServiceInstance> instances = discoveryClient.getInstances("Gateway");
//        if (instances != null && !instances.isEmpty()){
//            try {
//                EurekaServiceInstance gateway = (EurekaServiceInstance) instances.get(0);
//                return gateway.getInstanceInfo().getIPAddr();
//            } catch (ClassCastException ignored){
//                return instances.get(0).getHost();
//            }
//        }
////        throw new RuntimeException("Gateway not found!!!");
        log.warn("GATEWAY NOT FOUND!!! FALLING BACK TO DEFAULT ADDRESS");
        return environment.getProperty("gateway.ip");
    }
}
