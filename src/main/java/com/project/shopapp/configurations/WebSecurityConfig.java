package com.project.shopapp.configurations;

import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix)
                            )
                            .permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/categories?**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER, Role.ADMIN)
                            .requestMatchers(POST,
                                    String.format("%s/categories?**",apiPrefix)).hasAnyRole(Role.MANAGER, Role.ADMIN)
                            .requestMatchers(PUT,
                                    String.format("%s/categories?**",apiPrefix)).hasAnyRole(Role.MANAGER, Role.ADMIN)
                            .requestMatchers(DELETE,
                                    String.format("%s/categories?**",apiPrefix)).hasAnyRole(Role.MANAGER, Role.ADMIN)

                            .requestMatchers(GET,
                                    String.format("%s/products**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER, Role.ADMIN)
                            .requestMatchers(POST,
                                    String.format("%s/products**",apiPrefix)).hasAnyRole(Role.MANAGER, Role.ADMIN)
                            .requestMatchers(PUT,
                                    String.format("%s/products**",apiPrefix)).hasAnyRole(Role.MANAGER, Role.ADMIN)
                            .requestMatchers(DELETE,
                                    String.format("%s/products**",apiPrefix)).hasAnyRole(Role.MANAGER, Role.ADMIN)

                            .requestMatchers(POST,
                                    String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER)
                            .requestMatchers(GET,
                                    String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER)
                            .requestMatchers(PUT,
                                    String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER)
                            .requestMatchers(DELETE,
                                    String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER)

                            .requestMatchers(POST,
                                    String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER)
                            .requestMatchers(GET,
                                    String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER)
                            .requestMatchers(PUT,
                                    String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER)
                            .requestMatchers(DELETE,
                                    String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.STAFF, Role.MANAGER)

                            .anyRequest().authenticated();
                });

        //config cors
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.addExposedHeader(List.of("x-auth-token").toString());
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}
