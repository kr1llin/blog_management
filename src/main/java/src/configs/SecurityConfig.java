package src.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/profile").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/posts/mine").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/posts/create").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/posts/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/posts/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                ).formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .usernameParameter("user")
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/design")
                        .permitAll())
                .logout((log) -> log.logoutSuccessUrl("/"))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
