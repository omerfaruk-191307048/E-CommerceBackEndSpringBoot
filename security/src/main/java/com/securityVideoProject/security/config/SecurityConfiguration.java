package com.securityVideoProject.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private String[] WHITE_LIST_URL;

    @Bean
    //spring security, securityfilterchain bean'i aramaya başlar bunun için bunu oluştruruz. Securityfilterchain uygulamamızın tüm HTTP güvenliğini yapılandırmaktan sorumludur
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        WHITE_LIST_URL = new String[]{
                "/api/v1/auth/**",
                "/api/orders/**",
                "/api/products/**",
                "/api/orders",
                "/api/products",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"
        };
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler) //Tüm çıkış mekanizmasını uygulamak istediğimiz yer
                                .logoutSuccessHandler((request, response, authentication) -> //security context cleaning-Kullanıcı oturumu kapatırsa, süresi dolmuş ve geçersiz token'lar ile ilgili API'lerimize erişemeyecek
                                        SecurityContextHolder.clearContext())
                )

        ;
        return httpSecurity.build();
    }
}