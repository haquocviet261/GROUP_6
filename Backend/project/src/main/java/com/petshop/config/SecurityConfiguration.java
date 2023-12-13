package com.petshop.config;


import com.petshop.models.dto.response.JwtEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static com.petshop.common.constant.Role.admin;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtEntryPoint jwtEntryPoint;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtAuthenicationFilter jwtAuthenicationFilter;
    @Autowired
    private LogoutHandler logoutHandler;
    public static Logger logger = LoggerFactory.getLogger((SecurityConfiguration.class));
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            http.cors(Customizer.withDefaults())
                    .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authorizeRequests ->
                            authorizeRequests
                                    .requestMatchers("api/auth/**","api/v1/user/**","api/v1/home/**").permitAll()
                                    .requestMatchers("/api/v1/admin/**").hasAuthority(admin.name())
                                    .anyRequest()
                                    .authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .exceptionHandling(entryPoint -> entryPoint.authenticationEntryPoint(jwtEntryPoint))
                    .addFilterBefore(jwtAuthenicationFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout(logout ->
                            logout.logoutUrl("/api/auth/logout")
                                    .addLogoutHandler(logoutHandler)
                                    .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                    );
        }catch (Exception e){
            logger.error("Error -> {}",e.getMessage());
        }
        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
