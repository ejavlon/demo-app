package uz.ejavlon.demoapp.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.ejavlon.demoapp.filter.JwtAuthenticationFilter;

import static uz.ejavlon.demoapp.enums.Role.ADMIN;
import static uz.ejavlon.demoapp.enums.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize)->authorize
                        .requestMatchers(HttpMethod.POST,"/api/v1/users/profile/*").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/users/profile").hasRole(USER.name())

                        .requestMatchers(HttpMethod.GET,"/api/v1/mealplan","/api/v1/mealplan/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/mealplan/*").hasRole(ADMIN.name())
                        .requestMatchers("/**").hasRole(ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());

        return http.build();
    }
}
