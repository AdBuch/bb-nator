package ch.bbnator.config;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/** Explicit synthetic demo mode. Never enable on a public-facing deployment. */
@Configuration
@Profile("demo")
public class DemoSecurityConfiguration {
    @Bean
    SecurityFilterChain demoSecurityFilterChain(HttpSecurity http, org.springframework.core.env.Environment environment) throws Exception {
        DemoSafetyGuard.check(environment);
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/system/status", "/actuator/health").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/demo/learners", "/api/v1/demo/tasks").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/demo/tasks").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/v1/demo/tasks/*").permitAll()
                .anyRequest().denyAll())
            .build();
    }
}
