package com.petshop.config;


import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static com.petshop.common.constant.Role.admin;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.MalformedURLException;
import java.net.URL;

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

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private  String jwkUri;

    @Autowired
    public static Logger logger = LoggerFactory.getLogger((SecurityConfiguration.class));
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.cors(Customizer.withDefaults())
                    .csrf(csrf -> csrf
                            // ignore our stomp endpoints since they are protected using Stomp headers
                            .ignoringRequestMatchers("/**")
                    )
                    .headers(headers -> headers
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                    ).authorizeHttpRequests(authorizeRequests ->
                            authorizeRequests
                                    .requestMatchers("api/user/**","/**").permitAll()
                                    .requestMatchers("/api/v1/admin/**").hasAuthority(admin.name())
                                    .anyRequest()
                                    .authenticated()

                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(IF_REQUIRED))
                    .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .exceptionHandling(entryPoint -> entryPoint.authenticationEntryPoint(jwtEntryPoint))
                    .addFilterBefore(jwtAuthenicationFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout(logout ->
                            logout.logoutUrl("/api/auth/logout")
                                    .addLogoutHandler(logoutHandler)
                                    .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                    );

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

    @Bean
    public JwtDecoder jwtDecoder() throws KeySourceException, MalformedURLException {
        URL jwkSetUrl;
        jwkSetUrl = new URL(jwkUri);
        // Makes a request to the JWK Set endpoint
        JWSKeySelector<SecurityContext> jwsKeySelector =
                JWSAlgorithmFamilyJWSKeySelector.fromJWKSetURL(jwkSetUrl);

        DefaultJWTProcessor<SecurityContext> jwtProcessor =
                new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);

        return new NimbusJwtDecoder(jwtProcessor);
    }
//.oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->{
//        httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer -> {
//            try {
//                jwtConfigurer.decoder(jwtDecoder());
//            } catch (MalformedURLException | KeySourceException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    })

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
