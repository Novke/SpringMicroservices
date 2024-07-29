package fon.mas.novica.spring.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurity {

    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
         http.authorizeHttpRequests(
                auth ->
                        auth.anyRequest().permitAll()
        );

         http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
