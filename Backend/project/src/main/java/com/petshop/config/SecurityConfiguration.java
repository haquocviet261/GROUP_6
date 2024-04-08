package com.petshop.config;


import com.petshop.models.dto.response.JwtEntryPoint;
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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtAuthenicationFilter jwtAuthenicationFilter;
    @Autowired
    private LogoutHandler logoutHandler;

    @Autowired


    public static Logger logger = LoggerFactory.getLogger((SecurityConfiguration.class));
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.cors(Customizer.withDefaults())
                    .csrf(csrf -> csrf
                            .ignoringRequestMatchers("/chat/**")
                    )
                    .headers(headers -> headers
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                            )
                    ).authorizeHttpRequests(authorizeRequests ->
                            authorizeRequests
                                    .requestMatchers("api/auth/**","api/v1/user/**","/login/**").permitAll()
                                    .requestMatchers("/api/v1/admin/**").hasAuthority(admin.name())
                                    .anyRequest()
                                    .authenticated()

                    ).oauth2Login((oauth2Login) -> oauth2Login.loginPage("/login/oauth2/sign-in-google")
                            .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/oauth2/callback/google"))
                            .failureHandler((request, response, exception) -> response.sendRedirect("/login/oauth2/authorization"))
                            .successHandler((request, response, authentication) -> response.sendRedirect("/login/oauth2/google"))
                            .permitAll()
                    ).sessionManagement(session -> session.sessionCreationPolicy(IF_REQUIRED))

                    .logout(httpSecurityLogoutConfigurer ->
                            httpSecurityLogoutConfigurer.logoutUrl("/logout/oauth2")
                                    .clearAuthentication(true).
                                    deleteCookies("JSESSIONID")
                                    .logoutSuccessUrl("/api/auth/authenticate")
                                    .invalidateHttpSession(true)
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
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("756081284225-qk5ijqli6cuope3q2j3mdlm2ckgtvedb.apps.googleusercontent.com")
                .clientSecret("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")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:9999/login/oauth2/code/google")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
