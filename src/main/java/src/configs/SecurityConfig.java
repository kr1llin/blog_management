package src.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import src.proxies.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    JwtAuthenticationFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    @Order(2)
//    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/users/register").permitAll()
//                        .requestMatchers("/h2-console/**").permitAll()
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/users/profile").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/posts/mine").hasRole("USER")
//                        .requestMatchers(HttpMethod.POST, "/posts/create").hasRole("USER")
//                        .requestMatchers(HttpMethod.PUT, "/posts/**").hasRole("USER")
//                        .requestMatchers(HttpMethod.DELETE, "/posts/**").hasAnyRole("USER", "ADMIN")
//                ).formLogin((form) -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/authenticate")
//                        .usernameParameter("user")
//                        .passwordParameter("pwd")
//                        .defaultSuccessUrl("/design")
//                        .permitAll())
//                .logout((log) -> log.logoutSuccessUrl("/"))
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
}
